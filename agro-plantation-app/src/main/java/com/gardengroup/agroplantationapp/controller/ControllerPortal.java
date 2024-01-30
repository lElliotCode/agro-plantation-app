package com.gardengroup.agroplantationapp.controller;

import com.gardengroup.agroplantationapp.dtos.DtoRegistrer;
import com.gardengroup.agroplantationapp.entities.User;
import com.gardengroup.agroplantationapp.exceptions.OurException;
import com.gardengroup.agroplantationapp.security.JwtTokenProvider;
import com.gardengroup.agroplantationapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class ControllerPortal {

    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;


    @PostMapping("/registro")
    public ResponseEntity<String> record(@RequestBody User user) {

        try {
            userService.createUser(user);

            // Creación exitosa, devuelve una respuesta con HttpStatus.CREATED
            return new ResponseEntity<>("Usuario registrado correctamente!", HttpStatus.CREATED);
        } catch (OurException ex) {
            // En caso de error, devuelve una respuesta con HttpStatus.NOT_IMPLEMENTED
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(ex.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        User userNew = userService.findByUserEmail(user.getEmail());

        if (userNew != null && userNew.getPassword().equals(user.getPassword())) {
            return new ResponseEntity<>("Login exitoso", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Credenciales incorrectas", HttpStatus.UNAUTHORIZED);
        }
    }


}
