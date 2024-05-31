package com.authentication.jwtAuth.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ImportedEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String techStack;
    private Double yearsOfExperience;
    private Double yearsOfExperienceInWebknot;

    // Constructors, getters, and setters

    public ImportedEmployee() {
    }

    public ImportedEmployee(String name, String email, String techStack, Double yearsOfExperience, Double yearsOfExperienceInWebkno) {
        this.name = name;
        this.email = email;
        this.techStack = techStack;
        this.yearsOfExperience = yearsOfExperience;
        this.yearsOfExperienceInWebknot = yearsOfExperienceInWebkno;
    }


}
