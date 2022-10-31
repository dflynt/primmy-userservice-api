package org.dflynt.primmy.userservice.services;

import net.bytebuddy.utility.RandomString;
import org.dflynt.primmy.userservice.models.User;
import org.dflynt.primmy.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserService(){}

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(String userId) {
        return userRepository.findByuserid(userId);
    }

    public User createUser(Map<String, String> credentials) {
        User u = userRepository.findByemail(credentials.get("email"));
        if (u == null) {
            String uuid = UUID.randomUUID().toString();
            String verificationCode = RandomString.make(36);
            u = new User(uuid,
                    credentials.get("firstName"),
                    credentials.get("lastName"),
                    credentials.get("email"),
                    credentials.get("password"),
                    credentials.get("institution"),
                    new Date(),
                    credentials.get("field"),
                    credentials.get("focus"),
                    false,
                    verificationCode
            );
            String hashedPw = BCrypt.hashpw(u.getPassword(), BCrypt.gensalt());
            u.setPassword(hashedPw);
            userRepository.save(u);

            return u;
        } else {
            return null;
        }
    }

    public void deleteUser(String userId) {
        userRepository.deleteByuserid(userId);
    }

    public boolean confirmUser(String userId, String verificationCode) {
        User u = userRepository.findByuseridAndVerificationCode(userId, verificationCode);
        if(u == null || u.isEnabled()) {
            return false;
        }
        else {
            u.setEnabled(true);
            userRepository.save(u);

            return true;
        }
    }

    public int enableUser(String userId, String verificationCode) {
        return userRepository.enableUser(userId, verificationCode);
    }
}
