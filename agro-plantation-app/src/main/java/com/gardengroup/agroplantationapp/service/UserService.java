package com.gardengroup.agroplantationapp.service;

import com.gardengroup.agroplantationapp.dtos.DtoLogin;
import com.gardengroup.agroplantationapp.dtos.DtoRegistrer;
import com.gardengroup.agroplantationapp.entities.User;
import com.gardengroup.agroplantationapp.entities.UserType;
import com.gardengroup.agroplantationapp.exceptions.OurException;
import com.gardengroup.agroplantationapp.repository.UserRepository;
import com.gardengroup.agroplantationapp.repository.UserTypeRepository;
import com.gardengroup.agroplantationapp.security.JwtTokenProvider;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserTypeRepository userTypeRepository;
    @Autowired
    private AuthenticationManager authenticationManager;// Gestor de autenticación para verificar las credenciales enviadas durante el inicio de sesión
    @Autowired
    private JwtTokenProvider jwtTokenProvider;// Proveedor de tokens JWT encargado de generar y validar tokens de autenticación


    @Transactional
    public User newUser(String name, String lastname, String email, String address, String password) throws OurException {
        validate(name, lastname, email, address, password);

        User user = new User();

        user.setName(name);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setAddress(address);
        user.setPassword(password);

        UserType userType = new UserType();
        userType.setType("USER");
        user.setUserType(userType);


        return user;
    }

    @Transactional
    public void createUser(DtoRegistrer dtoRegistrer) throws OurException {
        
        User user = new User(dtoRegistrer.getEmail(), 
            passwordEncoder.encode(dtoRegistrer.getPassword()));
        
        userRepository.save(user);
        
    }

    public User getOne(Long id) {
        return userRepository.getOne(id);
    }

    @Transactional
    public List<User> listusers() {
        List<User> users = new ArrayList();
        users = userRepository.findAll();
        return users;
    }


    public Boolean existsEmail(String email) {
        //DtoRegistrer dtoRegistrer = new DtoRegistrer();
        //String emailDto= dtoRegistrer.getEmail();
        
        return userRepository.existsByUserEmail(email);
        // if (userRepository.existsByUserEmail(email)) {
        //     return true;
        // }
        //return false;
    }


    public void validate(String name, String lastname, String email, String address, String password) throws OurException {

        if (name == null || name.isEmpty()) {
            throw new OurException("El nombre no puede ser nulo o estar vacío");
        }
        if (lastname == null || lastname.isEmpty()) {
            throw new OurException("El apellido no puede ser nulo o estar vacío");
        }
        if (email == null || email.isEmpty()) {
            throw new OurException("El correo electrónico no puede ser nulo o estar vacío");
        }
        if (address == null || address.isEmpty()) {
            throw new OurException("La dirección no puede ser nula o estar vacía");
        }
        if (password == null || password.isEmpty() || password.length() <= 5) {
            throw new OurException("La contraseña no puede ser vacía, ni nula y debe tener más de 5 caracteres");
        }
    }


    // public User findByUserEmail(String email) {
    //     return userRepository.searchEmail(email);
    // }

    public String authentication(DtoLogin user) throws OurException{
        
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );
        // Establece la autenticación en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Se genera el token JWT
        
        String token = jwtTokenProvider.generateToken(authentication);

        //

        return token;
        
    }


}
