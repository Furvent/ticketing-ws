package fr.eql.ticketing.controller.debug;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.eql.ticketing.entity.User;
import fr.eql.ticketing.service.UserService;

@Controller
@SessionAttributes( value={"listUsers","testString"} )
public class DebugSessionController {
	
	UserService userService;

	public DebugSessionController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping({"/debug-session-1"}) // Point d'entrée d'url (dans le navigateur)
	public String displaySession1(Model model) {
		model.addAttribute("testString", "Modif 1 in DebugSessionController");
		List<User> users = userService.getAllUsers();
		model.addAttribute("listUsers", users);
		return "test/test-session-1"; // le nom du template
	}
	
	@GetMapping({"/debug-session-2"})
	public String displaySession2(Model model) {
		return "test/test-session-2";
	}

}
