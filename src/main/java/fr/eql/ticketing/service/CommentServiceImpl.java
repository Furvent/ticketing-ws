package fr.eql.ticketing.service;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.eql.ticketing.entity.Comment;
import fr.eql.ticketing.repository.CommentRepository;

@Service
public class CommentServiceImpl implements CommentService {
	CommentRepository repository;

	public CommentServiceImpl(CommentRepository repository) {
		this.repository = repository;
	}

	public void setRepository(CommentRepository repository) {
		this.repository = repository;
	}

	@Override
	public Comment save(Comment comment) {
		return repository.save(comment);
	}

	@Override
	public List<Comment> getAllComments() {
		return repository.findAll();
	}

	@Override
	public Comment getCommentById(Long commandId) {
		return repository.findById(commandId).get();
	}

	@Override
	public void delete(Comment comment) {
		repository.delete(comment);
	}

	@Override
	public Comment update(Comment comment) {
		return repository.save(comment);
	}
}
