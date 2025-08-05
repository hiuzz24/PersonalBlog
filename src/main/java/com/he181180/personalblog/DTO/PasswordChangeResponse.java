package com.he181180.personalblog.DTO;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordChangeResponse {
    private boolean success;
    private String message;
    private String errorCode;
}

