package com.globallogic.userstorage.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PhoneInput {

    @NotBlank(message = "The phone number cannot be empty")
    @Size(min = 5, max = 10, message = "The phone number must be valid")
    private String number;
    @NotBlank(message = "The city code cannot be empty")
    @Size(min = 1, max = 5, message = "The city code must be valid")
    private String cityCode;
    @NotBlank(message = "The country code cannot be empty")
    @Size(min = 1, max = 5, message = "The country code must be valid")
    private String countryCode;

}
