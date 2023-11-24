package com.example.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.user.domain.User;



public interface UserRepo extends JpaRepository<User, Long> {
}
