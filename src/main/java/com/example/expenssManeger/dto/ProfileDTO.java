package com.example.expenssManeger.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDTO
{


    private  Long id;
    private  String  fullName;
    private  String  email;
    private  String password;
    private  String profileImageUrl;
    private Timestamp createdAt;
    private Timestamp updatedAt;



}
