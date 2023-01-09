package br.com.attornatus.peopleapi.controller;

import br.com.attornatus.peopleapi.dto.endereco.EnderecoCreateDTO;
import br.com.attornatus.peopleapi.dto.endereco.EnderecoDTO;
import br.com.attornatus.peopleapi.exceptions.RegraDeNegocioException;
import br.com.attornatus.peopleapi.service.EnderecoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/endereco")
@Validated
@RequiredArgsConstructor
public class EnderecoController {

    private final EnderecoService enderecoService;

    @Operation(summary = "Recupera uma pessoa através de seu id", description = "Recupera uma pessoa, presente no banco de dados, através de seu id")
    @GetMapping("/listar-pessoa")
    public ResponseEntity<List<EnderecoDTO>> listById(@RequestParam Integer idPessoa) throws RegraDeNegocioException {
        return ResponseEntity.ok(enderecoService.listAllById(idPessoa));
    }

    @Operation(summary = "Define um endereço como principal", description = "Define um endereço como principal no banco de dados")
    @PostMapping("/endereco-principal")
    public ResponseEntity<List<EnderecoDTO>> enderecoPrincipal(@RequestParam Integer idPessoa,
                                                     @RequestParam Integer idEndereco) throws RegraDeNegocioException {
        return ResponseEntity.ok(enderecoService.postEnderecoPrincipal(idPessoa, idEndereco));
    }

    @Operation(summary = "Adicina endereço ao cadastro da pessoa", description = "Adiciona endereço ao cadastro da pessoa na base de dados")
    @PostMapping("/adicionar-endereco")
    public ResponseEntity<EnderecoDTO> create(@RequestParam Integer idPessoa,
                                              @RequestBody @Valid EnderecoCreateDTO enderecoCreateDTO) throws RegraDeNegocioException {
        return ResponseEntity.ok(enderecoService.create(enderecoCreateDTO, idPessoa));
    }
}
