package br.com.attornatus.peopleapi.dto.pessoa;

import br.com.attornatus.peopleapi.dto.endereco.EnderecoDTO;
import br.com.attornatus.peopleapi.dto.endereco.EnderecoPutDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PessoaPutDTO {

    @Schema(description = "Nome da pessoa", example = "João")
    @NotBlank
    @Size(min = 3, max = 50)
    String nome;

    @Schema(description = "Data de nascimento da pessoa", example = "10/10/23")
    @NotNull
    @Past
    LocalDate dataNascimento;

    @Schema(description = "Endereço da pessoa")
    private List<EnderecoPutDTO> enderecoDTOList;
}
