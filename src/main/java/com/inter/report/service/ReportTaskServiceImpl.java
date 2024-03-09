package com.inter.report.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportTaskServiceImpl implements ReportTaskService{
	
	private final Map<String, CompletableFuture<String>> tasks = new ConcurrentHashMap<>();
	
	@Autowired
	private EmployeeService employeeService;

	@Override
	public String createReportTask() {
	       String taskId = UUID.randomUUID().toString();
	        CompletableFuture<String> reportFuture = employeeService.generateReportAsync()
	            .whenComplete((result, ex) -> {
	                if (ex != null) {
	   
	                }
	                // we can remove the task from the map if we need to query it once
	                // tasks.remove(taskId);
	            });
	        tasks.put(taskId, reportFuture);
	        return taskId;
	}
	
	 public CompletableFuture<String> getReportFuture(String taskId) {
	        return tasks.get(taskId);
	    }

}
