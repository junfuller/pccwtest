package org.example.pccwtest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.pccwtest.dto.UserDTO;
import org.example.pccwtest.security.annotation.Anonymous;
import org.example.pccwtest.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Login Authentication", description = "Endpoints to authenticate login.")
public class LoginController {
    private final UserService userService;

    @Anonymous
    @PostMapping("/login")
    @Operation(summary = "User Login", description = "This endpoint allows a user to log into the system.")
    public ResponseEntity<String> login(@RequestBody @Validated UserDTO userDTO) {
        return userService.loginUser(userDTO);
    }
}
