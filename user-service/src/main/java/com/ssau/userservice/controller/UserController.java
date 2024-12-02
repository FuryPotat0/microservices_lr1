package com.ssau.userservice.controller;

import com.ssau.userservice.dto.UserDto;
import com.ssau.userservice.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;

    @GetMapping("/{userId}/username")
    public ResponseEntity<?> getUserNameById(@PathVariable("userId") Long userId) {
        try {
            return ResponseEntity.ok(service.getUserNameById(userId));
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/exists-by-id/{userId}")
    public Boolean existUserById(@PathVariable("userId") Long userId) {
        return service.existById(userId);
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(UserDto dto) {
        try {
            return new ResponseEntity<>(service.createUser(dto), HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{userId}/state")
    public ResponseEntity<?> setUserState(@PathVariable("userId") Long userId, Boolean isEnabled) {
        try {
            return new ResponseEntity<>(
                    service.setUserState(userId, isEnabled), HttpStatus.CREATED
            );
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public List<UserDto> getAllUsers() {
        return service.getAllUsers();
    }

    @PatchMapping("/update-user")
    public ResponseEntity<?> updateUser(UserDto dto) {
        try {
            return new ResponseEntity<>(service.updateUser(dto), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
