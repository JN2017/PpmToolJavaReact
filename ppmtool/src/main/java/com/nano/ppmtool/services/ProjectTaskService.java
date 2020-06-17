package com.nano.ppmtool.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nano.ppmtool.domain.Backlog;
import com.nano.ppmtool.domain.ProjectTask;
import com.nano.ppmtool.exceptions.ProjectNotFoundException;
import com.nano.ppmtool.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {
	//@Autowired
	//private BacklogRepository backlogRepository;
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	//@Autowired
	//private ProjectRepository projectRepository;
	@Autowired
	private ProjectService projectService;
	
	public ProjectTask addprojectTask(String projectIdentifier, ProjectTask projectTask, String username) {
		//Exceptions when a project is not found 
//		  try {
			//PTs be added to a specific project, project != null => BL exists
			Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();//backlogRepository.findByProjectIdentifier(projectIdentifier);
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
			if (projectTask.getPriority()== null || projectTask.getPriority() == 0 ) { //in the future we need to add test on value 0
			//if (projectTask.getPriority()== null) {
			projectTask.setPriority(3);
			}
			//Initial status when status is null
			if(projectTask.getStatus() =="" || projectTask.getStatus() == null) {
				projectTask.setStatus("TO_DO");
			}
			return projectTaskRepository.save(projectTask);
		
//		} catch (Exception e) {
//			throw new ProjectNotFoundException("Project Not Found!");
//		}
	}
			
	public Iterable<ProjectTask> findBacklogById(String id, String username) {
		
//		Project project = projectRepository.findByProjectIdentifier(id);
//		if (project==null) {
//			throw new ProjectNotFoundException("Project ID: '"+ id + "' does not exist!");
//		}
		// use this method instead for user owner change based on token
		projectService.findProjectByIdentifier(id, username);
		
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(id); 
	}
	
	public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username) {
		//Make sure we are searching on the right backlog
//		Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
//		if (backlog == null) {
//			throw new ProjectNotFoundException("Project ID: '"+ backlog_id + "' does not exist!");
		projectService.findProjectByIdentifier(backlog_id, username);
//		}
		//Make that our task exists
		ProjectTask projectTask = projectTaskRepository.findByprojectSequence(pt_id);
		if(projectTask == null) {
			throw new ProjectNotFoundException("Project Task: '"+ pt_id + "' not found!");
		}
		//Make sure that backlog/Project id in the path corresponds to the right project 
		if (!projectTask.getProjectIdentifier().equals(backlog_id)) {
			throw new ProjectNotFoundException("Project Task: '"+ pt_id + "' does not exist in project:  '"+ backlog_id + "' !");
		}
		return projectTask;
	}
	
	
		//update project task
		//find existing project task
		//replace it with updated task
		//save update
	public ProjectTask updateByProjectSequence(ProjectTask updateTask, String backlog_id, String pt_id, String username) {
		ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id, username);
		projectTask=updateTask;
		
		return projectTaskRepository.save(projectTask);
	}
	
	public void deleteProjectTaskBySequence(String backlog_id, String pt_id, String username) {
		ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id, username);
		
//		Backlog backlog= projectTask.getBacklog();
//		List<ProjectTask> pts = backlog.getProjectTasks() ;
//		pts.remove(projectTask);
//		backlogRepository.save(backlog);
		// Removed due to cascade.refresh from the owning side which is the projectTask
		
		
		projectTaskRepository.delete(projectTask);
	}
}
