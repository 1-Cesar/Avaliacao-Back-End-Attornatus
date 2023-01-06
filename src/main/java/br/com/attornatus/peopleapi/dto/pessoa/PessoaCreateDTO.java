package br.com.attornatus.peopleapi.dto.pessoa;

import br.com.attornatus.peopleapi.dto.endereco.EnderecoCreateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PessoaCreateDTO {

    @Schema(description = "Nome da pessoa", example = "João")
    @NotBlank
    @Size(min = 3, max = 50)
    String nome;

    @Schema(description = "Data de nascimento da pessoa", example = "10/10/23")
    @NotBlank
    @Past
    Date dataNascimento;

    @Schema(description = "Endereço da pessoa")
    private List<EnderecoCreateDTO> enderecoCreateDTOList;
}
