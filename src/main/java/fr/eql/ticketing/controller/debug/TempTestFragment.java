package fr.eql.ticketing.controller.debug;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import fr.eql.ticketing.entity.Group;
import fr.eql.ticketing.service.GroupService;

@Controller
public class TempTestFragment {

	GroupService groupService;
	
	public TempTestFragment(GroupService groupService) {
		this.groupService = groupService;
	}
	@GetMapping({"/test-fragment"}) // Point d'entrée d'url (dans le navigateur)
	public String displayGroups(Model model) {
		List<Group> users = groupService.getAllGroups();
		model.addAttribute("users", users);
		return "test/fragment-main"; // le nom du template
	}
}
