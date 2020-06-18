package it.uniroma3.siw.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.uniroma3.siw.taskmanager.controller.session.SessionData;
import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.service.CredentialsService;
import it.uniroma3.siw.taskmanager.service.ProjectService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	    @Autowired
	    private CredentialsService credentialsService;
	    @Autowired
	    private ProjectService projectService;
	    @Autowired
	    private SessionData sessionData;
	    
		@GetMapping()
		public String admin(Model model) {
			User loggedAdmin = this.sessionData.getLoggedUser();
			model.addAttribute("admin", loggedAdmin);
			return "admin";  //restituisce la vista
		}
	    
	    @GetMapping(value = "/users")
	    public String showAllUsersList(Model model) {
	        model.addAttribute("allUsersCredentials", this.credentialsService.getAllCredentials());
	        return "allUsers";
	    }
	    @PostMapping(value = "/users/{userName}/delete")
	    public String deleteUser(@PathVariable String userName,
	                             Model model) {
	        this.credentialsService.deleteCredentials(userName);
	        return "redirect:/admin/users";
	    }
	    
	    @GetMapping(value = "/users/{userName}/projects")
	    public String showAllProjectsOfUser(@PathVariable String userName,
	                                        Model model) {
	        User user = this.credentialsService.getCredentialsByUserName(userName).getUser();
	        model.addAttribute("userName", userName);
	        model.addAttribute("projectList", this.projectService.getAllProjectsOwnedByUser(user));
	        return "allUserProjects";
	    }

	    @PostMapping(value = "/users/{userName}/projects/{projectId}/delete")
	    public String deleteUserProject(@PathVariable String userName,
	                                    @PathVariable Long projectId,
	                                    Model model) {
	        Project projectToDelete = this.projectService.getProject(projectId);
	        if(projectToDelete != null) {
	            this.projectService.deleteProject(projectToDelete);
	            return "redirect:/admin/users";
	        }
	        return "/admin";
	    }
}
