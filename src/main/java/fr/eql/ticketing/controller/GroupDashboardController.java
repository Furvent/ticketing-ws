package fr.eql.ticketing.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.view.RedirectView;

import fr.eql.ticketing.controller.ViewModels.ViewUserTaskTicket;
import fr.eql.ticketing.controller.form.AddUserForm;
import fr.eql.ticketing.controller.form.TicketForm;
import fr.eql.ticketing.entity.Group;
import fr.eql.ticketing.entity.Membership;
import fr.eql.ticketing.entity.Status;
import fr.eql.ticketing.entity.StatusHistory;
import fr.eql.ticketing.entity.Task;
import fr.eql.ticketing.entity.Ticket;
import fr.eql.ticketing.entity.User;
import fr.eql.ticketing.enums.TicketStatus;
import fr.eql.ticketing.service.GroupService;
import fr.eql.ticketing.service.MembershipService;
import fr.eql.ticketing.service.StatusHistoryService;
import fr.eql.ticketing.service.StatusService;
import fr.eql.ticketing.service.TaskService;
import fr.eql.ticketing.service.TicketService;
import fr.eql.ticketing.service.UserService;

@Controller
@SessionAttributes(value = { "user", "groupSelectedByUserId", "isUserAdmin" })
public class GroupDashboardController {
	GroupService groupService;
	MembershipService membershipService;
	UserService userService;
	TicketService ticketService;
	StatusHistoryService statusHistoryService;
	TaskService taskService;
	StatusService statusService;

	public GroupDashboardController(GroupService groupService, MembershipService membershipService,
			UserService userService, TicketService ticketService, StatusHistoryService statusHistoryService,
			TaskService taskService, StatusService statusService) {
		this.groupService = groupService;
		this.membershipService = membershipService;
		this.userService = userService;
		this.ticketService = ticketService;
		this.statusHistoryService = statusHistoryService;
		this.taskService = taskService;
		this.statusService = statusService;
	}

	@GetMapping("group")
	public String displayGroupDashboard(Model model, @RequestParam(name = "groupId", required = true) String groupID) {
		String templateName = "";
		// Récupération et envoi des données pour la page admin
		Long groupIdLong = Long.parseLong(groupID);
		List<User> users2 = displayUsersByGroup(groupIdLong);
		model.addAttribute("users2", users2);
		AddUserForm addUserForm = new AddUserForm();
		model.addAttribute("addUserForm", addUserForm);
		// Récupération des users qu'on peut ajouter au groupe
		List<User> userToAdd = new ArrayList<User>();
		List<User> allUsers = userService.getAllUsers();

		for (User user : allUsers) {
			// On teste si le user est dans le groupe
			Boolean isInGroupe = false;
			for (Membership membership : user.getMemberships()) {
				if (membership.getGroup().getId() == groupIdLong) {
					isInGroupe = true;
					break;
				}
			}
			if (isInGroupe == false) {
				userToAdd.add(user);
			}
		}
		for (User user : userToAdd) {
			System.out.println(user.getPseudo());
		}
		model.addAttribute("userToAdd", userToAdd);

		// Restriction de l'accès à la page admin
		boolean isAdmin = false;
		User userConnected = (User) model.getAttribute("user");
		Group actifGroup = groupService.getGroupById(groupIdLong);
		if (userConnected.equals(actifGroup.getCreatedBy())) {
			isAdmin = true;
		}
		System.err.println(isAdmin);
		model.addAttribute("isAdmin", isAdmin);
		System.err.println((boolean) model.getAttribute("isAdmin"));

		// On ajoute le groupId en session
		model.addAttribute("groupSelectedByUserId", groupID);
		// On vérifie que l'utilisateur appartienne bien à ce groupe
		User user = (User) model.getAttribute("user");
		List<Membership> userMemberships = membershipService.getMembershipsWithUser(user);
		Boolean isUserBelongToGroup = false;
		for (Membership membership : userMemberships) {
			if (membership.getGroup().getId() == groupIdLong) {
				isUserBelongToGroup = true;
			}
		}
		if (isUserBelongToGroup) {
			// On récupère le group en db
			Group currUserGroup = groupService.getGroupById(groupIdLong);
			model.addAttribute("currUserGroup", currUserGroup);
			// On vérifie si l'utilisateur est administrateur et on rajoute l'information
			// dans le model
			model.addAttribute("isUserAdmin", checkIfUserIsAdmin(user, currUserGroup));
			// On récupère tous les tickets du groupe
			List<Ticket> groupTickets = getGroupTickets(currUserGroup);
			model.addAttribute("allTickets", groupTickets);
			// On trie les tickets selon leur dernier statut
			sortGroupTicketsByLastStatusAndAddThemToModel(model, groupTickets);
			// On ajoute au modèle les tickets attribués à l'utilisateur (task)
			addToModelUserOnTaskTickets(model, user, groupIdLong);
			// On ajoute les titres de status au model
			addStatusTitleToModel(model);
			// Ajout form ticket
			formAddTicket(model, currUserGroup);
			templateName = "dashboard/group-dashboard.html";
		} else {
			templateName = "dashboard/general-dashboard.html";
		}
		return templateName;
	}

