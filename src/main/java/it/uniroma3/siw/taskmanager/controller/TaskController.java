package it.uniroma3.siw.taskmanager.controller;

import java.util.ArrayList;
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
import it.uniroma3.siw.taskmanager.model.Comment;
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
@RequestMapping(value= {"/projects/{projectId}/tasks"})
public class TaskController {

	@Autowired
	SessionData sessionData;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	TagService tagService;
	
	@Autowired
	TaskValidator taskValidator;
	
	@Autowired
	UserService userService;
	
	@Autowired
	CredentialsService credentialsService;
	
	@Autowired
	CommentValidator commentValidator;
	
	@RequestMapping(value= {"/add"}, method = RequestMethod.GET)
	public String taskForm(@PathVariable Long projectId, Model model) {
		User loggedUser = this.sessionData.getLoggedUser();
		Project project = this.projectService.getProject(projectId);
		
		if(project==null)
			return "redirect:/projects";
		if(!loggedUser.equals(project.getOwner()))
			return "redirect:/projects/" + projectId;
		
		model.addAttribute("projectId",projectId);
		model.addAttribute("taskForm",new Task());
		return "addTask";
	}
	
	@RequestMapping(value= {"/add"}, method = RequestMethod.POST)
	public String addTask(@PathVariable Long projectId, @Valid @ModelAttribute("taskForm")Task task,
							BindingResult taskBindingResult,Model model) {
		
		this.taskValidator.validate(task, taskBindingResult);
		
		if(!taskBindingResult.hasErrors()) {
			Project project = this.projectService.getProject(projectId);
			this.projectService.addTask(project, task);
			return "redirect:/projects/" + projectId;
		}
		return "addTask";
	}
	
	@RequestMapping(value = {"/{taskId}"}, method = RequestMethod.GET)
	public String task(@PathVariable Long projectId, @PathVariable Long taskId, Model model) {
		
		User loggedUser = this.sessionData.getLoggedUser();
		Project project = this.projectService.getProject(projectId);
		
		if(project==null)
			return "redirect:/projects";
		List<User> members = this.userService.getMembers(project);
		if(!loggedUser.equals(project.getOwner()) && !members.contains(loggedUser))
			return "redirect:/projects";
		
		Task task = this.taskService.getTask(taskId);
		model.addAttribute("task", task);
		model.addAttribute("projectId", projectId);
		model.addAttribute("project",project);
		model.addAttribute("loggedUser", loggedUser);
		return "task";
	}
	
	@RequestMapping(value= {"/{taskId}/edit"}, method = RequestMethod.GET)
	public String editForm(@PathVariable Long projectId, 
							@PathVariable Long taskId, Model model) {
		User loggedUser = this.sessionData.getLoggedUser();
		Project project = this.projectService.getProject(projectId);
		Task task = this.taskService.getTask(taskId);
		
		if(project==null)
			return "redirect:/projects"; 
		
		if(task==null)
			return "redirect:/projects" + projectId +"/tasks";
		
		if(!loggedUser.equals(project.getOwner()))
			return "redirect:/projects";
		
		model.addAttribute("projectId", projectId);
		model.addAttribute("taskForm", new Task());
		model.addAttribute("taskId",taskId);
		model.addAttribute("loggedUser",loggedUser);
		return "editTask";
	}
	
	@RequestMapping(value= {"/{taskId}/edit"}, method = RequestMethod.POST)
	public String editTask(@Valid @ModelAttribute("taskForm") Task task, BindingResult taskBindingResult,
							@PathVariable Long projectId, @PathVariable Long taskId, Model model) {
		
		this.taskValidator.validate(task, taskBindingResult);
		if(!taskBindingResult.hasErrors()) {
			Task currentTask = this.taskService.getTask(taskId);
			currentTask.setName(task.getName());
			currentTask.setDescription(task.getDescription());
			Project currentProject = this.projectService.getProject(projectId);
			this.projectService.addTask(currentProject, currentTask);
			return "redirect:/projects/" +projectId + "/tasks/" + taskId;
		}
		return "editTask";
	}
	
