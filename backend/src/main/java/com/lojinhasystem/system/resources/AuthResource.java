package com.lojinhasystem.system.resources;

import com.lojinhasystem.system.resources.dto.LoginRequest;
import com.lojinhasystem.system.resources.dto.LoginResponse;
import com.lojinhasystem.system.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AuthResource {

    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
