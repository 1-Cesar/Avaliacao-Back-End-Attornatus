package br.com.attornatus.peopleapi.service;

import br.com.attornatus.peopleapi.dto.pessoa.PessoaCreateDTO;
import br.com.attornatus.peopleapi.dto.pessoa.PessoaDTO;
import br.com.attornatus.peopleapi.model.Endereco;
import br.com.attornatus.peopleapi.model.Pessoa;
import br.com.attornatus.peopleapi.repository.EnderecoRepository;
import br.com.attornatus.peopleapi.repository.PessoaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final ObjectMapper objectMapper;

    private final PessoaRepository pessoaRepository;

    private final EnderecoRepository enderecoRepository;

    public PessoaDTO findPersonById (Integer idPessoa) {
        return pessoaRepository.findById(idPessoa).stream()
                .map(pessoa -> {
                    PessoaDTO pessoaDTO;
                    pessoaDTO = objectMapper.convertValue(pessoa, PessoaDTO.class);
                    pessoaDTO.setEnderecoDTOList(objectMapper.convertValue(pessoa.getEnderecoList(), List.class));
                    return pessoaDTO;
                })
                .findFirst()
                .orElseThrow();
    }

    public List<PessoaDTO> listAll () {
        return pessoaRepository.findAll().stream()
                .map(pessoa -> {
                    PessoaDTO pessoaDTO;
                    pessoaDTO = objectMapper.convertValue(pessoa, PessoaDTO.class);
                    pessoaDTO.setEnderecoDTOList(objectMapper.convertValue(pessoa.getEnderecoList(), List.class));
                    return pessoaDTO;
                })
                .collect(Collectors.toList());
    }

    public PessoaDTO create (PessoaCreateDTO pessoaCreateDTO) {
        Pessoa pessoa = converterDTO(pessoaCreateDTO);

        if (pessoaCreateDTO.getEnderecoDTOList() != null) {
            List<Endereco> enderecoList = pessoaCreateDTO.getEnderecoDTOList().stream()
                            .map(enderecoDTO -> enderecoRepository.save(objectMapper.convertValue(enderecoDTO, Endereco.class)))
                            .toList();

            pessoa.setEnderecoList(enderecoList);
            pessoaRepository.save(pessoa);

            PessoaDTO pessoaDTO = retornarDTO(pessoa);
            pessoaDTO.setEnderecoDTOList(objectMapper.convertValue(pessoa.getEnderecoList(), List.class));

            return pessoaDTO;
        } else {
            pessoaRepository.save(pessoa);
            return retornarDTO(pessoa);
        }
    }

    public PessoaDTO update (PessoaCreateDTO pessoaCreateDTO, Integer idPessoa) {
        Pessoa pessoaLocalizada = findById(idPessoa);

        pessoaLocalizada.setNome(pessoaCreateDTO.getNome());
        pessoaLocalizada.setDataNascimento(pessoaCreateDTO.getDataNascimento());

        List<Pessoa> pessoaList = new ArrayList<>();
        pessoaList.add(pessoaLocalizada);

        List<Endereco> enderecosLocalizado = pessoaCreateDTO.getEnderecoDTOList().stream()
                .map(enderecoDTO -> {
                    pessoaRepository.findById(enderecoDTO.getIdEndereco());
                    return objectMapper.convertValue(enderecoDTO, Endereco.class);
                })
                .map(endereco -> {
                    endereco.setPessoaList(pessoaList);
                    return enderecoRepository.save(endereco);
                })
                .toList();

        PessoaDTO pessoaDTO = retornarDTO(pessoaLocalizada);
        pessoaDTO.setEnderecoDTOList(objectMapper.convertValue(enderecosLocalizado, List.class));

        return pessoaDTO;
    }

    public Pessoa converterDTO (PessoaCreateDTO pessoaCreateDTO) {
        return objectMapper.convertValue(pessoaCreateDTO, Pessoa.class);
    }

    public PessoaDTO retornarDTO (Pessoa pessoa) {
        return objectMapper.convertValue(pessoa, PessoaDTO.class);
    }

    public Pessoa findById(Integer idPessoa) {
        return pessoaRepository.findById(idPessoa)
                .orElseThrow();
    }
}