	@RequestMapping(value= {"/{taskId}/delete"}, method = RequestMethod.GET)
	public String formDelete(@PathVariable Long projectId, @PathVariable Long taskId, Model model) {
		User loggedUser = this.sessionData.getLoggedUser();
		Project project = this.projectService.getProject(projectId);
		Task task = this.taskService.getTask(taskId);
		//se non esiste un progetto con quell'id
		if(project == null )
			return "redirect:/projects";
		//se non esiste un progetto con quell'id
		if(task == null )
			return "redirect:/projects/" + projectId + "/tasks";
		
		//se non ho l'autorizzazione per modificare l'id (non sono owner ne admin)
		if(!loggedUser.equals(project.getOwner()))
			return "redirect:/projects/{projectId}/tasks";
		
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectId", projectId);
		model.addAttribute("taskId",taskId);
		return "deleteTask";
	}
	
	@RequestMapping(value= {"/{taskId}/delete"}, method = RequestMethod.POST)
	public String delete(@PathVariable Long projectId, @PathVariable Long taskId, Model model) {
		
		Task task = this.taskService.getTask(taskId);
		this.taskService.deleteTask(task);
		
		return "redirect:/projects/" + projectId;
	}
	
	@RequestMapping(value= {"/{taskId}/share"}, method = RequestMethod.GET)
	public String shareForm(@PathVariable Long projectId, @PathVariable Long taskId, Model model) {
		User loggedUser = this.sessionData.getLoggedUser();
		Project project = this.projectService.getProject(projectId);
		Task task = this.taskService.getTask(taskId);
		
		//se non esiste un progetto con quell'id
		if(project == null )
			return "redirect:/projects";
		//se non esiste un task con quell'id
		if(task == null )
			return "redirect:/projects/" +projectId + "/tasks";
		
		//se non ho l'autorizzazione per modificare il task (non sono owner ne admin)
		if(!loggedUser.equals(project.getOwner()))
			return "redirect:/projects/" +projectId + "/tasks";
		
		model.addAttribute("user", new Credentials());
		model.addAttribute("projectId", projectId);
		model.addAttribute("taskId",taskId);
		
		return "shareTask";
	}
	
	@RequestMapping(value= {"/{taskId}/share"}, method = RequestMethod.POST)
	public String shareTask(@PathVariable Long projectId, @PathVariable Long taskId, 
							@Valid @ModelAttribute("user") Credentials credentials, Model model) {
		Credentials currentCredentials = this.credentialsService.getCredentialsByUserName(credentials.getUserName());
		User user = currentCredentials.getUser();
		Project project = this.projectService.getProject(projectId);
		List<User> members = this.userService.getMembers(project);
		if(members.contains(user)) {
			Task task = this.taskService.getTask(taskId);
			this.taskService.addTaskToUser(user, task);
		}
		
		return "redirect:/projects/" +projectId + "/tasks/" + taskId;
	}
	
	@RequestMapping(value= {"/{taskId}/addTag"}, method = RequestMethod.GET)
	public String addTagForm(@PathVariable Long projectId, @PathVariable Long taskId, Model model) {
		User loggedUser = this.sessionData.getLoggedUser();
		Project project = this.projectService.getProject(projectId);
		Task task = this.taskService.getTask(taskId);
		
		//se non esiste un progetto con quell'id
		if(project == null )
			return "redirect:/projects";
		//se non esiste un task con quell'id
		if(task == null )
			return "redirect:/projects/" +projectId + "/tasks";
		//se non ho l'autorizzazione per modificare il task (non sono owner ne admin)
		if(!loggedUser.equals(project.getOwner()))
			return "redirect:/projects/" +projectId + "/tasks";
		
		List<Tag> allProjectTags= project.getTags();
		
		List<Tag> result = new ArrayList<>();
		for(Tag t: allProjectTags) {
			if(!task.getTags().contains(t))
				result.add(t);
		}
		
		model.addAttribute("loggedUser",loggedUser);
		model.addAttribute("project", project);
		model.addAttribute("task",task);
		model.addAttribute("projectId", projectId);
		model.addAttribute("taskId",taskId);
		model.addAttribute("validTags",result);
		return "pickTag";
	}
	
