package br.com.attornatus.peopleapi.dto.pessoa;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PessoaDTO extends PessoaCreateDTO {

    @Schema(description = "identificador da pessoa")
    @NotNull
    private Integer idPessoa;
}
