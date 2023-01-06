package br.com.attornatus.peopleapi.service;

import br.com.attornatus.peopleapi.dto.endereco.EnderecoCreateDTO;
import br.com.attornatus.peopleapi.dto.endereco.EnderecoDTO;
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

    public List<EnderecoDTO> listAllById(Integer idPessoa) {
        Pessoa pessoaLocalizada = pessoaService.findById(idPessoa);
        List<EnderecoDTO> enderecoDTOList =pessoaLocalizada.getEnderecoList().stream()
                .map(this::retornarDTO)
                .toList();
        return enderecoDTOList;
    }

    public EnderecoDTO create (EnderecoCreateDTO enderecoCreateDTO, Integer idPessoa) {
        Pessoa pessoaLocalizada = pessoaService.findById(idPessoa);
        Endereco endereco = converterDTO(enderecoCreateDTO);

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
}
