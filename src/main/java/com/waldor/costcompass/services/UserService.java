package com.waldor.costcompass.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.waldor.costcompass.models.UserModel;
import com.waldor.costcompass.db.repository.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserModel saveUser(String name, String email) {
        UserModel userModel = new UserModel(name, email);
        return userRepository.save(userModel);
    }

    public List<UserModel> getUsers() {
        return userRepository.findAll();
    }

    public Optional<UserModel> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public UserModel deleteUserById(Long id) {
        UserModel user = getUserById(id)
                .orElseThrow(() -> new RuntimeException());
        userRepository.deleteById(user.getId());
        return user;
    }

}
