package br.com.ajufood.pedeai.rest.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteRequestDTO {
    @Length(min = 2, max = 128, message = "O nome deverá ter no mínimo 2 caracteres e no máximo 128 caracteres")
    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    @CPF(message = "CPF inválido!")
    @Length(min = 11, max = 11, message = "O cpf deverá ter obrigatoriamente 11 digitos")
    @NotBlank(message = "O CPF é obrigatório.")
    private String cpf;


    @Email(message = "E-mail inválido!")
    @Length(min = 3, max = 256, message = "O email deverá ter no mínimo 3 caracteres e no máximo 256 caracteres")
    @NotBlank(message = "O email é obrigatório.")
    private String email;

    @Length(min = 11, max = 11, message = "O telefone deverá ter obrigatoriamente 11 digitos")
    @NotBlank(message = "O telefone é obrigatório.")
    private String telefone;
}
