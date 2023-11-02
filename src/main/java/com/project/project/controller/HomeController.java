package com.project.project.controller;

import com.project.project.entity.BlackListToken;
import com.project.project.model.User;
import com.project.project.model.UserLoginLogout;
import com.project.project.repository.UserLoginLogoutRepository;
import com.project.project.repository.UserRepository;
import com.project.project.service.UserLoginService;
import com.project.project.service.UserService;
import com.project.project.service.impl.CustomUserServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
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
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private UserService userService;

    
    @Autowired
    private CustomUserServiceImpl customUserService;

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private BlackListToken blackListToken;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLoginLogoutRepository userLoginLogoutRepository;

    private String secret = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";

    @GetMapping("/all")
    public List<User> getAllUser() {
        return userService.getUsers();
    }

    @CrossOrigin("http://localhost:3001")
    @GetMapping("/getById/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") int userid) {
        return new ResponseEntity<>(userService.getUserById(userid), HttpStatus.CREATED);
    }

    @GetMapping("/get/{email}")
    public User getByEmail(@PathVariable String email) {
        return (userService.getUserByEmail(email));
    }

    @PutMapping("/update/{userid}")
    public User update(@RequestBody User user, @PathVariable("userid") int userid) {
        return userService.updateUser(user, userid);
    }


    @DeleteMapping("/{userid}")
    public ResponseEntity<String> deleteUser(@PathVariable int userid) {
        boolean deleted = userService.deleteById(userid);
        if (deleted) {
            return ResponseEntity.ok("User is deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid User ID");
        }
    }

    @GetMapping("/logintime/getall")
    public List<UserLoginLogout> getAll() {
        return userLoginService.getAll();
    }

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    private String contry = "Asia/Calcutta";
    private final String WORLD_CLOCK_API_URL = "https://timeapi.io/api/Time/current/zone?timeZone="+contry;

    private String extractUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret) // Set your secret key
                    .parseClaimsJws(token)
                    .getBody();

            // Extract the "email" claim from the JWT (assuming "email" is the claim name)
            String email = claims.get("sub", String.class);
            logger.info("email  from token{} ",email);
            return email;
        } catch (Exception e) {
            // Handle exceptions such as token validation failure
            // Return null or throw an exception as needed
            return "user not found";
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {

        String token = extractTokenFromRequest(request);
        String email=null;
//        System.out.println(token);
        if (token != null) {
            email = extractUsernameFromToken(token);
            logger.info("email From Token :{}",email);
            blackListToken.addToBlacklist(token);
        }

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
            LocalTime logoutTime = LocalTime.parse(timeString1, formatter1);

            Optional<User> user = userRepository.findByEmail(email);

//            String token = extractTokenFromRequest(request);
//            String email1 = extractUsernameFromToken(token);

            if(!ObjectUtils.isEmpty(user))
                user1 = user.get();
            else
                logger.info("user not found with email :",email);

//            Times loginTimeEntry = new Times();
            List<UserLoginLogout> times=userLoginLogoutRepository.findByUserOrderByIdDesc(user1);
            if(!ObjectUtils.isEmpty(times)) {


                UserLoginLogout times1 = times.get(0);
                times1.setLogoutTime(logoutTime);
                times1.setUser(user1);
                userLoginLogoutRepository.save(times1);
            }else {
                logger.info("user not found: {}");
            }
//            System.out.println(" Date: " + dateIndex);
            System.out.println(" Time: " + timeIndex);
        } else {
            System.out.println("The Object is not a LocalDateTime or compatible type.");
        }


        return ResponseEntity.ok("Logged out successfully");
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // 7 is the length of "Bearer "
        }
        return null;
    }


    @GetMapping("/filter")
    public List<User> findByProjectNameAndJobType(
            @RequestParam(name = "projectName", required = false) String projectName,
            @RequestParam(name = "jobType", required = false) String jobType
    ) {
    if (projectName !=null && jobType !=null){
        return userService.findByProjectNameAndJobType(projectName , jobType);
    } else if ( projectName != null){
        return userService.findByProjectName(projectName);
    }else if (jobType != null){
        return userService.findByJobType(jobType);
    }else {
        return userService.findAll();
    }

    }
}

