package com.nano.ppmtool.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nano.ppmtool.domain.Backlog;
import com.nano.ppmtool.domain.Project;
import com.nano.ppmtool.domain.ProjectTask;
import com.nano.ppmtool.exceptions.ProjectNotFoundException;
import com.nano.ppmtool.repositories.BacklogRepository;
import com.nano.ppmtool.repositories.ProjectRepository;
import com.nano.ppmtool.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {
	@Autowired
	private BacklogRepository backlogRepository;
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	@Autowired
	private ProjectRepository projectRepository;
	
	public ProjectTask addprojectTask(String projectIdentifier, ProjectTask projectTask) {
		//Exceptions when a project is not found 
		try {
			//PTs be added to a specific project, project != null => BL exists
			Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
			//set BL to PT
			projectTask.setBacklog(backlog);
			//We want our project sequence to be like this: IDPRO-1 , IDPRO-2 etc...IDPRO-2000
			Integer BacklogSequence = backlog.getPTSequence();
			//Update the BL sequence
			BacklogSequence++;
			backlog.setPTSequence(BacklogSequence);
			//Add sequence to PT
			projectTask.setProjectSequence(projectIdentifier+ "-" + BacklogSequence);
			projectTask.setProjectIdentifier(projectIdentifier);
			//INITIAL priority when priority is null
//			if (projectTask.getPriority() == 0 || projectTask.getPriority()== null) { //in the future we need to add test on value 0
			if (projectTask.getPriority()== null) {
			projectTask.setPriority(3);
			}
			//Initial status when status is null
			if(projectTask.getStatus() =="" || projectTask.getStatus() == null) {
				projectTask.setStatus("TO_DO");
			}
			return projectTaskRepository.save(projectTask);
		
		} catch (Exception e) {
			throw new ProjectNotFoundException("Project Not Found!");
		}
	}
			
	public Iterable<ProjectTask> findBacklogById(String id) {
		
		Project project = projectRepository.findByProjectIdentifier(id);
		if (project==null) {
			throw new ProjectNotFoundException("Project ID: '"+ id + "' does not exist!");
		}
		
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
		 
	}
}
