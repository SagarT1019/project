package com.project.project.controller;


import com.project.project.dto.UserDto;
import com.project.project.entity.JwtRequest;
import com.project.project.entity.JwtResponce;
import com.project.project.model.User;
import com.project.project.model.UserLoginLogout;
import com.project.project.repository.UserLoginLogoutRepository;
import com.project.project.repository.UserRepository;
import com.project.project.security.JwtHelper;
import com.project.project.service.UserService;
import com.project.project.service.impl.CustomUserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Controller
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtHelper helper;
    @Autowired
    private CustomUserServiceImpl customUserService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private  UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLoginLogoutRepository userLoginLogoutRepository;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    private String contry = "Asia/Calcutta";
    private final String WORLD_CLOCK_API_URL = "https://timeapi.io/api/Time/current/zone?timeZone="+contry;

    @PostMapping("/login")
    public ResponseEntity<JwtResponce> login(@RequestBody JwtRequest request) {

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(WORLD_CLOCK_API_URL, Map.class);

        List<Object> valuesList = new ArrayList<>(response.values());

        int index = 8 & 9; // The index you want to access
        User user1=null;
        if (index >= 0 && index < valuesList.size()) {

            Object dateIndex = valuesList.get(8);
            Object timeIndex = valuesList.get(9);

            String timeString = (String) dateIndex; // Assuming valueAtIndex is a string
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate loginDate = LocalDate.parse(timeString, formatter);

            String timeString1 = (String) timeIndex;// Adjust the pattern according to the actual format
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime loginTime = LocalTime.parse(timeString1, formatter1);

            Optional<User> user = userRepository.findByEmail(request.getEmail());
            user1 = user.get();

            UserLoginLogout loginTimeEntry = new UserLoginLogout();
            loginTimeEntry.setDate(loginDate);
            loginTimeEntry.setLoginTime(loginTime);
            loginTimeEntry.setUser(user1);

            userLoginLogoutRepository.save(loginTimeEntry);

            System.out.println(" Date: " + dateIndex);
            System.out.println(" Time: " + timeIndex);
        } else {
            System.out.println("The Object is not a LocalDateTime or compatible type.");
        }

        this.doAuthenticate(request.getEmail(), request.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        JwtResponce responce= generateAuthenticationResponse(user1);

        return new ResponseEntity<>(responce, HttpStatus.OK);
    }


    public JwtResponce generateAuthenticationResponse(User user) {
        var jwtToken = helper.generateToken(user.getUsername(), user.getRole());
        var role = user.getRole();
        var userName = user.getName();
        return JwtResponce.builder()
                .jwtToken(jwtToken)
                .userName(userName)
                .role(role)
                .build();
    }

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@RequestBody UserDto userdto) {
        return new ResponseEntity<>(userService.createUser(userdto), HttpStatus.CREATED);
    }

}
