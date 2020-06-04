package com.nano.ppmtool.exceptions;

public class ProjectNotFoundExceptionResponse {

	private String projectNoFound;

	public ProjectNotFoundExceptionResponse(String projectNoFound) {
		super();
		this.projectNoFound = projectNoFound;
	}

	public String getProjectNoFound() {
		return projectNoFound;
	}

	public void setProjectNoFound(String projectNoFound) {
		this.projectNoFound = projectNoFound;
	}
	
	
}
