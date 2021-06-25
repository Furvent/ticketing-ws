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

import fr.eql.ticketing.controller.rest.dto.create.NewComment;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/comment", headers = "Accept=application/json")
public class CommentRestController {

	@PostMapping("/create")
	public ResponseEntity<?> testCreateComment(@RequestBody NewComment newComment) {
		String url = "http://localhost:8083/api-comments/public/create";
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<NewComment> request = new HttpEntity<>(newComment);
		ResponseEntity<?> result = restTemplate.postForObject(url, request, ResponseEntity.class);
		System.out.println(result);

		return new ResponseEntity<>(HttpStatus.OK);
	}
}
