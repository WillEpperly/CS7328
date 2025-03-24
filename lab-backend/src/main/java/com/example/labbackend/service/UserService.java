package com.example.labbackend.service;

import com.example.labbackend.model.User;
import com.example.labbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    public Optional<User> getUser(String username) {
        return repo.findByUsername(username);
    }

    public void registerUser(User user) {
        repo.save(user);
    }

    public boolean validateLogin(String username, String password) {
        Optional<User> user = repo.findByUsername(username);
        return user.isPresent() && user.get().getPassword().equals(password);
    }
    
    public User updateUser(User updated) {
        Optional<User> existing = repo.findByUsername(updated.getUsername());
        if (existing.isPresent()) {
            User user = existing.get();
            user.setName(updated.getName());
            user.setAddress(updated.getAddress());
            user.setDegree(updated.getDegree());
            return repo.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

}
