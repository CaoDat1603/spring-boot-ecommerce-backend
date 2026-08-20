package com.dat.ecommerce.service;

import com.dat.ecommerce.dto.request.RegisterRequest;
import com.dat.ecommerce.dto.request.UpdateUserRequest;
import com.dat.ecommerce.dto.request.UserFilterRequest;
import com.dat.ecommerce.dto.response.UserResponse;
import com.dat.ecommerce.entity.User;
import com.dat.ecommerce.enums.Role;
import com.dat.ecommerce.exception.EmailAlreadyExistsException;
import com.dat.ecommerce.exception.UserNotFoundException;
import com.dat.ecommerce.repository.UserRepository;
import com.dat.ecommerce.specification.UserSpecification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Transactional(readOnly = true)
    public Page<UserResponse> getUsers(
            String email,
            UserFilterRequest filter,
            Pageable pageable

    ) {
        User user = repository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found"
                        )
                );

        Specification<User> specification =
                (root, query, cb) -> null;

        if (user.getRole() == Role.USER) {
            return Page.empty();
        }

        if (filter.getId() != null) {
            specification = specification.and(
                    UserSpecification.hasUserID(
                            filter.getId()
                    )
            );
        }

        if (filter.getName() != null) {
            specification = specification.and(
                    UserSpecification.hasName(
                            filter.getName()
                    )
            );
        }

        if (filter.getEmail() != null) {
            specification = specification.and(
                    UserSpecification.hasEmail(
                            filter.getEmail()
                    )
            );
        }

        if (filter.getRole() != null) {
            specification = specification.and(
                    UserSpecification.hasRole(
                            filter.getRole()
                    )
            );
        }

        if (filter.getCreatedFrom() != null) {
            specification = specification.and(
                    UserSpecification.createdAtGreaterThanOrEqual(
                            filter.getCreatedFrom()
                    )
            );
        }

        if (filter.getCreatedTo() != null) {
            specification = specification.and(
                    UserSpecification.createdAtLessThanOrEqual(
                            filter.getCreatedTo()
                    )
            );
        }

        Page<User> users =
                repository.findAll(
                        specification,
                        pageable
                );

        return users.map(UserResponse::new);
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
