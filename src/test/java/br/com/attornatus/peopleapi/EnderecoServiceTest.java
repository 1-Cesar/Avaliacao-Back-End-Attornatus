package br.com.attornatus.peopleapi;

import br.com.attornatus.peopleapi.dto.endereco.EnderecoCreateDTO;
import br.com.attornatus.peopleapi.dto.endereco.EnderecoDTO;
import br.com.attornatus.peopleapi.exceptions.RegraDeNegocioException;
import br.com.attornatus.peopleapi.model.Endereco;
import br.com.attornatus.peopleapi.model.Pessoa;
import br.com.attornatus.peopleapi.repository.EnderecoRepository;
import br.com.attornatus.peopleapi.service.EnderecoService;
import br.com.attornatus.peopleapi.service.PessoaService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {EnderecoService.class})
@RunWith(MockitoJUnitRunner.class)
public class EnderecoServiceTest {

    @InjectMocks
    private EnderecoService enderecoService;

    @Mock
    private PessoaService pessoaService;

    @Mock
    private EnderecoRepository enderecoRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(enderecoService, "objectMapper", objectMapper);
    }

    @Test
    public void deveTestarListAll() throws RegraDeNegocioException {
        Pessoa pessoa = getPessoa();

        when(pessoaService.findById(anyInt())).thenReturn(pessoa);

        List<EnderecoDTO> enderecoDTOList = enderecoService.listAllById(anyInt());

        assertNotNull(enderecoDTOList);
    }

    @Test
    public void deveTestarPostEnderecoPrincipal() throws RegraDeNegocioException {
        Pessoa pessoa = getPessoa();
        List<Endereco> enderecoList = List.of(getEndereco(), getEndereco2());
        pessoa.setEnderecoList(enderecoList);
        Endereco endereco = getEndereco();

        deveTestarFindById();
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);
        when(pessoaService.findById(anyInt())).thenReturn(pessoa);

        List<EnderecoDTO> enderecoDTOList = enderecoService.postEnderecoPrincipal(1, 2);

        assertNotNull(enderecoDTOList);
    }

    @Test
    public void deveTestarCreateComSucesso() throws RegraDeNegocioException {
        Pessoa pessoa = getPessoa();
        Endereco endereco = getEndereco();

        EnderecoCreateDTO enderecoCreateDTO = getEnderecoCreateDTO();

        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);
        when(pessoaService.findById(anyInt())).thenReturn(pessoa);

        EnderecoDTO enderecoDTO = enderecoService.create(enderecoCreateDTO, 1);

        assertNotNull(enderecoDTO);
        assertEquals(1, enderecoDTO.getIdEndereco().intValue());
        assertEquals("Rua", enderecoDTO.getLogradouro());
        assertEquals("12345678", enderecoDTO.getCep());
        assertEquals("123", enderecoDTO.getNumero());
        assertEquals("São Paulo", enderecoDTO.getCidade());
        assertTrue(enderecoDTO.getPrincipal());
    }

    @Test
    public void deveTestarCreateComMaisDeUmEnderecoComSucesso() throws RegraDeNegocioException {
        Pessoa pessoa = getPessoa();
        Endereco endereco = getEndereco2();
        EnderecoCreateDTO enderecoCreateDTO = getEnderecoCreateDTO();

        List<Endereco> enderecoList = List.of(getEndereco2());
        pessoa.setEnderecoList(enderecoList);

        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);
        when(pessoaService.findById(anyInt())).thenReturn(pessoa);

        EnderecoDTO enderecoDTO = enderecoService.create(enderecoCreateDTO, 1);

        assertNotNull(enderecoDTO);
        assertEquals(2, enderecoDTO.getIdEndereco().intValue());
        assertEquals("Rua", enderecoDTO.getLogradouro());
        assertEquals("12345678", enderecoDTO.getCep());
        assertEquals("123", enderecoDTO.getNumero());
        assertEquals("São Paulo", enderecoDTO.getCidade());
        assertFalse(enderecoDTO.getPrincipal());
    }

    @Test
    public void deveTestarFindById() {
        Endereco endereco = getEndereco();

        when(enderecoRepository.findById(anyInt())).thenReturn(Optional.of(endereco));
    }

    public static Pessoa getPessoa() {
        Pessoa pessoa = new Pessoa();
        List<Endereco> enderecoList = new ArrayList<>();
        pessoa.setIdPessoa(1);
        pessoa.setNome("Cesar");
        pessoa.setDataNascimento(LocalDate.parse("2000-11-11"));
        pessoa.setEnderecoList(enderecoList);

        return pessoa;
    }

    public static Endereco getEndereco() {
        Endereco endereco = new Endereco();

        endereco.setIdEndereco(1);
        endereco.setPrincipal(true);
        endereco.setCep("12345678");
        endereco.setNumero("123");
        endereco.setCidade("São Paulo");
        endereco.setLogradouro("Rua");
        endereco.setPessoaList(getListPessoa());

        return endereco;
    }

    public static EnderecoCreateDTO getEnderecoCreateDTO() {
        EnderecoCreateDTO enderecoCreateDTO = new EnderecoCreateDTO();

        enderecoCreateDTO.setCep("12345678");
        enderecoCreateDTO.setNumero("123");
        enderecoCreateDTO.setCidade("São Paulo");
        enderecoCreateDTO.setLogradouro("Rua");

        return enderecoCreateDTO;
    }

    public static Endereco getEndereco2() {
        Endereco endereco = new Endereco();

        endereco.setIdEndereco(2);
        endereco.setPrincipal(false);
        endereco.setCep("12345678");
        endereco.setNumero("123");
        endereco.setCidade("São Paulo");
        endereco.setLogradouro("Rua");
        endereco.setPessoaList(getListPessoa());

        return endereco;
    }

    public static List<Pessoa> getListPessoa() {
        List<Pessoa> pessoa = new ArrayList<>();

        pessoa.add(getPessoa());

        return pessoa;
    }
}
