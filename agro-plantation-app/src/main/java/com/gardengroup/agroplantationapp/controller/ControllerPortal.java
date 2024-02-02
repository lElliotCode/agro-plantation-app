package com.gardengroup.agroplantationapp.controller;

import com.gardengroup.agroplantationapp.dtos.DtoLogin;
import com.gardengroup.agroplantationapp.dtos.DtoRegistrer;
import com.gardengroup.agroplantationapp.exceptions.OurException;
import com.gardengroup.agroplantationapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/auth")
public class ControllerPortal {

    @Autowired
    private UserService userService;



    @PostMapping("/register")
    public ResponseEntity<String> record(@RequestBody DtoRegistrer dtoRegistrer) {
        
        try {
            // Verifica si el correo electrónico ya existe
            if (userService.existsEmail(dtoRegistrer.getEmail())) {
                // Si el correo electrónico ya existe, devuelve un mensaje indicando que el usuario ya existe
                return new ResponseEntity<>("Este correo electrónico ya está registrado.", HttpStatus.CONFLICT);
            }
            
            // Guarda el usuario en la base de datos
            userService.createUser(dtoRegistrer);

            // Creación exitosa, devuelve una respuesta con HttpStatus.CREATED y el mensaje
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado correctamente!");
        } catch (OurException ex) {
            // En caso de otros errores, devuelve una respuesta con HttpStatus.NOT_IMPLEMENTED
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(ex.getMessage());
        }
    }



    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody DtoLogin user) {
        try {
            return ResponseEntity.ok(userService.authentication(user));
        } catch (OurException ex) {
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
        

    }

}
