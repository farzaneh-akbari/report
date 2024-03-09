package com.inter.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTOResponse {
	private Long id;
	private String name;
	private String dateOfBirth;
	private String location;
	private String department;

}
