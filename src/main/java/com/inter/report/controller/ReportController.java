package com.inter.report.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inter.report.service.EmployeeService;

import net.sf.jasperreports.engine.JRException;



@RestController
public class ReportController {
	

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employee")
    public ResponseEntity<byte[]> getEmployeeReport() throws JRException, IOException {
        ByteArrayInputStream inputStream = employeeService.generateReport2();
        byte[] reportContent = IOUtils.toByteArray(inputStream);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity<>(reportContent, headers, HttpStatus.OK);
    }
}