package fr.eql.ticketing.controller.debug;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import fr.eql.ticketing.entity.Group;
import fr.eql.ticketing.service.GroupService;

@Controller
public class DebugGroupController {

	GroupService groupService;

	public DebugGroupController(GroupService groupService) {
		this.groupService = groupService;
	}
	
	// Non utilisé pour l'instant
	@GetMapping({"/debug-group"}) // Point d'entrée d'url (dans le navigateur)
	public String displayGroups(Model model) {
		List<Group> groups = groupService.getAllGroups();
		model.addAttribute("groups", groups);
		return "usersDebug"; // le nom du template
	}

}
