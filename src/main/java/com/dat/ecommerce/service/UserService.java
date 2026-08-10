package com.dat.ecommerce.service;

import com.dat.ecommerce.dto.request.RegisterRequest;
import com.dat.ecommerce.dto.request.UpdateUserRequest;
import com.dat.ecommerce.dto.response.UserResponse;
import com.dat.ecommerce.entity.User;
import com.dat.ecommerce.enums.Role;
import com.dat.ecommerce.exception.EmailAlreadyExistsException;
import com.dat.ecommerce.exception.UserNotFoundException;
import com.dat.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse createUser(RegisterRequest request) {
        if(repository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "Email already exists");
        }
        String encoded = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getName(),
                request.getEmail(),
                encoded,
                Role.USER);

        User savedUser = repository.save(user);

        return new UserResponse(savedUser);
    }


    public List<UserResponse> getAllUsers() {
        return repository.findAll()
                .stream()
                .map(UserResponse::new)
                .toList();
    }

    public UserResponse getUserById(Long id) {

        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        return new UserResponse(user);
    }

    public UserResponse getUserByEmail(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        return new UserResponse(user);
    }


    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        // Update name
        if (request.getName() != null
                && !request.getName().equals(user.getName())) {
            user.setName(request.getName());
        }

        // Update email
        if (request.getEmail() != null
                && !request.getEmail().equals(user.getEmail())) {

            if (repository.existsByEmail(request.getEmail())) {
                throw new EmailAlreadyExistsException(
                        "Email already exists"
                );
            }
            user.setEmail(request.getEmail());
        }

        // Update password
        if (request.getPassword() != null
                && !request.getPassword().isBlank()) {

            user.setPasswordHash(
                    passwordEncoder.encode(request.getPassword())
            );
        }

        User updatedUser = repository.save(user);

        return new UserResponse(updatedUser);
    }

    public void deleteUserById(Long id) {
        if (!repository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }

        repository.deleteById(id);
    }
}
