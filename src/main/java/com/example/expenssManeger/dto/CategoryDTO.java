package com.example.expenssManeger.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {

    private Long id;
    private String name;
    private String type;
    private String icon;
    private Long profileId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
