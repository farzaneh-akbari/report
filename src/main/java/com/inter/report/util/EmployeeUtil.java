package com.inter.report.util;

import java.text.SimpleDateFormat;

import com.inter.report.dto.EmployeeDTO;
import com.inter.report.dto.EmployeeDTOResponse;

public class EmployeeUtil {
	static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	
	public static EmployeeDTOResponse mapEmployeeDTOResponseToEmployeeDTO(EmployeeDTO employeeDTO) {
		
		return EmployeeDTOResponse.builder()
		.id(employeeDTO.getId())
		.name(employeeDTO.getName())
		.dateOfBirth(dateFormat.format(employeeDTO.getDateOfBirth()))
		.department(employeeDTO.getDepartment())
		.location(employeeDTO.getLocation())
		.build();
		
	}
	

}
