package com.inter.report.util;

import org.springframework.stereotype.Component;

@Component
public class EmployeeControllerPath {
	
	public static final String WEB_BASE_URL="/api/employees";
	public static final String CREATE="/create";
	public static final String REPORT="/report";
	public static final String REPORT_EXCEL="/report/excel";
	public static final String REPORT_ASYNC="/report/async";
	public static final String REPORT_TASK_TASKID="/reports/tasks/{taskId}";
}
