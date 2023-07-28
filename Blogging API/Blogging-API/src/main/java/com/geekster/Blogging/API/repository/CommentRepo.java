package com.geekster.Blogging.API.repository;

import com.geekster.Blogging.API.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface CommentRepo extends JpaRepository<Comment,Integer> {

}
