package com.example.expenssManeger.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterDTO {

    private  String type;
    public LocalDate startDate;
    private  String keyword;
    private String sortField; //date,amount,name
    public LocalDate endDate;
    private  String sortOrder; //asc,desc

}
