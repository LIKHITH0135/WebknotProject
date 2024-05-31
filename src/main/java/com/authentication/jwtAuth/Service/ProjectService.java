package com.authentication.jwtAuth.Service;

import com.authentication.jwtAuth.dto.BenchEmployeeDTO;
import com.authentication.jwtAuth.dto.ProjectDTO;
import com.authentication.jwtAuth.dto.ProjectStatusUpdateDTO;
import com.authentication.jwtAuth.entities.Employee;
import com.authentication.jwtAuth.entities.Project;
import com.authentication.jwtAuth.entities.User;
import com.authentication.jwtAuth.repositories.EmployeeRepository;
import com.authentication.jwtAuth.repositories.ProjectRepository;
import com.authentication.jwtAuth.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    @Autowired
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository, EmployeeService employeeService) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.employeeService = employeeService; // Initialize employeeService with the injected instance
    }
    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));
        return convertTodto(project);
    }

    private ProjectDTO convertTodto(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(project.getId());
        projectDTO.setProjectType(project.getProjectType());
        projectDTO.setSourceClient(project.getSourceClient());
        projectDTO.setEndClient(project.getEndClient());
        projectDTO.setProjectDescription(project.getProjectDescription());
        projectDTO.setAccountManager(project.getAccountManager());
        projectDTO.setProjectManager(project.getProjectManager());
        projectDTO.setProjectStatus(project.getProjectStatus());
        projectDTO.setEmployees(project.getEmployees());
        return projectDTO;
    }

    public Project createProject(Project project) {
        // Fetch and set account manager details
        if (project.getAccountManager() != null) {
            Optional<User> accountManagerOpt = userRepository.findById(project.getAccountManager().getUserID());
            if (accountManagerOpt.isPresent()) {
                project.setAccountManager(accountManagerOpt.get());
            } else {
                throw new RuntimeException("Account manager not found");
            }
        }

        // Fetch and set project manager details
        if (project.getProjectManager() != null) {
            Optional<User> projectManagerOpt = userRepository.findById(project.getProjectManager().getUserID());
            if (projectManagerOpt.isPresent()) {
                project.setProjectManager(projectManagerOpt.get());
            } else {
                throw new RuntimeException("Project manager not found");
            }
        }

        // Save the project (and associated user entities if cascading is configured)
        try {
            project = projectRepository.save(project);
        } catch (Exception e) {
            logger.error("Error saving project:", e);
            logger.error("Project: {}", project);
            throw new RuntimeException("Failed to save project", e);
        }

        logger.info("Project saved successfully: {}", project);
        return project;
    }

    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public void deleteProject(Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            logger.info("Project with ID {} deleted successfully.", id);
        } else {
            throw new RuntimeException("Project not found with ID " + id);
        }

    }

    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(project.getId());
        projectDTO.setProjectType(project.getProjectType());
        projectDTO.setSourceClient(project.getSourceClient());
        projectDTO.setEndClient(project.getEndClient());
        projectDTO.setProjectDescription(project.getProjectDescription());
        projectDTO.setAccountManager(project.getAccountManager());
        projectDTO.setProjectManager(project.getProjectManager());
        projectDTO.setProjectStatus(project.getProjectStatus());
        return projectDTO;
    }

    public Project addEmployeeToProject(Long projectId, Employee employee) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);

        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            if ("inactive".equalsIgnoreCase(project.getProjectStatus())) {
                throw new RuntimeException("Cannot add employee to an inactive project");
            }
            employee.setProject(project); // Set the project reference in the employee
            project.getEmployees().add(employee); // Add employee to the project's employee list
            employeeRepository.save(employee); // Save the employee first to ensure it's persisted
            return projectRepository.save(project); // Save the project with the new employee
        } else {
            throw new RuntimeException("Project not found with id: " + projectId);
        }
    }

    public List<Employee> getEmployeesByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));
        return project.getEmployees();
    }
    @Transactional
    public void removeEmployeeFromProject(Long projectId, Long employeeId) {
        try {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));

            Employee employeeToRemove = project.getEmployees().stream()
                    .filter(employee -> employee.getId().equals(employeeId))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found in project"));

            project.getEmployees().remove(employeeToRemove);
            projectRepository.save(project); // Save changes to the project
            employeeRepository.delete(employeeToRemove); // Delete the employee from the database
        } catch (Exception e) {
            logger.error("Error removing employee from project: {}", e.getMessage());
            throw new RuntimeException("Error removing employee from project", e);
        }
    }

    @Transactional
    public Project updateProjectStatus(Long projectId, ProjectStatusUpdateDTO statusUpdateDTO) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));

        String status = statusUpdateDTO.getStatus();
        project.setProjectStatus(status);

        if ("inactive".equalsIgnoreCase(status)) {
            for (Employee employee : project.getEmployees()) {
                employee.setProject(null);
            }
        }

        return projectRepository.save(project);
    }

    public List<BenchEmployeeDTO> getBenchList() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .filter(employee -> employee.getAllocationPercentage() < 90)
                .map(this::convertToBenchEmployeeDTO)
                .collect(Collectors.toList());
    }

    private BenchEmployeeDTO convertToBenchEmployeeDTO(Employee employee) {
        BenchEmployeeDTO dto = new BenchEmployeeDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setTechStack(employee.getTechStack());
        dto.setAllocationPercentage(employee.getAllocationPercentage());
        return dto;
    }
}
