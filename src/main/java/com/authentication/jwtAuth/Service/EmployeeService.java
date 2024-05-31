
package com.authentication.jwtAuth.Service;

import com.authentication.jwtAuth.entities.Employee;
import com.authentication.jwtAuth.entities.Project;
import com.authentication.jwtAuth.repositories.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void assignProjectToEmployee(Employee employee, Project project) {
        // Set the project for the employee
        employee.setProject(project);

        // Save the employee (assuming cascade is configured for project in Employee entity)
        try {
            employeeRepository.save(employee);
        } catch (Exception e) {
            logger.error("Error assigning project to employee:", e);
            logger.error("Employee: {}", employee);
            throw new RuntimeException("Failed to assign project to employee", e);
        }

        logger.info("Project assigned to employee successfully: {}", employee);
    }


    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    // You can add more methods here as needed for other employee operations
}
