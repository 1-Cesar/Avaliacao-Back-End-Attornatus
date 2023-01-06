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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final ObjectMapper objectMapper;

    private final PessoaRepository pessoaRepository;

    private final EnderecoRepository enderecoRepository;

    public PessoaDTO findPersonById (Integer idPessoa) {
        return retornarDTO(findById(idPessoa));
    }

    public List<PessoaDTO> listAll () {
        return pessoaRepository.findAll().stream()
                .map(this::retornarDTO)
                .collect(Collectors.toList());
    }

    public PessoaDTO create (PessoaCreateDTO pessoaCreateDTO) {
        Pessoa pessoa = converterDTO(pessoaCreateDTO);

        List<Endereco> enderecoList = pessoaCreateDTO.getEnderecoCreateDTOList().stream()
                .map(enderecoCreateDTO -> enderecoRepository.save(objectMapper.convertValue(enderecoCreateDTO, Endereco.class)))
                .toList();

        pessoa.setEnderecoList(enderecoList);

        return retornarDTO(pessoaRepository.save(pessoa));
    }

    public PessoaDTO update (PessoaCreateDTO pessoaCreateDTO, Integer idPessoa) {
        Pessoa pessoaLocalizada = findById(idPessoa);

        pessoaLocalizada.setNome(pessoaCreateDTO.getNome());
        pessoaLocalizada.setDataNascimento(pessoaCreateDTO.getDataNascimento());

        return retornarDTO(pessoaRepository.save(pessoaLocalizada));
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
