package com.globallogic.userstorage.exceptions.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorResponse {

    private Instant timeStamp;
    private int code;
    private String detail;

    public ErrorResponse(int code, String detail) {
        this.timeStamp = Instant.now();
        this.code = code;
        this.detail = detail;
    }

}
