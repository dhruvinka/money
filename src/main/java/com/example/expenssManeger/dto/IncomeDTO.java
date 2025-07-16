package com.example.expenssManeger.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncomeDTO {


    private Long id;
    private String name;
    private String icon;
    private BigDecimal amount;
    private LocalDate date;
    private String categoryName;
    private Long categoryId;
    private String createdAt;
    private String updatedAt;



}
