package com.example.userservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUser {
    //Form에서 데이터를 받기위한 용도, 객체 분리해서 사용 주의
    @NotNull(message = "Email cannot be null")
    @Size(min =2, message = "Email not be less than two characters")
    @Email
    private String email;

    @NotNull(message = "Name cannot be null")
    @Size(min =2, message = "Name not be less than two characters")
    private String name;
    private String pwd;
}
