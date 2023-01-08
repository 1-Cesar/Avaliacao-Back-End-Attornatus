package br.com.attornatus.peopleapi.dto.endereco;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoPutDTO extends EnderecoCreateDTO {

    @Schema(description = "Identificador do endereço")
    @NotNull
    private Integer idEndereco;
}
