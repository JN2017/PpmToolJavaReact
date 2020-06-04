package com.nano.ppmtool.web;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nano.ppmtool.domain.ProjectTask;
import com.nano.ppmtool.services.MapValidationErrorService;
import com.nano.ppmtool.services.ProjectTaskService;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	@Autowired
	private ProjectTaskService projectTaskService;
	@PostMapping("/{backlog_id}")
	public ResponseEntity<?> addPTtoBacklog(@Valid @RequestBody ProjectTask projectTask, 
											BindingResult result, @PathVariable String backlog_id ){
		
		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
		if (errorMap != null) return errorMap;
		ProjectTask projectTask1 = projectTaskService.addprojectTask(backlog_id, projectTask);
		
		return new ResponseEntity<ProjectTask> (projectTask1, HttpStatus.CREATED);
	}
	
	
	@GetMapping("/{backlog_id}")
	Iterable<ProjectTask> getProjectBacklog(@PathVariable String backlog_id){
		
		return projectTaskService.findBacklogById(backlog_id);
	}
	
	
}
