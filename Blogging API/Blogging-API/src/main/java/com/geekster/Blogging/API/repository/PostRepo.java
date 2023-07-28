package com.geekster.Blogging.API.repository;

import com.geekster.Blogging.API.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository<Post,Long> {
}
