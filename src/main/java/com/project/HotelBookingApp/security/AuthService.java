package com.project.HotelBookingApp.security;

import com.project.HotelBookingApp.dtos.LoginRequestDto;
import com.project.HotelBookingApp.dtos.SignupRequestDto;
import com.project.HotelBookingApp.dtos.UserDto;
import com.project.HotelBookingApp.entities.User;
import com.project.HotelBookingApp.entities.enums.Roles;
import com.project.HotelBookingApp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserDto signup(SignupRequestDto signupRequestDto){
        User user = userRepository.findByEmail(signupRequestDto.getEmail()).orElse(null);


        if (user != null){
            throw  new RuntimeException("User already exists with this email: "+ signupRequestDto.getEmail());
        }
        //if not signup
        User newUser = mapper.map(signupRequestDto,User.class);

        newUser.setRole(Set.of(Roles.GUEST)); //by default guest role...
        newUser.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));

        newUser = userRepository.save(newUser);

        return mapper.map(newUser, UserDto.class);

    }

    public String[] login(LoginRequestDto loginRequestDto){
        Authentication authenticated = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequestDto.getEmail(), loginRequestDto.getPassword()
        ));

        User user = (User) authenticated.getPrincipal();

        String[] arr = new String[2];
        arr[0] = jwtService.generateAccessToken(user);
        arr[1] = jwtService.generateRefreshToken(user);

        return arr;

    }

}
