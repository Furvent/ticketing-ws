package fr.eql.ticketing.controller.rest.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import fr.eql.ticketing.exception.restController.InvalidNewDataPostException;
import fr.eql.ticketing.controller.rest.dto.create.NewComment;
import fr.eql.ticketing.enums.EntityType;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/comment", headers = "Accept=application/json")
public class CommentRestController {
	
	private static String urlWSComment = "http://localhost:8083/api/";

	@PostMapping("/createTicket")
	public ResponseEntity<?> createTicketComment(@RequestBody NewComment newComment) {
		
		try {
			// Checks
			dataCheck(newComment);
			
			if (!(newComment.getEntityType() == null) && !newComment.getEntityType().equals(EntityType.TICKET.name())) {
				throw new InvalidNewDataPostException("The entity type invalid for this request");
			}
			
			String url = urlWSComment + "public/create";
			newComment.setEntityType(EntityType.TICKET.name());
			System.out.println(newComment.getEntityType());
			ResponseEntity<?> result = setUpAndSendResponse(newComment, url);

			return new ResponseEntity<>(HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}
	@PostMapping("/createGroup")
	public ResponseEntity<?> createGroupComment(@RequestBody NewComment newComment) {
		
		try {
			dataCheck(newComment);
			
			String url = urlWSComment + "public/create";
			newComment.setEntityType(EntityType.GROUP.name());
			ResponseEntity<?> result = setUpAndSendResponse(newComment, url);

			return new ResponseEntity<>(HttpStatus.OK);
		} catch(Exception e) {
			System.err.println(e);
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}
	
	private ResponseEntity<?> setUpAndSendResponse(NewComment newComment, String url){
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<NewComment> request = new HttpEntity<>(newComment);
		ResponseEntity<?> result = restTemplate.postForObject(url, request, ResponseEntity.class);
		return result;
	}
	
	private void dataCheck(NewComment newComment) throws InvalidNewDataPostException {
		if (newComment.getEntityId() == null || newComment.getEntityId() < 1l) {
			throw new InvalidNewDataPostException("Can't create comment with invalid entityId: " + newComment.getEntityId());
		}
		if (newComment.getAuthor() == null || newComment.getAuthor().isEmpty()) {
			throw new InvalidNewDataPostException("Can't create comment with empty author's name");
		}
		if (newComment.getText() == null || newComment.getText().isEmpty()) {
			throw new InvalidNewDataPostException("Can't create comment with empty text");
		}
	}

}