package com.inter.report.service;

import java.util.concurrent.CompletableFuture;

public interface ReportTaskService {

	String createReportTask();

	CompletableFuture<String> getReportFuture(String taskId);

}