	@RequestMapping(value= {"/{taskId}/addTag/{tagId}"}, method = RequestMethod.POST)
	public String addTag(@PathVariable Long projectId, @PathVariable Long taskId, @PathVariable Long tagId, Model model){
		Task task = this.taskService.getTask(taskId);
		Tag tag = this.tagService.getTag(tagId);
	    if(!task.getTags().contains(tag)) {
            this.taskService.addTagToTask(tag, task);
        }
		return "redirect:/projects/" +projectId + "/tasks/" + taskId;
	}
	
	@RequestMapping(value= {"/{taskId}/comment/add"}, method = RequestMethod.GET)
	public String commentForm(@PathVariable Long projectId, @PathVariable Long taskId, Model model) {
		Task task = this.taskService.getTask(taskId);
		Project project = this.projectService.getProject(projectId);
		User loggedUser = this.sessionData.getLoggedUser();
		List<User> members = this.userService.getMembers(project);
		if(members.contains(loggedUser)) {
			model.addAttribute("commentForm", new Comment());
			model.addAttribute("taskId",taskId);
			model.addAttribute("projectId",projectId);
			model.addAttribute("task",task);
			return "addComment";
		}
		return "redirect:/projects/" +projectId + "/tasks/" + taskId;
	}
	
	@RequestMapping(value= {"/{taskId}/comment/add"}, method = RequestMethod.POST)
	public String addComment(@PathVariable Long projectId, @PathVariable Long taskId, 
							@Valid @ModelAttribute("commentForm")Comment comment,
							BindingResult commentBindingResult,Model model) {
		this.commentValidator.validate(comment, commentBindingResult);
		if(!commentBindingResult.hasErrors()) {
			Task task = this.taskService.getTask(taskId);
			comment.setUser(this.sessionData.getLoggedUser());
			this.taskService.addCommentToTask(comment, task);
			return "redirect:/projects/" + projectId + "/tasks/" + taskId;
		}
		return "addComment";
	}
	
	@GetMapping(value= {"/{taskId}/deleteTag"})
	public String showDeleteTag(@PathVariable Long projectId, @PathVariable Long taskId,
															Model model) {
		User loggedUser = this.sessionData.getLoggedUser();
		Project project = this.projectService.getProject(projectId);
		Task task = this.taskService.getTask(taskId);
		
		//se non esiste un progetto con quell'id
		if(project == null )
			return "redirect:/projects";
		//se non esiste un task con quell'id
		if(task == null )
			return "redirect:/projects/" +projectId + "/tasks";
		//se non ho l'autorizzazione per modificare il task (non sono owner ne admin)
		if(!loggedUser.equals(project.getOwner()))
			return "redirect:/projects/" +projectId + "/tasks";
		
		model.addAttribute("projectId",projectId);
		model.addAttribute("project",project);
		model.addAttribute("task",task);
		model.addAttribute("taskId",taskId);
		model.addAttribute("loggedUser",loggedUser);
		return "deleteTagTask";
	}
	
	@PostMapping(value= {"/{taskId}/deleteTag/{tagId}"})
	public String deleteTag(@PathVariable Long projectId, @PathVariable Long taskId,
							@PathVariable Long tagId, Model model) {
		
		Tag tag = this.tagService.getTag(tagId);
		Task task  = this.taskService.getTask(taskId);
		this.taskService.deleteTagToTask(tag, task);
		return "redirect:/projects/" +projectId + "/tasks/" + taskId;
	}
}
