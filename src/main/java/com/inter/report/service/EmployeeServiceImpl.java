package com.inter.report.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.inter.report.dto.EmployeeDTO;
import com.inter.report.dto.EmployeeDTOResponse;
import com.inter.report.exception.NoEmployeesFoundException;
import com.inter.report.repository.EmployeeRepo;
import com.inter.report.util.EmployeeUtil;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;


@Service
public class EmployeeServiceImpl implements EmployeeService{
	
	@Autowired
	private EmployeeRepo employeeRepo;

	@Override
	public List<EmployeeDTO> saveAllEmployees(List<EmployeeDTO> employees) {
		return employeeRepo.saveAll(employees);
	}

	@Override
	public List<EmployeeDTOResponse> getAllEmployees() {
        List<EmployeeDTO> employees = employeeRepo.findAll();
        if (employees.isEmpty()) {
            throw new NoEmployeesFoundException("No employees found in the database.");
        }
        return employees.stream().map(EmployeeUtil::mapEmployeeDTOResponseToEmployeeDTO).collect(Collectors.toList());
	}

	@Override
	public byte[] generateReport() throws JRException{
		 InputStream reportStream = getClass().getResourceAsStream("/employeeReport.jrxml");
	        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
	        List<EmployeeDTOResponse> employees = getAllEmployees();
	        Collections.sort(employees, Comparator.comparing(EmployeeDTOResponse::getDepartment).thenComparing(EmployeeDTOResponse::getName));
	        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);
	        return JasperExportManager.exportReportToPdf(jasperPrint);

	}

	@Override
	public byte[] generateExcelReport() throws JRException{
		  JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream("/employeeReportExcel.jrxml"));

		  	List<EmployeeDTOResponse> employees = getAllEmployees();
		  	Collections.sort(employees, Comparator.comparing(EmployeeDTOResponse::getDepartment).thenComparing(EmployeeDTOResponse::getName));
		    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);


	        // Fill the report
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);

	        // Configure the exporter
	        JRXlsxExporter exporter = new JRXlsxExporter();
	        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

	        ByteArrayOutputStream os = new ByteArrayOutputStream();
	        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));

	        SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
	        config.setOnePagePerSheet(true); // Each group in its own sheet
	        config.setWhitePageBackground(false); // Optional: improves readability in Excel
	        config.setRemoveEmptySpaceBetweenRows(true); 
	        exporter.setConfiguration(config);
	 
	        exporter.exportReport();

	        return os.toByteArray();
	        

	}

	@Async

	public CompletableFuture<String> generateReportAsync() {
		   try {
	            JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream("/employeeReport.jrxml"));

	         	List<EmployeeDTOResponse> employees = getAllEmployees();
			  	Collections.sort(employees, Comparator.comparing(EmployeeDTOResponse::getDepartment).thenComparing(EmployeeDTOResponse::getName));
		        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);
	            JasperPrint print = JasperFillManager.fillReport(report, new HashMap<>(), dataSource);

	            // Save the report to a temporary file
	            File tempFile = File.createTempFile("employee_report_", ".pdf");
	            JasperExportManager.exportReportToPdfFile(print, tempFile.getAbsolutePath());

	            // Return the path to the generated report
	            return CompletableFuture.completedFuture(tempFile.getAbsolutePath());
	        } catch (Exception e) {
	            e.printStackTrace();
	            // Optionally, return a CompletableFuture completed exceptionally
	            CompletableFuture<String> future = new CompletableFuture<>();
	            future.completeExceptionally(e);
	            return future;
	        }
	}



}