	public void formAddTicket(Model model, Group group) {
		model.addAttribute("ticketForm", new TicketForm());
		List<User> users = new ArrayList<User>();
		// Ajouter la sélection du groupe
		users = displayUsersByGroup(model, group);
		model.addAttribute("users", users);
	}

	private List<User> displayUsersByGroup(Model model, Group group) {
		List<Membership> memberships = membershipService.getMembershipsWithGroup(group);
		List<User> users = new ArrayList<User>();
		for (Membership membership : memberships) {
			users.add(membership.getUser());
		}
		return users;
	}

	// Déclencher lors de l'ajout d'un nouveau membre au groupe
	@PostMapping("/addUser")
	public RedirectView deleteUser(@ModelAttribute("addUserForm") AddUserForm addUserForm,
			@ModelAttribute("groupSelectedByUserId") Long groupId) {
		List<Long> idUserToAdds = addUserForm.getIdUserToAdd();
		for (Long idUser : idUserToAdds) {
			User user = userService.getUserWithId(idUser);
			Group group = groupService.getGroupById(groupId);
			Membership membership = new Membership(user, group, LocalDateTime.now());
			membershipService.save(membership);
		}

		return new RedirectView("group?groupId=" + groupId);
	}

	@PostMapping("/setTicketStatusToDown")
	public RedirectView setTicketStatusToDown(@ModelAttribute("groupSelectedByUserId") Long groupSelectedByUserId,
			@RequestParam("ticketIdToChangeStatusToDone") String ticketIdToChangeStatusToDone) {
		// get ticket from db
		Long idTicketToDone = Long.parseLong(ticketIdToChangeStatusToDone);
		Ticket ticketSelected = ticketService.getTicketById(idTicketToDone);
		// get status done from DB
		Status statusAllocated = statusService.getStatusByLabel(TicketStatus.DONE);
		// create a new StatusHistory between ticket and status
		StatusHistory statusHistory = new StatusHistory(statusAllocated, ticketSelected, LocalDateTime.now());
		statusHistoryService.save(statusHistory);
		return new RedirectView("/group?groupId=" + groupSelectedByUserId);
	}

	private List<Ticket> getGroupTickets(Group group) {
		return ticketService.getTicketsWithGroup(group);
	}

