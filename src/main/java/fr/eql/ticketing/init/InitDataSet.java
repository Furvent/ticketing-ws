package fr.eql.ticketing.init;

import java.time.LocalDateTime;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

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

@Profile("initData") // code ci-dessus activé que si le profile "initData" est choisi (parmis
						// d'autres)
						// au démarrage d'un test ou de l'application
@Component
//@Scope("singleton") par defaut
public class InitDataSet {

	@Autowired
	UserService userService;
	@Autowired
	GroupService groupService;
	@Autowired
	MembershipService membershipService;
	@Autowired
	TicketService ticketService;
	@Autowired
	TaskService taskService;
	@Autowired
	StatusService statusService;
	@Autowired
	StatusHistoryService statusHistoryService;

	@PostConstruct
	public void initDemoData() {
		// ajout des utilisateurs lambdas
		User mimi = new User("mimi", "pwdMimi", "mimi92", LocalDateTime.of(2021, 4, 10, 10, 10));
		User matteo = new User("matteo", "pwdMatteo", "matteo le dev", LocalDateTime.of(2021, 4, 10, 12, 10));
		User myriam = new User("myriam", "pwdMyriam", "devMy75", LocalDateTime.of(2021, 4, 10, 14, 10));
		User paul = new User("paul", "pwdPaul", "paul_Celton", LocalDateTime.of(2021, 3, 02, 12, 10));
		User nana = new User("nana", "pwdNana", "nana_traore", LocalDateTime.of(2021, 3, 10, 10, 10));
		User rafael = new User("rafael", "pwdrafael", "rafaelEql", LocalDateTime.now());
		User florentine = new User("florentine", "pwdFlorentine", "floflo", LocalDateTime.now());
		User maxime = new User("maxime", "pwdMaxime", "maxime1977", LocalDateTime.now());
		User marie = new User("marie", "pwdMarie", "marie", LocalDateTime.now());
		User mohamed = new User("mohamed", "pwdMohamed", "mohamed12", LocalDateTime.now());
		User yasmine = new User("yasmine", "pwdYasmine", "yasmine_pro", LocalDateTime.now());
		userService.save(mimi);
		userService.save(matteo);
		userService.save(myriam);
		userService.save(paul);
		userService.save(nana);
		userService.save(rafael);
		userService.save(florentine);
		userService.save(maxime);
		userService.save(marie);
		userService.save(mohamed);
		userService.save(rafael);
		userService.save(yasmine);

		// creation de deux groupes
		// un groupe de copains dev avec mimi comme admin et matteo/myriam comme users
		// un groupe qui organise un évènement mondain avec paul comme admin et nana
		// comme user.

		Group devGroup = new Group("Ticketing App", mimi, LocalDateTime.of(2021, 04, 11, 10, 15));
		groupService.save(devGroup);
		// Membership membership1ToGroup1 = new Membership(user1, group1,
		// LocalDateTime.of(2021, 1, 11, 10, 15));
		createMembership(mimi, devGroup, LocalDateTime.of(2021, 04, 11, 10, 15));
		createMembership(matteo, devGroup, LocalDateTime.of(2021, 05, 05, 12, 15));
		createMembership(myriam, devGroup, LocalDateTime.of(2021, 05, 05, 12, 15));

		Group eventGroup = new Group("Préparation Mariage", paul, LocalDateTime.of(2021, 3, 02, 12, 12));
		groupService.save(eventGroup);
		createMembership(paul, eventGroup, LocalDateTime.of(2021, 3, 02, 12, 12));
		createMembership(nana, eventGroup, LocalDateTime.of(2021, 3, 03, 18, 20));

		// Create new status
		Status statusOpened = new Status(TicketStatus.OPENED);
		Status statusAllocated = new Status(TicketStatus.ALLOCATED);
		Status statusDone = new Status(TicketStatus.DONE);
		Status statusClosed = new Status(TicketStatus.CLOSED);
		Status statusCanceled = new Status(TicketStatus.CANCELED);
		statusService.save(statusOpened);
		statusService.save(statusAllocated);
		statusService.save(statusDone);
		statusService.save(statusClosed);
		statusService.save(statusCanceled);

		/*
		 * ***************************************** //Création des tickets Openned pour
		 * le groupe devGroup; date min LocalDateTime.of(2021, 4, 11, 14, 10));
		 */
		// First 3, going until Closed
		Ticket ticketInfos = new Ticket("Gather infos about other apps",
		"Gather information about already existing" + "ticketing apps." + 
		"\n I'm baby vinyl prism consequat consectetur yr, DIY in aesthetic. "
		+ "Biodiesel lo-fi squid man braid microdosing la croix lumbersexual elit mustache affogato veniam brooklyn lyft"
		, devGroup);
		ticketService.save(ticketInfos);
		addStatusHistory(ticketInfos, statusOpened, LocalDateTime.of(2021, 4, 14, 15, 10));
		addTaskBetweenUserAndTicket(mimi, ticketInfos, statusAllocated, LocalDateTime.of(2021, 1, 14, 16, 10));
		addStatusHistory(ticketInfos, statusDone, LocalDateTime.of(2021, 4, 15, 15, 10));
		addStatusHistory(ticketInfos, statusClosed, LocalDateTime.of(2021, 4, 15, 16, 10));

		Ticket ticketDataModel = new Ticket("Prepare the database", 
		"Prepare the database model for our " + "ticketing apps." +
		" Authentic dreamcatcher sustainable, XOXO nisi 3 wolf moon hell of hashtag. +1 typewriter PBR&B "
		+ "voluptate kickstarter sustainable palo santo poutine, craft beer street art woke.",
		devGroup);
		ticketService.save(ticketDataModel);
		addStatusHistory(ticketDataModel, statusOpened, LocalDateTime.of(2021, 4, 14, 15, 15));
		addTaskBetweenUserAndTicket(matteo, ticketDataModel, statusAllocated, LocalDateTime.of(2021, 5, 15, 16, 10));
		addTaskBetweenUserAndTicket(myriam, ticketDataModel, statusAllocated, LocalDateTime.of(2021, 5, 15, 16, 10));
		addStatusHistory(ticketDataModel, statusDone, LocalDateTime.of(2021, 05, 17, 15, 10));
		addStatusHistory(ticketDataModel, statusClosed, LocalDateTime.of(2021, 05, 18, 15, 10));

		Ticket ticketComm = new Ticket("Setup sharing tools", 
		"Prepare a discord and drive to share data. " +
		"\n Aliquip consequat minim cold-pressed, tote bag cloud bread pork belly. "
		+ "Taxidermy glossier williamsburg cred stumptown adaptogen duis cliche viral farm-to-table keytar.", 
		devGroup);
		ticketService.save(ticketComm);
		addStatusHistory(ticketComm, statusOpened, LocalDateTime.of(2021, 04, 14, 15, 20));
		addTaskBetweenUserAndTicket(matteo, ticketComm, statusAllocated, LocalDateTime.of(2021, 05, 15, 16, 10));
		addStatusHistory(ticketComm, statusDone, LocalDateTime.of(2021, 05, 17, 15, 10));
		addStatusHistory(ticketComm, statusClosed, LocalDateTime.of(2021, 05, 18, 15, 10));

		// next two in done
		Ticket ticketBack = new Ticket("Code the back", 
		"Program and implement the back-end" + 
		"\n Dolor meggings edison bulb, schlitz knausgaard occaecat laboris tbh messenger bag marfa. "
		+ "Sed meggings culpa listicle. Chillwave master cleanse mlkshk adaptogen typewriter fam.", 
		devGroup);
		ticketService.save(ticketBack);
		addStatusHistory(ticketBack, statusOpened, LocalDateTime.of(2021, 05, 14, 15, 25));
		addTaskBetweenUserAndTicket(matteo, ticketBack, statusAllocated, LocalDateTime.of(2021, 05, 14, 19, 25));
		addStatusHistory(ticketBack, statusDone, LocalDateTime.of(2021, 05, 17, 15, 10));

		Ticket ticketDemo = new Ticket("Prepare a demo", 
		"Prepare a scenario to be used in a demo." +
		"\n Eiusmod normcore irony af, eu sed lumbersexual 3 wolf moon. Locavore aliqua banjo, snackwave anim 3 wolf moon air plant.",
		devGroup);
		ticketService.save(ticketDemo);
		addStatusHistory(ticketDemo, statusAllocated, LocalDateTime.of(2021, 05, 16, 15, 10));
		addTaskBetweenUserAndTicket(myriam, ticketDemo, statusAllocated, LocalDateTime.of(2021, 05, 16, 18, 10));
		addStatusHistory(ticketDemo, statusDone, LocalDateTime.of(2021, 05, 19, 15, 10));

		// next two in Allocated
		Ticket ticketFront = new Ticket("Code the front",
		"Program and implement the front-end." +
		"\n Hashtag ennui ugh drinking vinegar wolf raw denim dolor freegan enim next level XOXO blog tumeric. "
		+ "VHS enim cray health goth tilde. Fugiat activated charcoal enim nulla butcher bicycle rights, taiyaki fixie distillery",
		devGroup);
		ticketService.save(ticketFront);
		addStatusHistory(ticketFront, statusOpened, LocalDateTime.of(2021, 05, 14, 15, 25));
		addTaskBetweenUserAndTicket(mimi, ticketFront, statusAllocated, LocalDateTime.of(2021, 05, 18, 19, 25));

		Ticket ticketHost = new Ticket("Find a hosting service", 
		"Find an hosting service." + 
		"\n Quinoa dolore ethical sartorial +1. Cloud bread four dollar toast stumptown commodo migas seitan. "
		+ "Skateboard biodiesel scenester, lyft tilde ut chicharrones consequat kogi velit prism. Man braid 8-bit excepteur salvia."
		,devGroup);
		ticketService.save(ticketHost);
		addStatusHistory(ticketHost, statusOpened, LocalDateTime.of(2021, 05, 14, 15, 25));
		addTaskBetweenUserAndTicket(myriam, ticketHost, statusAllocated, LocalDateTime.of(2021, 05, 16, 19, 25));

		// next two in Opened
		Ticket ticketPopulate = new Ticket( "Populate the database", 
		"Populate the database." + 
		"\n Skateboard occaecat farm-to-table sriracha, deep v fam austin ramps tumeric cliche migas. "
		+ "Scenester forage everyday carry taxidermy taiyaki pitchfork helvetica ullamco ea nostrud."
		, 	devGroup);
		ticketService.save(ticketPopulate);
		addStatusHistory(ticketPopulate, statusOpened, LocalDateTime.of(2021, 05, 14, 15, 25));

		Ticket ticketCloud = new Ticket("PrepareCloud", 
		"Push the app in the clouds." + 
		"\n Kale chips fashion axe af air plant anim sartorial succulents ut pop-up helvetica synth vaporware. "
		+ "Sustainable tacos aute jianbing, narwhal bicycle rights pour-over adipisicing. ", devGroup);
		ticketService.save(ticketCloud);
		addStatusHistory(ticketCloud, statusOpened, LocalDateTime.of(2021, 05, 14, 15, 25));

		// LocalDateTime.of(2021, 3, 03, 18, 20));
		/*
		 * ***************************************** //Création des tickets Openned pour
		 * le groupe devGroup; date min LocalDateTime.of(2021, 3, 03, 18, 20)); acteurs
		 * : paul, nana
		 */

		// Closed
		Ticket ticketDate = new Ticket("Fixer la date de marriage", "Fixer la date", eventGroup);
		ticketService.save(ticketDate);
		addStatusHistory(ticketDate, statusOpened, LocalDateTime.of(2021, 03, 12, 18, 20));
		addTaskBetweenUserAndTicket(paul, ticketDate, statusAllocated, LocalDateTime.of(2021, 03, 12, 19, 20));
		addTaskBetweenUserAndTicket(nana, ticketDate, statusAllocated, LocalDateTime.of(2021, 03, 12, 19, 20));
		addStatusHistory(ticketDate, statusDone, LocalDateTime.of(2021, 03, 14, 18, 20));
		addStatusHistory(ticketDate, statusClosed, LocalDateTime.of(2021, 03, 15, 18, 20));

		// find florinst, caterer, decoration, sitting arrangements

		// Deux suivants en done
		Ticket ticketVenue = new Ticket("Trouver et réserver un lieu pour organiser le mariage",
				"Trouver lieu de mariage", eventGroup);
		ticketService.save(ticketVenue);
		addStatusHistory(ticketVenue, statusOpened, LocalDateTime.of(2021, 03, 16, 18, 20));
		addTaskBetweenUserAndTicket(paul, ticketVenue, statusAllocated, LocalDateTime.of(2021, 03, 17, 18, 20));
		addTaskBetweenUserAndTicket(nana, ticketVenue, statusAllocated, LocalDateTime.of(2021, 03, 17, 18, 20));
		addStatusHistory(ticketVenue, statusDone, LocalDateTime.of(2021, 03, 30, 18, 20));

		Ticket ticketCaterer = new Ticket("Trouver un traiteur pour le marriage vers les dates", "Trouver un traiteur",
				eventGroup);
		ticketService.save(ticketCaterer);
		addStatusHistory(ticketCaterer, statusOpened, LocalDateTime.of(2021, 03, 20, 18, 20));
		addTaskBetweenUserAndTicket(nana, ticketCaterer, statusAllocated, LocalDateTime.of(2021, 03, 21, 18, 20));
		addStatusHistory(ticketCaterer, statusDone, LocalDateTime.of(2021, 03, 30, 18, 20));

		// Next two allocated

		Ticket ticketInvite = new Ticket("Préparer et envoyer les invitations", "Faire les invitations", eventGroup);
		ticketService.save(ticketInvite);
		addStatusHistory(ticketInvite, statusOpened, LocalDateTime.of(2021, 04, 01, 18, 20));
		addTaskBetweenUserAndTicket(paul, ticketInvite, statusAllocated, LocalDateTime.of(2021, 04, 01, 20, 20));

		Ticket ticketMusician = new Ticket("Trouver et réserver des musiciens pour le mariage",
				"Réserver des musiciens", eventGroup);
		ticketService.save(ticketMusician);
		addStatusHistory(ticketMusician, statusOpened, LocalDateTime.of(2021, 04, 01, 19, 20));
		addTaskBetweenUserAndTicket(nana, ticketMusician, statusAllocated, LocalDateTime.of(2021, 04, 02, 18, 20));

		// Next two opened
		Ticket ticketHelp = new Ticket("Inviter d'autres personnes pour aider à la préparation du mariage",
				"Chercher de l'aide pour l'organisation", eventGroup);
		ticketService.save(ticketHelp);
		addStatusHistory(ticketHelp, statusOpened, LocalDateTime.of(2021, 04, 03, 18, 20));

		Ticket ticketDeco = new Ticket("Trouver et réserver un décorateur qui sait faire du rococo pour le mariage",
				"Réserver un décorateur", eventGroup);
		ticketService.save(ticketDeco);
		addStatusHistory(ticketDeco, statusOpened, LocalDateTime.of(2021, 04, 05, 19, 20));

	}

	private void createMembership(User user, Group group, LocalDateTime time) {
		membershipService.save(new Membership(user, group, time));
	}

	private void addStatusHistory(Ticket ticket, Status status, LocalDateTime taskTime) {
		StatusHistory sh = new StatusHistory(status, ticket, taskTime);
		statusHistoryService.save(sh);

	}

	private void addTaskBetweenUserAndTicket(User user, Ticket ticket, Status statusAllocated, LocalDateTime taskTime) {
		// Create a task
		Task task = new Task(user, ticket, taskTime);
		// Add StatusHistory on ticket
		StatusHistory sh = new StatusHistory(statusAllocated, ticket, taskTime);
		// Save entity
		taskService.save(task);
		statusHistoryService.save(sh);
	}

}
