package com.example.userservice.repositories;

import com.example.userservice.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.repository.JpaRepository;
@Repository
public interface UserRepository extends JpaReposiroty<User,long> {
}
