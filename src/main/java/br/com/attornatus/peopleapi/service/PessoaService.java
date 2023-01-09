package br.com.attornatus.peopleapi.service;

import br.com.attornatus.peopleapi.dto.pessoa.PessoaCreateDTO;
import br.com.attornatus.peopleapi.dto.pessoa.PessoaDTO;
import br.com.attornatus.peopleapi.dto.pessoa.PessoaPutDTO;
import br.com.attornatus.peopleapi.exceptions.RegraDeNegocioException;
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

    //Recupera uma pessoa com base em seu id ou lança uma exceção quando não encontrada
    public PessoaDTO findPersonById (Integer idPessoa) throws RegraDeNegocioException {
        return pessoaRepository.findById(idPessoa).stream()
                .map(pessoa -> {
                    PessoaDTO pessoaDTO;
                    pessoaDTO = objectMapper.convertValue(pessoa, PessoaDTO.class);
                    pessoaDTO.setEnderecoDTOList(objectMapper.convertValue(pessoa.getEnderecoList(), List.class));
                    return pessoaDTO;
                })
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Pessoa não encontrada"));
    }

    //Recupera todas as pessoas da base de dados
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

    //cria uma pessoa, podendo criar junto (ou nao) um ou mais endereços.
    public PessoaDTO create (PessoaCreateDTO pessoaCreateDTO) throws RegraDeNegocioException {
        Pessoa pessoa = converterDTO(pessoaCreateDTO);

        if (pessoaCreateDTO.getEnderecoDTOList() != null) {

            List<Endereco> enderecoList = pessoaCreateDTO.getEnderecoDTOList().stream()
                    .map(enderecoDTO -> {
                        enderecoDTO.setPrincipal(false);
                        return enderecoRepository.save(objectMapper.convertValue(enderecoDTO, Endereco.class));
                    })
                    .toList();

            enderecoList.stream()
                    .map(enderecoDTO -> {
                        enderecoDTO.setPrincipal(true);
                        return enderecoRepository.save(objectMapper.convertValue(enderecoDTO, Endereco.class));
                    })
                    .findFirst()
                    .orElseThrow(() -> new RegraDeNegocioException("Erro na requisição"));

            pessoa.setEnderecoList(enderecoList);
            pessoa = pessoaRepository.save(pessoa);

            PessoaDTO pessoaDTO = retornarDTO(pessoa);
            pessoaDTO.setEnderecoDTOList(objectMapper.convertValue(pessoa.getEnderecoList(), List.class));

            return pessoaDTO;
        } else {
            pessoa = pessoaRepository.save(pessoa);
            return retornarDTO(pessoa);
        }
    }

    //atualiza o cadastro de uma pessoa na base de dados
    public PessoaDTO update (PessoaPutDTO pessoaCreateDTO, Integer idPessoa) throws RegraDeNegocioException {
        Pessoa pessoaLocalizada = findById(idPessoa);
        PessoaDTO pessoaDTO;

        pessoaLocalizada.setNome(pessoaCreateDTO.getNome());
        pessoaLocalizada.setDataNascimento(pessoaCreateDTO.getDataNascimento());

        if (pessoaCreateDTO.getEnderecoDTOList() != null) {
            List<Pessoa> pessoaList = new ArrayList<>();
            pessoaList.add(pessoaLocalizada);

            List<Endereco> enderecosLocalizado = pessoaCreateDTO.getEnderecoDTOList().stream()
                    .map(enderecoDTO -> {
                        Endereco endereco;
                        try {
                            endereco = enderecoRepository.findById(enderecoDTO.getIdEndereco()).orElseThrow(() -> new RegraDeNegocioException("Endereço não encontrado"));
                        } catch (RegraDeNegocioException e) {
                            throw new RuntimeException(e);
                        }
                        enderecoDTO.setPrincipal(endereco.getPrincipal());
                        return objectMapper.convertValue(enderecoDTO, Endereco.class);
                    })
                    .map(endereco -> {
                        endereco.setPessoaList(pessoaList);
                        return enderecoRepository.save(endereco);
                    })
                    .toList();

            pessoaDTO = retornarDTO(pessoaLocalizada);
            pessoaDTO.setEnderecoDTOList(objectMapper.convertValue(enderecosLocalizado, List.class));
        } else {
            pessoaRepository.save(pessoaLocalizada);
            pessoaDTO = retornarDTO(pessoaLocalizada);
        }
        return pessoaDTO;
    }

    public Pessoa converterDTO (PessoaCreateDTO pessoaCreateDTO) {
        return objectMapper.convertValue(pessoaCreateDTO, Pessoa.class);
    }

    public PessoaDTO retornarDTO (Pessoa pessoa) {
        return objectMapper.convertValue(pessoa, PessoaDTO.class);
    }

    //Recupera uma pessoa por id ou caso contrario, lança uma exceção
    public Pessoa findById(Integer idPessoa) throws RegraDeNegocioException {
        return pessoaRepository.findById(idPessoa)
                .orElseThrow(() -> new RegraDeNegocioException("Pessoa não encontrada"));
    }
}
