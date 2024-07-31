package com.globallogic.userstorage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PhoneDto {

    @Positive(message = "The phone number must be valid")
    private long number;
    @NotBlank(message = "The city code cannot be empty")
    @Size(min = 1, max = 5, message = "The city code must be valid")
    private String cityCode;
    @NotBlank(message = "The country code cannot be empty")
    @Size(min = 1, max = 5, message = "The country code must be valid")
    private String countryCode;

}
