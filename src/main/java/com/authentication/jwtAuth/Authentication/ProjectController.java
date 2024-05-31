package com.authentication.jwtAuth.Authentication; // Correct package statement

import com.authentication.jwtAuth.Service.EmployeeService;
import com.authentication.jwtAuth.Service.ProjectService; // Correct import statement
import com.authentication.jwtAuth.dto.BenchEmployeeDTO;
import com.authentication.jwtAuth.dto.ProjectDTO;
import com.authentication.jwtAuth.dto.ProjectStatusUpdateDTO;
import com.authentication.jwtAuth.entities.Employee;
import com.authentication.jwtAuth.entities.Project;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import com.authentication.jwtAuth.Service.ProjectService;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/create-project")
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project createdProject = projectService.createProject(project);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete-project/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        //return ResponseEntity.noContent().build();
        return new ResponseEntity<>("Project deleted successfully", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/projects/{projectId}/add-employee")
    public ResponseEntity<?> addEmployeeToProject(@PathVariable Long projectId, @RequestBody Employee employee) {
        try {
            Project updatedProject = projectService.addEmployeeToProject(projectId, employee);
            return new ResponseEntity<>(updatedProject, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/projects")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> projects = projectService.getAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/projects/{projectId}/employees")
    public ResponseEntity<List<Employee>> getEmployeesByProjectId(@PathVariable Long projectId) {
        List<Employee> employees = projectService.getEmployeesByProjectId(projectId);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/projects/{projectId}/employees/{employeeId}")
    public ResponseEntity<String> removeEmployeeFromProject(@PathVariable Long projectId, @PathVariable Long employeeId) {
        projectService.removeEmployeeFromProject(projectId, employeeId);
        return new ResponseEntity<>("Employee removed from project successfully", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/projects/{projectId}/update-status")
    public ResponseEntity<Project> updateProjectStatus(@PathVariable Long projectId, @RequestBody ProjectStatusUpdateDTO statusUpdateDTO) {
        Project updatedProject = projectService.updateProjectStatus(projectId, statusUpdateDTO);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/bench-list")
    public ResponseEntity<List<BenchEmployeeDTO>> getBenchList() {
        List<BenchEmployeeDTO> benchList = projectService.getBenchList();
        return new ResponseEntity<>(benchList, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/project/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        try {
            ProjectDTO projectDTO = projectService.getProjectById(id);
            return new ResponseEntity<>(projectDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



}
