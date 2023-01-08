package br.com.attornatus.peopleapi.service;

import br.com.attornatus.peopleapi.dto.endereco.EnderecoCreateDTO;
import br.com.attornatus.peopleapi.dto.endereco.EnderecoDTO;
import br.com.attornatus.peopleapi.exceptions.RegraDeNegocioException;
import br.com.attornatus.peopleapi.model.Endereco;
import br.com.attornatus.peopleapi.model.Pessoa;
import br.com.attornatus.peopleapi.repository.EnderecoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnderecoService {

    private final ObjectMapper objectMapper;

    private final EnderecoRepository enderecoRepository;

    private final PessoaService pessoaService;

    public List<EnderecoDTO> listAllById(Integer idPessoa) throws RegraDeNegocioException {
        Pessoa pessoa = pessoaService.findById(idPessoa);
        List<EnderecoDTO> enderecoDTOS = pessoa.getEnderecoList().stream()
                .map(this::retornarDTO)
                .toList();
        return enderecoDTOS;
    }

    public List<EnderecoDTO> postEnderecoPrincipal(Integer idPessoa, Integer idEndereco) throws RegraDeNegocioException {
        Pessoa pessoa = pessoaService.findById(idPessoa);
        findById(idEndereco);

        List<EnderecoDTO> enderecoPrincipalDTOS2 = pessoa.getEnderecoList().stream()
                .filter(endereco -> !endereco.getIdEndereco().equals(idEndereco))
                .map(endereco -> {
                    endereco.setPrincipal(false);
                    enderecoRepository.save(endereco);
                    return objectMapper.convertValue(endereco, EnderecoDTO.class);
                })
                .toList();

        List<EnderecoDTO> enderecoPrincipalDTOS = new ArrayList<>(pessoa.getEnderecoList().stream()
                .filter(endereco -> endereco.getIdEndereco().equals(idEndereco))
                .map(endereco -> {
                    endereco.setPrincipal(true);
                    enderecoRepository.save(endereco);
                    return objectMapper.convertValue(endereco, EnderecoDTO.class);
                })
                .toList());

        enderecoPrincipalDTOS.addAll(enderecoPrincipalDTOS2);

        return enderecoPrincipalDTOS;
    }

    public EnderecoDTO create (EnderecoCreateDTO enderecoCreateDTO, Integer idPessoa) throws RegraDeNegocioException {
        Pessoa pessoaLocalizada = pessoaService.findById(idPessoa);
        Endereco endereco = converterDTO(enderecoCreateDTO);

        if (pessoaLocalizada.getEnderecoList().size() != 0) {
            endereco.setPrincipal(false);
        } else {
            endereco.setPrincipal(true);
        }

        List<Pessoa> pessoaList = new ArrayList<>();
        pessoaList.add(pessoaLocalizada);

        endereco.setPessoaList(pessoaList);
        return retornarDTO(enderecoRepository.save(endereco));
    }

    public Endereco converterDTO (EnderecoCreateDTO enderecoCreateDTO) {
        return objectMapper.convertValue(enderecoCreateDTO, Endereco.class);
    }

    public EnderecoDTO retornarDTO (Endereco endereco) {
        return objectMapper.convertValue(endereco, EnderecoDTO.class);
    }

    public Endereco findById(Integer idEndereco) throws RegraDeNegocioException {
        return enderecoRepository.findById(idEndereco)
                .orElseThrow(() -> new RegraDeNegocioException("Endereço não encontrada"));
    }
}
