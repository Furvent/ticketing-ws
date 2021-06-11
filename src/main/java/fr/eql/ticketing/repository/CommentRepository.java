package fr.eql.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.eql.ticketing.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{

}
