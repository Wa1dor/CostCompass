package com.waldor.costcompass.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.waldor.costcompass.dto.UserDto;
import com.waldor.costcompass.models.UserModel;
import com.waldor.costcompass.services.UserService;


@RestController
@RequestMapping("/api/costcompass/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

@PostMapping("")
public ResponseEntity<UserModel> createUser(@RequestBody UserDto userDto) {
    System.out.println(userDto.name());
    UserModel add = userService.saveUser(userDto.name(), userDto.email());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(add.getId())
        .toUri();
    return ResponseEntity.created(location).body(add);
}

@GetMapping("")
public List<UserModel> getAllUsers() {
    return userService.getUsers();
}

@GetMapping("/{id}")
public Optional<UserModel> getUser(@PathVariable Long id){
    return userService.getUserById(id);
}

@DeleteMapping("/{id}")
public ResponseEntity<UserModel> deleteUser(@PathVariable Long id) {
    userService.deleteUserById(id);
    return ResponseEntity.noContent().build();
}

}
