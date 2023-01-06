package br.com.attornatus.peopleapi.dto.endereco;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoCreateDTO {

    @Schema(description = "Logradouro do endereço", example = "Rua")
    @NotBlank
    @Size(min = 6, max = 100)
    private String logradouro;

    @Schema(description = "CEP do endereço", example = "11111-111")
    @NotBlank
    @Size(min = 8, max = 9)
    private String cep;

    @Schema(description = "Número do endereço", example = "123")
    @NotBlank
    @Size(min = 1, max = 10000)
    private String numero;

    @Schema(description = "Nome da cidade", example = "São Paulo")
    @NotBlank
    @Size(min = 3, max = 50)
    private String cidade;
}
