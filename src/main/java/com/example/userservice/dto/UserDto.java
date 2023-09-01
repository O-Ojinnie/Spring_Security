package com.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    //Domain 성격의 객체, Application 전반에서 사용될 예정
    //Request Content
    private String email;
    private String name;
    private String pwd;

    private String userId;
    private Date createdAt;
    private String encryptedPwd;


}
