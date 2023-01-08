package br.com.attornatus.peopleapi.controller;

import br.com.attornatus.peopleapi.dto.pessoa.PessoaCreateDTO;
import br.com.attornatus.peopleapi.dto.pessoa.PessoaDTO;
import br.com.attornatus.peopleapi.dto.pessoa.PessoaPutDTO;
import br.com.attornatus.peopleapi.service.PessoaService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pessoa")
@Validated
@RequiredArgsConstructor
public class PessoaController {

    private final PessoaService pessoaService;

    @Operation(summary = "Lista todas as pessoas", description = "Lista todas as pessoas presentes no base de dados")
    @GetMapping("/listar-todos")
    public ResponseEntity<List<PessoaDTO>> listAll() {
        return ResponseEntity.ok(pessoaService.listAll());
    }

    @Operation(summary = "Recupera uma pessoa através de seu id", description = "Recupera uma pessoa, presente no banco de dados, através de seu id")
    @GetMapping("/listar-pessoa/{idPessoa}")
    public ResponseEntity<PessoaDTO> listById(@PathVariable("idPessoa") Integer idPessoa) {
        return ResponseEntity.ok(pessoaService.findPersonById(idPessoa));
    }

    @Operation(summary = "Realiza o cadastro de uma pessoa", description = "Realiza o cadastro de uma pessoa, salvando esta na base de dados")
    @PostMapping("/cadastrar")
    public ResponseEntity<PessoaDTO> create (@RequestBody @Valid PessoaCreateDTO pessoaCreateDTO) {
        return ResponseEntity.ok(pessoaService.create(pessoaCreateDTO));
    }

    @Operation(summary = "Atualiza uma pessoa através de seu id", description = "Atualiza uma pessoa, presente no banco de dados, através de seu id")
    @PutMapping("/atualizar-pessoa/{idPessoa}")
    public ResponseEntity<PessoaDTO> update(@PathVariable("idPessoa") Integer idPessoa,
                                            @RequestBody @Valid PessoaPutDTO pessoaCreateDTO) {
        return ResponseEntity.ok(pessoaService.update(pessoaCreateDTO, idPessoa));
    }
}
