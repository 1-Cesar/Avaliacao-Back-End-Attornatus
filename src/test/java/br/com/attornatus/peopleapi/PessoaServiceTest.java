package br.com.attornatus.peopleapi;

import br.com.attornatus.peopleapi.dto.endereco.EnderecoDTO;
import br.com.attornatus.peopleapi.dto.endereco.EnderecoPutDTO;
import br.com.attornatus.peopleapi.dto.pessoa.PessoaCreateDTO;
import br.com.attornatus.peopleapi.dto.pessoa.PessoaDTO;
import br.com.attornatus.peopleapi.dto.pessoa.PessoaPutDTO;
import br.com.attornatus.peopleapi.exceptions.RegraDeNegocioException;
import br.com.attornatus.peopleapi.model.Endereco;
import br.com.attornatus.peopleapi.model.Pessoa;
import br.com.attornatus.peopleapi.repository.EnderecoRepository;
import br.com.attornatus.peopleapi.repository.PessoaRepository;
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
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {PessoaService.class})
@RunWith(MockitoJUnitRunner.class)
public class PessoaServiceTest {

    @InjectMocks
    private PessoaService pessoaService;

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private EnderecoRepository enderecoRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(pessoaService, "objectMapper", objectMapper);
    }

    @Test
    public void deveTestarfindPersonByIdComSucesso() throws RegraDeNegocioException {
        Pessoa pessoa = getPessoa();

        when(pessoaRepository.findById(anyInt())).thenReturn(Optional.of(pessoa));

        PessoaDTO pessoaDTO = pessoaService.findPersonById(anyInt());

        assertNotNull(pessoaDTO);
        assertEquals(1, pessoaDTO.getIdPessoa().intValue());
        assertEquals("Cesar", pessoaDTO.getNome());
        assertEquals(LocalDate.parse("2000-11-11"), pessoaDTO.getDataNascimento());
    }

    @Test
    public void deveTestarlistAllComSucesso() {
        List<Pessoa> pessoa = List.of(getPessoa());

        when(pessoaRepository.findAll()).thenReturn(pessoa);

        List<PessoaDTO> pessoaDTO = pessoaService.listAll();

        assertNotNull(pessoaDTO);
    }

    @Test
    public void deveTestarCreateSemEnderecoComSucesso() throws RegraDeNegocioException {
        Pessoa pessoa = getPessoa();

        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        PessoaDTO pessoaDTO = pessoaService.create(getPessoaSemEnderecoDTO());

        assertNotNull(pessoaDTO);
        assertEquals(1, pessoaDTO.getIdPessoa().intValue());
        assertEquals("Cesar", pessoaDTO.getNome());
        assertEquals(LocalDate.parse("2000-11-11"), pessoaDTO.getDataNascimento());
    }

    @Test
    public void deveTestarCreateComEnderecoComSucesso() throws RegraDeNegocioException {
        Pessoa pessoa = getPessoa();
        Endereco endereco = getEndereco();

        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        PessoaDTO pessoaDTO = pessoaService.create(getPessoaComEnderecoDTO());

        assertNotNull(pessoaDTO);
        assertEquals(1, pessoaDTO.getIdPessoa().intValue());
        assertEquals("Cesar", pessoaDTO.getNome());
        assertEquals(LocalDate.parse("2000-11-11"), pessoaDTO.getDataNascimento());
    }

    @Test()
    public void deveTestarUpdateComEnderecoComSucesso() throws RegraDeNegocioException {
        Pessoa pessoa = getPessoa();
        Endereco endereco = getEndereco();

        when(pessoaRepository.findById(anyInt())).thenReturn(Optional.of(pessoa));
        when(enderecoRepository.findById(anyInt())).thenReturn(Optional.of(endereco));
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);

        PessoaDTO pessoaDTO = pessoaService.update(getPessoaPutDTO(), anyInt());

        assertNotNull(pessoaDTO);
        assertEquals(1, pessoaDTO.getIdPessoa().intValue());
        assertEquals("Cesaaaar", pessoaDTO.getNome());
        assertEquals(LocalDate.parse("2000-11-11"), pessoaDTO.getDataNascimento());
    }

    @Test()
    public void deveTestarUpdateSemEnderecoComSucesso() throws RegraDeNegocioException {
        Pessoa pessoa = getPessoa();
        PessoaPutDTO pessoaPutDTO = getPessoaPutDTO();
        pessoaPutDTO.setEnderecoDTOList(null);

        when(pessoaRepository.findById(anyInt())).thenReturn(Optional.of(pessoa));
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        PessoaDTO pessoaDTO = pessoaService.update(pessoaPutDTO, anyInt());

        assertNotNull(pessoaDTO);
        assertEquals(1, pessoaDTO.getIdPessoa().intValue());
        assertEquals("Cesaaaar", pessoaDTO.getNome());
        assertEquals(LocalDate.parse("2000-11-11"), pessoaDTO.getDataNascimento());
    }

    @Test(expected = RuntimeException.class)
    public void deveTestarUpdateComEnderecoSemSucesso() throws RegraDeNegocioException {
        Pessoa pessoa = new Pessoa();

        when(pessoaRepository.findById(anyInt())).thenReturn(Optional.of(pessoa));

        PessoaDTO pessoaDTO = pessoaService.update(getPessoaPutDTO(), anyInt());

        assertNotNull(pessoaDTO);
    }

    public static PessoaCreateDTO getPessoaSemEnderecoDTO() {
        PessoaCreateDTO pessoaCreateDTO = new PessoaCreateDTO();

        pessoaCreateDTO.setEnderecoDTOList(null);
        pessoaCreateDTO.setNome("Cesar");
        pessoaCreateDTO.setDataNascimento(LocalDate.parse("2000-11-11"));

        return pessoaCreateDTO;
    }

    public static PessoaPutDTO getPessoaPutDTO() {
        PessoaPutDTO pessoaPutDTO = new PessoaPutDTO();

        pessoaPutDTO.setNome("Cesaaaar");
        pessoaPutDTO.setDataNascimento(LocalDate.parse("2000-11-11"));
        pessoaPutDTO.setEnderecoDTOList(getListEnderecoPutDTO());

        return pessoaPutDTO;
    }

    public static PessoaCreateDTO getPessoaComEnderecoDTO() {
        PessoaCreateDTO pessoaCreateDTO = new PessoaCreateDTO();

        pessoaCreateDTO.setNome("Cesar");
        pessoaCreateDTO.setDataNascimento(LocalDate.parse("2000-11-11"));
        pessoaCreateDTO.setEnderecoDTOList(getListEnderecoDTO());

        return pessoaCreateDTO;
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

    public static List<Pessoa> getListPessoa() {
        List<Pessoa> pessoa = new ArrayList<>();

        pessoa.add(getPessoa());

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

    public static EnderecoDTO getEnderecoDTO() {
        EnderecoDTO enderecoDTO = new EnderecoDTO();

        enderecoDTO.setIdEndereco(1);
        enderecoDTO.setPrincipal(true);
        enderecoDTO.setCep("12345678");
        enderecoDTO.setNumero("123");
        enderecoDTO.setCidade("São Paulo");
        enderecoDTO.setLogradouro("Rua");

        return enderecoDTO;
    }

    public static EnderecoPutDTO getEnderecoPutDTO() {
        EnderecoPutDTO enderecoPutDTO = new EnderecoPutDTO();

        enderecoPutDTO.setIdEndereco(1);
        enderecoPutDTO.setPrincipal(true);
        enderecoPutDTO.setCep("12345678");
        enderecoPutDTO.setNumero("123");
        enderecoPutDTO.setCidade("São Paulo");
        enderecoPutDTO.setLogradouro("Rua");

        return enderecoPutDTO;
    }

    public static List<EnderecoDTO> getListEnderecoDTO() {
        List<EnderecoDTO> enderecoDTOList = new ArrayList<>();

        enderecoDTOList.add(getEnderecoDTO());

        return enderecoDTOList;
    }

    public static List<EnderecoPutDTO> getListEnderecoPutDTO() {
        List<EnderecoPutDTO> enderecoPutDTOS = new ArrayList<>();

        enderecoPutDTOS.add(getEnderecoPutDTO());

        return enderecoPutDTOS;
    }
}
