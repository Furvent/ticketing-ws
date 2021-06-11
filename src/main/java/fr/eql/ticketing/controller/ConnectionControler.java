package fr.eql.ticketing.controller;

import java.time.LocalDateTime;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;

import fr.eql.ticketing.entity.User;
import fr.eql.ticketing.service.UserService;

@Controller
@SessionAttributes("user")
public class ConnectionControler {

	private UserService userService;

	public ConnectionControler(UserService userService) {
		this.userService = userService;
	}

	@GetMapping({ "/connect", "/" })
	public String personneForm(Model model) {
		if(((User) model.getAttribute("user")) == null) {
			model.addAttribute("user", new User());
		}
		
		return "ConnectionPage";
	}

	@PostMapping("/list-users")
	public RedirectView signUpNewUser(@ModelAttribute("user") User user) {
		user.setCreationAccountDate(LocalDateTime.now());
		userService.save(user);
		return new RedirectView("/dashboard");
	}

	@PostMapping("/connexionSubmit")
	public RedirectView connexionSubmit(@ModelAttribute("user") User user, Model model) {
		RedirectView retour;
		User userConnected = userService.getUserForConnection(user.getLogin(), user.getPassword());
		if (userConnected != null) {
			model.addAttribute("user", userConnected);
			retour = new RedirectView("/dashboard");
		} else {
			retour = new RedirectView("/connect");
		}
		return retour;
	}
	
	/* version modele 
	@RequestMapping("/session-end")
    public String finSession(Model model,HttpSession session) {
	    session.invalidate();
        return "/connect"; 
    }*/
	
	//WebRequest permet de supprimer les sessions attributs
	@PostMapping("/deconnexion")
	public RedirectView disconnect(@ModelAttribute("user") User user, Model model, HttpSession session) {
		session.invalidate(); 
		user = new User();
		model.addAttribute(user); 
		return new RedirectView("/connect");
	}
}
