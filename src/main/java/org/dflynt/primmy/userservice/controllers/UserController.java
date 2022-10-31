package org.dflynt.primmy.userservice.controllers;

import org.dflynt.primmy.userservice.models.User;
import org.dflynt.primmy.userservice.services.EmailService;
import org.dflynt.primmy.userservice.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final EmailService emailService;

    public UserController(UserService userService, EmailService emailService){
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/heartBeat")
    @ResponseBody
    public ResponseEntity<String> getHeartBeat() {
        return new ResponseEntity<String>("Status: UP", HttpStatus.OK);
    }

    @GetMapping(value = "/user/{userId}")
    @ResponseBody
    public ResponseEntity<User> getUser(@RequestHeader HttpHeaders headers, @PathVariable String userId) {
        return new ResponseEntity<User>(userService.getUser(userId), HttpStatus.OK);
    }

    @PostMapping(value = "/user")
    @ResponseBody
    public ResponseEntity createUser(@RequestBody Map<String, String> credentials) {
        logger.info("Received request to create user " + credentials);
        //TODO: send email with async request
        //TODO: return success
        User u = userService.createUser(credentials);

        if(u == null) {
            logger.info("User already exists with email: " + credentials.get("email"));
            return new ResponseEntity<String>("User exists", HttpStatus.BAD_REQUEST);
        }

        logger.info("Successfully created record for {} {} with ID {}.", u.getFirstName(), u.getLastName(), u.getUserid());
        if(emailService.sendVerificationEmail(u)) {
            logger.info("Verification email sent to {}", u.getEmail());
        }

        return new ResponseEntity<User>(u, HttpStatus.OK);
    }

    @DeleteMapping(value="/user/{userId}")
    @ResponseBody
    public ResponseEntity deleteUser(@PathVariable String userId) {
        logger.info("Request to delete user {}", userId);
        userService.deleteUser(userId);

        return new ResponseEntity<Boolean>(HttpStatus.OK);
    }

    @GetMapping(value = "/verify/{userId}/verificationCode/{verificationCode}")
    public ResponseEntity<Boolean> verifyUser(@PathVariable String userId, @PathVariable String verificationCode) {
        userService.confirmUser(userId, verificationCode);
        //TODO: Alter anchor tag to go to angular confirmation page that sends a request to this endpoint
        return new ResponseEntity<Boolean>(HttpStatus.OK);
    }

    @PutMapping(value="/verify/{userId}/verificationCode/{verificationCode}")
    public ResponseEntity<Integer> enableUser(@PathVariable String userId, @PathVariable String verificationCode) {
        if(userService.enableUser(userId, verificationCode) == 1) {
            return new ResponseEntity<Integer>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<Integer>(HttpStatus.UNAUTHORIZED);
        }
        //TODO: Alter anchor tag to go to angular confirmation page that sends a request to this endpoint
    }
}