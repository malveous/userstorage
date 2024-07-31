package com.globallogic.userstorage.model.request;

import com.globallogic.userstorage.model.PhoneDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserInput {

    private String name;

    @NotBlank(message = "The email cannot be empty")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", message = "The email must have a valid format")
    private String email;

    @NotBlank(message = "The user password cannot be empty")
    @Size(min = 8, max = 12, message = "The user password must contain between 8 and 12 characters")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "The password must contain at least one upper case, one lower case, one digit and one special character")
    private String password;

    private List<@Valid PhoneDto> phones;

}
