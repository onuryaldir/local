package org.development.authms.route;

import org.development.authms.entity.*;
import org.development.authms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService service;


    public UserController(UserService service) {
        this.service = service;
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        AuthResponse response = null;
        try {
            response = service.authenticate(authRequest);
            if (response != null && response.getMessage().equals("Success")) {
                return ResponseEntity.ok(response);
            }
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestParam String token) {
        AuthResponse response = null;
        try {
            response = service.refresh(token);
            if (response != null && response.getMessage().equals("Success")) {
                return ResponseEntity.ok(response);
            }
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<RegisterRequest> createUser(@RequestBody RegisterRequest request) {
        User user1 = service.createUser(request);
        if ("Success".equals(user1.getMessage())) {
            return new ResponseEntity<>(request, HttpStatus.OK);
        }
        return new ResponseEntity<>(request, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam Long userId) {
        String message = service.deleteUser(userId);
        if ("Success".equals(message)) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get")
    public ResponseEntity<User> findUser(@RequestParam Long userId) {
        User user = service.findUserById(userId);
        if (Objects.nonNull(user)) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/update")
    public ResponseEntity<User> updateUser(
            @RequestParam Long userId,
            @RequestBody UpdateUserRequest request) {
        User updatedUser = service.updateUser(userId, request);
        if (Objects.nonNull(updatedUser)) {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getByEmail")
    public ResponseEntity<User> findByEmail(@RequestParam String email) {
        User user = service.findUserByEmail(email);
        if (Objects.nonNull(user)) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
