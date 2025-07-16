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
    private String  fullname;
    private  String  email;
    private  String password;
    private BigDecimal profileImageUrl;
    private Timestamp createdAt;
    private Timestamp updatedAt;



}
