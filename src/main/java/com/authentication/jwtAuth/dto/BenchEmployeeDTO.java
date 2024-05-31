package com.authentication.jwtAuth.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BenchEmployeeDTO {
    private Long id;
    private String name;
    private String techStack;
    private double allocationPercentage;




}
