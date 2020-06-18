package it.uniroma3.siw.taskmanager.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.taskmanager.controller.session.SessionData;
import it.uniroma3.siw.taskmanager.model.Credentials;
import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.Tag;
import it.uniroma3.siw.taskmanager.model.Task;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.service.CredentialsService;
import it.uniroma3.siw.taskmanager.service.ProjectService;
import it.uniroma3.siw.taskmanager.service.TagService;
import it.uniroma3.siw.taskmanager.service.TaskService;
import it.uniroma3.siw.taskmanager.service.UserService;

@Controller
public class ProjectController {

	@Autowired
	SessionData sessionData;
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	ProjectValidator projectValidator;
	
	@Autowired
	UserService userService;
	
	@Autowired
	CredentialsService credentialsService;
	
	@Autowired
	TagValidator tagValidator;
	
	@Autowired
	TagService tagService;
	
	@Autowired
	TaskService taskService;
	
	@RequestMapping(value = {"/projects"}, method = RequestMethod.GET)
	public String myOwnedProjects(Model model) {
		User loggedUser = this.sessionData.getLoggedUser();
		List<Project> projects = this.projectService.getMyProjects(loggedUser);
		model.addAttribute("loggedUser",loggedUser);
		model.addAttribute("projects",projects);
		return "myOwnedProjects";
	}
	
	@RequestMapping(value = {"/projects/add"}, method = RequestMethod.GET)
	public String createProjectForm(Model model) {
		User loggedUser= this.sessionData.getLoggedUser();
		model.addAttribute("loggedUser",loggedUser);
		model.addAttribute("projectForm",new Project());
		return "addProject";
	}
	
	@RequestMapping(value = {"/projects/add"}, method = RequestMethod.POST)
	public String createProject(@Valid @ModelAttribute("projectForm") Project project, BindingResult projectBindingResult, Model model) {
		User loggedUser = this.sessionData.getLoggedUser();
		
		this.projectValidator.validate(project,projectBindingResult);
		
		if(!projectBindingResult.hasErrors()) {
			project.setOwner(loggedUser);
			this.projectService.saveProject(project);
			return "redirect:/projects/" + project.getId(); 
		}
		return "addProject";
	}
	
	@RequestMapping( value = {"/projects/{projectId}"}, method = RequestMethod.GET)
	public String project(Model model, @PathVariable Long projectId) {
		User loggedUser = this.sessionData.getLoggedUser();
		
		//se non esiste un progetto con quell'id ritorna alla lista dei progetti
		Project project = this.projectService.getProject(projectId);
		if(project == null)
			return "redirect:/projects";
		
		//se non ho il permesso di vedere quel progetto, ritorna alla lista dei progetti
		//faccio così perche quando faccio il retrieve del progetto non ho anche i members
		List<User> members = this.userService.getMembers(project);
		if(!project.getOwner().equals(loggedUser) && !members.contains(loggedUser))
			return "redirect:/projects";
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("project", project);
		model.addAttribute("members",members);
		return "project";
	}
	
	@RequestMapping( value = {"/projects/shared"}, method = RequestMethod.GET)
	public String sharedProjects(Model model) {
		
		User loggedUser =this.sessionData.getLoggedUser();
		List<Project> visibleProjects = this.projectService.getAllVisibleProjects(loggedUser);
		model.addAttribute("loggedUser",loggedUser);
		model.addAttribute("visibleProjects",visibleProjects);
		return "myVisibleProjects";
	}
	
	@RequestMapping(value= {"/projects/{projectId}/edit"}, method = RequestMethod.GET)
	public String formEdit(@PathVariable Long projectId, Model model) {
		
		User loggedUser = this.sessionData.getLoggedUser();
		Credentials loggedCredentials = this.sessionData.getLoggedCredentials();
		
		Project project = this.projectService.getProject(projectId);
		//se non esiste un progetto con quell'id
		if(project == null )
			return "redirect:/projects";
		
		//se non ho l'autorizzazione per modificare l'id (non sono owner ne admin)
		if(!loggedUser.equals(project.getOwner()) && !loggedCredentials.getRole().equals(Credentials.ADMIN_ROLE))
			return "redirect:/projects";
		
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectForm",new Project());
		model.addAttribute("projectId", projectId);
		return "editProject";
	}
	
	@RequestMapping(value = {"/projects/{projectId}/edit"}, method = RequestMethod.POST)
	public String editProject(@PathVariable Long projectId, @Valid @ModelAttribute("projectForm") Project project, BindingResult projectBindingResult, Model model) {
			
		this.projectValidator.validate(project, projectBindingResult);
		
		if(!projectBindingResult.hasErrors()) {
			Project currentProject = this.projectService.getProject(projectId);
			currentProject.setName(project.getName());
			this.projectService.saveProject(currentProject);
			return "updateProjectSuccessful";
		}
		return "editProject";
	}
	
