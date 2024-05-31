package com.authentication.jwtAuth.dto;

import com.authentication.jwtAuth.entities.Employee;
import com.authentication.jwtAuth.entities.User;
//import com.authentication.jwtAuth.entities.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private Long id;
    private String projectType;
    private String sourceClient;
    private String endClient;
    private String projectDescription;
    private User accountManager;
    private User projectManager;
    private String projectStatus;
    private List<Employee> employees;

}
