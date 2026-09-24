package com.waldor.costcompass.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.waldor.costcompass.models.UserModel;


public interface UserRepository extends JpaRepository<UserModel, Long> {
}