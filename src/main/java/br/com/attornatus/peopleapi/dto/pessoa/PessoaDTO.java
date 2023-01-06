package br.com.attornatus.peopleapi.dto.pessoa;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PessoaDTO extends PessoaCreateDTO {

    @Schema(description = "identificador da pessoa", example = "1")
    @NotNull
    private Integer idPessoa;
}
