package com.inter.report.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inter.report.dto.EmployeeDTO;
import com.inter.report.dto.EmployeeDTOResponse;
import com.inter.report.service.EmployeeService;
import com.inter.report.service.ReportTaskService;
import com.inter.report.util.EmployeeControllerPath;

import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping(EmployeeControllerPath.WEB_BASE_URL)
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private ReportTaskService reportTaskService;
	
	@PostMapping(EmployeeControllerPath.CREATE)
	 public ResponseEntity<String> createMultipleEmployees(@Validated @RequestBody List<EmployeeDTO> employees) {        
	        try {
	            List<EmployeeDTO> savedEmployees = employeeService.saveAllEmployees(employees);
	            return ResponseEntity.ok("Successfully saved " + savedEmployees.size() + " employees.");
	        } catch (Exception e) {
	            return ResponseEntity.internalServerError().body("Error saving employees: " + e.getMessage());
	        }
	    }
	
    @GetMapping
    public ResponseEntity<List<EmployeeDTOResponse>> getAllEmployees() {
        List<EmployeeDTOResponse> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping(EmployeeControllerPath.REPORT)
    public ResponseEntity<byte[]> generateEmployeeReport() {
        try {
            byte[] reportContent = employeeService.generateReport();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "employee-report.pdf");
            return new ResponseEntity<>(reportContent, headers, HttpStatus.OK);
        } catch (JRException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping(EmployeeControllerPath.REPORT_EXCEL)
    public ResponseEntity<byte[]> downloadEmployeeReportExcel() {
        try {
            byte[] reportContent = employeeService.generateExcelReport();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("filename", "employees_report.xlsx");

            return new ResponseEntity<>(reportContent, headers, HttpStatus.OK);
        } catch (JRException e) {
        	e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping(EmployeeControllerPath.REPORT_ASYNC)
    public ResponseEntity<?> generateEmployeeReportAsync() {
        String taskId = reportTaskService.createReportTask();
        return ResponseEntity.accepted().body(Map.of("message", "Report generation in progress", "taskId", taskId));
    }
    
    @GetMapping(EmployeeControllerPath.REPORT_TASK_TASKID)
    public ResponseEntity<?> getReportStatus(@PathVariable String taskId) {
    	  CompletableFuture<String> reportFuture = reportTaskService.getReportFuture(taskId);
    	    if (reportFuture == null) {
    	        return ResponseEntity.notFound().build();
    	    }

    	    if (reportFuture.isDone()) {
    	        try {
    	            String reportLocation = reportFuture.get(); 
    	            return ResponseEntity.ok().body(Map.of("status", "completed", "reportLocation", reportLocation));
    	        } catch (Exception e) {
    	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating report: " + e.getMessage());
    	        }
    	    } else {
    	        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of("status", "in_progress"));
    	    }
    }
    

}
