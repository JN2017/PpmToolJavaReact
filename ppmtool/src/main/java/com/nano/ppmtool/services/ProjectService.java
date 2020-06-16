package com.nano.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nano.ppmtool.domain.Backlog;
import com.nano.ppmtool.domain.Project;
import com.nano.ppmtool.domain.User;
import com.nano.ppmtool.exceptions.ProjectIdException;
import com.nano.ppmtool.exceptions.ProjectNotFoundException;
import com.nano.ppmtool.repositories.BacklogRepository;
import com.nano.ppmtool.repositories.ProjectRepository;
import com.nano.ppmtool.repositories.UserRepository;

@Service
public class ProjectService {
	
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private BacklogRepository backlogRepository;
	@Autowired
	private UserRepository userRepository;
	
	public Project saveOrUpdateProject(Project project,String username) { 
	
	// Check if the project to update is owned by the token.username
	if (project.getId()!= null) {
		Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
	
	if(existingProject != null &&(!existingProject.getProjectLeader().equals(username))) {
		throw new ProjectNotFoundException("Project not found in your account!");
		//check if database  id exists before updating
	}else if (existingProject == null) {
		throw new ProjectNotFoundException("Project with ID: '"+ project.getProjectIdentifier()+"' cannot be update because it doesn't exist!");
		}
	}
	// create a new project
		try {
			User user = userRepository.findByUsername(username); 
			project.setUser(user);
			project.setProjectLeader(user.getUsername());
			project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			
			
			project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			
			if (project.getId() == null) {
				Backlog backlog = new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			}
			if (project.getId() != null) {
				project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
			}
			
			return projectRepository.save(project);
		}catch(Exception e) {
			throw new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase()+"' already exists!!");
		}	
	}
	
	public Project findProjectByIdentifier(String projectId, String username) {
		
		Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		if (project == null) {
			throw new ProjectIdException("Project ID '" + projectId +"' do not exist!");
		}
		if(!project.getProjectLeader().equals(username)) {
			throw new ProjectNotFoundException("Project not found in your account!");
		} 		
		return project;
	}
	
	public Iterable<Project> findAllprojects(String username){
		//return projectRepository.findAll();
		return projectRepository.findAllByProjectLeader(username);
	}
	
	public void deleteProjectByIdentifier(String projectId, String username) {
//		Project project= projectRepository.findByProjectIdentifier(projectId.toUpperCase());
//		if (project == null) {
//			throw new ProjectIdException("Cannot delete project with ID: '" + projectId +"' , this project doesn't exist!");
//		}
		
		
		projectRepository.delete(findProjectByIdentifier(projectId, username));
	}
}
