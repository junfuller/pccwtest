package org.example.pccwtest.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.pccwtest.dto.UserDTO;
import org.example.pccwtest.model.LoginUser;
import org.example.pccwtest.model.User;
import org.example.pccwtest.security.annotation.Anonymous;
import org.example.pccwtest.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@Validated
@RequiredArgsConstructor
@Tag(name = "Manage Users", description = "Endpoints for user management")
public class UserController {
    private final UserService userService;

    @Anonymous
    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Register a new user with username, email and password")
    public ResponseEntity<User> register(@RequestBody @Validated UserDTO userDTO) {
        User user = userService.registerUser(userDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/test")
    @Operation(summary = "Test Endpoint", description = "Use this endpoint to test the secured endpoint. Bearer token must be provided to access this endpoint.")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("test", HttpStatus.CREATED);
    }

    @GetMapping("/list")
    @Operation(summary = "Retrieve all users", description = "Use this endpoint to retrieve all the users.")
    public ResponseEntity<List<LoginUser>> list() {
        return new ResponseEntity<>(userService.listUser(), HttpStatus.CREATED);
    }

}
