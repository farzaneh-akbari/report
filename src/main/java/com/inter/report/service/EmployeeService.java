package com.inter.report.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.inter.report.dto.EmployeeDTO;
import com.inter.report.dto.EmployeeDTOResponse;

import net.sf.jasperreports.engine.JRException;

public interface EmployeeService {

	List<EmployeeDTO> saveAllEmployees(List<EmployeeDTO> employees);

	List<EmployeeDTOResponse> getAllEmployees();

	byte[] generateReport() throws JRException;

	byte[] generateExcelReport() throws JRException;

	CompletableFuture<String> generateReportAsync();

}
