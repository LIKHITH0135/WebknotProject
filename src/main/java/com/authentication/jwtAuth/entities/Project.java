package com.authentication.jwtAuth.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String projectType; // MSP or SA
    private String sourceClient;
    private String endClient;
    private String projectDescription;

    @ManyToOne
    @JoinColumn(name = "account_manager_id", referencedColumnName = "userID")
    private User accountManager;

    @ManyToOne
    @JoinColumn(name = "project_manager_id", referencedColumnName = "userID")
    private User projectManager;

    @JsonManagedReference
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Employee> employees = new ArrayList<>();

    private String projectStatus; // active or inactive
}
