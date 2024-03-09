package com.inter.report.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inter.report.dto.EmployeeDTO;

@Repository
public interface EmployeeRepo extends JpaRepository<EmployeeDTO, Long> {

}
