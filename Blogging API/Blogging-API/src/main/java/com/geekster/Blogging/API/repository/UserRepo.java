package com.geekster.Blogging.API.repository;

import com.geekster.Blogging.API.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    User findFirstByUserEmail(String email);
}