	private void sortGroupTicketsByLastStatusAndAddThemToModel(Model model, List<Ticket> tickets) {
		List<Ticket> openedTickets = new ArrayList<Ticket>();
		List<Ticket> allocatedTickets = new ArrayList<Ticket>();
		List<Ticket> doneTickets = new ArrayList<Ticket>();
		List<Ticket> closedTickets = new ArrayList<Ticket>();
		for (Ticket ticket : tickets) {
			// On récupère le dernier Statut / NB : pas du tout optimisé puisque une
			// ouverture
			// de requête SQL est coûteux, peut être récupérer prélablement les
			// HistoricsStatus
			StatusHistory lastStatusHistory = statusHistoryService.getLastStatusHistoryFromThisTicket(ticket);
			if (lastStatusHistory != null) {
				switch (lastStatusHistory.getStatus().getLabel()) {
				case TicketStatus.OPENED:
					openedTickets.add(ticket);
					break;
				case TicketStatus.ALLOCATED:
					allocatedTickets.add(ticket);
					break;
				case TicketStatus.DONE:
					doneTickets.add(ticket);
					break;
				case TicketStatus.CLOSED:
					closedTickets.add(ticket);
					break;
				}
			}
		}
		model.addAttribute("openedTickets", openedTickets);
		model.addAttribute("allocatedTickets", allocatedTickets);
		model.addAttribute("doneTickets", doneTickets);
		model.addAttribute("closedTickets", closedTickets);
	}

	private void addStatusTitleToModel(Model model) {
		model.addAttribute("statusTitleOpened", "Opened");
		model.addAttribute("statusTitleAllocated", "Allocated");
		model.addAttribute("statusTitleDone", "Done");
		model.addAttribute("statusTitleClosed", "Closed");
	}

	private void addToModelUserOnTaskTickets(Model model, User user, Long groupId) {
		List<ViewUserTaskTicket> userTicketsView = new ArrayList<ViewUserTaskTicket>();
		// On va chercher les task de l'utilisateur
		List<Task> userTasks = taskService.getTasksByUser(user);
		// On sélection les tickets appartenant uniquement au groupe sélectionné
		List<Ticket> userTicketsOfThisGroup = new ArrayList<Ticket>();
		for (Task task : userTasks) {
			if (task.getTicket().getGroup().getId() == groupId) {
				userTicketsOfThisGroup.add(task.getTicket());
			}
		}
		for (Ticket ticket : userTicketsOfThisGroup) {
			// Pour créer notre TicketView on a besoin du ticket, du dernier statut, et des
			// utilisateurs en tâche dessus
			// On commence par récupérer les task d'un ticket
			List<Task> ticketTasks = taskService.getTasksByTicket(ticket);
			// et on récupère les pseudos
			List<String> usersPseudo = getUsersPseudoFromListTasks(ticketTasks);
			// Et enfin on récupère le dernier statut
			String status = getLastStatusOfTheTicket(ticket);
			userTicketsView.add(new ViewUserTaskTicket(ticket, status, usersPseudo));
		}
		model.addAttribute("userTickets", userTicketsView);
	}

	private String getLastStatusOfTheTicket(Ticket ticket) {
		StatusHistory lastStatusHistory = statusHistoryService.getLastStatusHistoryFromThisTicket(ticket);
		if (lastStatusHistory != null) {
			return lastStatusHistory.getStatus().getLabel();
		}
		return "No status found";
	}

	private List<String> getUsersPseudoFromListTasks(List<Task> tasks) {
		List<String> pseudos = new ArrayList<String>();
		for (Task task : tasks) {
			pseudos.add(task.getUser().getPseudo());
		}
		return pseudos;
	}

	private boolean checkIfUserIsAdmin(User user, Group group) {
		return user.getId() == group.getCreatedBy().getId();
	}

	// Récupérer la liste des users du groupe
	private List<User> displayUsersByGroup(Long idGroup) {
		Group group = groupService.getGroupById(idGroup);
		List<Membership> memberships = membershipService.getMembershipsWithGroup(group);
		List<User> users = new ArrayList<User>();
		for (Membership membership : memberships) {
			users.add(membership.getUser());
		}
		return users;
	}

}
