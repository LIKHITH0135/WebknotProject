package com.authentication.jwtAuth.repositories;

import com.authentication.jwtAuth.entities.ImportedEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportedEmployeeRepository extends JpaRepository<ImportedEmployee, Long> {


}