	@RequestMapping(value = {"/projects/{projectId}/delete"}, method = RequestMethod.GET)
	public String formDelete(@PathVariable Long projectId, Model model) {
		User loggedUser = this.sessionData.getLoggedUser();
		Credentials loggedCredentials = this.sessionData.getLoggedCredentials();
		Project project = this.projectService.getProject(projectId);
		
		//se non esiste un progetto con quell'id
		if(project == null )
			return "redirect:/projects";
		
		//se non ho l'autorizzazione per modificare l'id (non sono owner ne admin)
		if(!loggedUser.equals(project.getOwner()) && !loggedCredentials.getRole().equals(Credentials.ADMIN_ROLE))
			return "redirect:/projects";
		
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("project",project);
		model.addAttribute("projectId", projectId);
		return "deleteProject";
	}
	
	@RequestMapping(value = {"/projects/{projectId}/delete"}, method = RequestMethod.POST)
	public String delete(@PathVariable Long projectId, Model model) {
		Project project = this.projectService.getProject(projectId);
		this.projectService.deleteProject(project);
		return "redirect:/projects";
	}
	
	@RequestMapping(value = {"/projects/{projectId}/share"}, method = RequestMethod.GET)
	public String shareForm(@PathVariable Long projectId, Model model) {
		Project project = this.projectService.getProject(projectId);
		User loggedUser = this.sessionData.getLoggedUser();
		Credentials loggedCredentials = this.sessionData.getLoggedCredentials();
		
		if(project == null )
			return "redirect:/projects";
		
		//se non ho l'autorizzazione per condividere un progetto(non sono owner ne admin)
		if(!loggedUser.equals(project.getOwner()) && !loggedCredentials.getRole().equals(Credentials.ADMIN_ROLE))
			return "redirect:/projects";
		
		model.addAttribute("projectId",projectId);
		model.addAttribute("user",new Credentials());
		return "shareProject";
	}
	
	@RequestMapping(value = {"/projects/{projectId}/share"}, method = RequestMethod.POST)
	public String shareProject(@PathVariable Long projectId, @Valid @ModelAttribute("user") Credentials credentials, Model model) {
		
		Credentials credentialsUser = this.credentialsService.getCredentialsByUserName(credentials.getUserName());
		User user = credentialsUser.getUser();
		Project project = this.projectService.getProject(projectId);
		this.projectService.shareProjectWithUser(user, project);
		return "redirect:/projects";
	}
	
	@RequestMapping(value = {"/projects/{projectId}/tags/add"}, method = RequestMethod.GET)
	public String formAddTag(@PathVariable Long projectId, Model model) {
		Project project = this.projectService.getProject(projectId);
		User loggedUser = this.sessionData.getLoggedUser();
		
		if(project == null )
			return "redirect:/projects";
		
		//se non ho l'autorizzazione per condividere un progetto(non sono owner)
		if(!loggedUser.equals(project.getOwner()))
			return "redirect:/projects/" + projectId;
		
		model.addAttribute("tagForm",new Tag());
		model.addAttribute("projectId",projectId);
		return "addTag";
	}
	
	@RequestMapping(value= {"/projects/{projectId}/tags/add"}, method = RequestMethod.POST)
	public String addTag(@PathVariable Long projectId, @Valid @ModelAttribute("tagForm") Tag tag,
											BindingResult tagBindingResult, Model model) {
		
		this.tagValidator.validate(tag, tagBindingResult);
		
		if(!tagBindingResult.hasErrors()) {
			Project project = this.projectService.getProject(projectId);
			this.projectService.addTag(project, tag);
			return "redirect:/projects/" + projectId;
		}
		return "addTag";
	}
	
	@GetMapping(value= {"/projects/{projectId}/tags/delete"})
	public String formDeleteTag(@PathVariable Long projectId, Model model) {
		User loggedUser = this.sessionData.getLoggedUser();
		Project project = this.projectService.getProject(projectId);
		
		if(project == null )
			return "redirect:/projects";
		
		if(loggedUser.equals(project.getOwner())) {
			model.addAttribute("projecId",projectId);
			model.addAttribute("project",project);
			model.addAttribute("loggedUser", loggedUser);
			return "deleteTagProject";
		}
		return "redirect:/projects/" + projectId;
	}
	
	@PostMapping(value = {"/projects/{projectId}/tags/{tagId}/delete"})
	public String deleteTag(@PathVariable Long projectId, @PathVariable Long tagId,
												Model model) {
		
		Tag tag = this.tagService.getTag(tagId);
		Project project = this.projectService.getProject(projectId);
		List<Task> tasks = project.getTasks();
		for(Task t: tasks) {
			this.taskService.deleteTagToTask(tag, t);
		}
		this.projectService.deleteTagToProject(tag,project);
		return "redirect:/projects/" + projectId;
	}
}
