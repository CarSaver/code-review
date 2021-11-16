package com.carsaver.codereview.service;

import com.carsaver.codereview.model.User;
import com.carsaver.codereview.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toMap;

@Service
public class UserService {
    private UserRepository repository;
    private EmailService emailService;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    public List<User> findAll() {
        return repository.findAllByOrderByIdAsc();
    }

    // What is this method for? Since I dont have requirements that would indicate that changing an email is necessary,
    // I would remove the method
    public User updateEmail(User user, String email) {
        try {
            if (user != null) {
                // Not sure this check is needed, since if it is null, it will get generated when it is saved.
                // The usecase for it I think, would be to ensure you are only updating something that has already been
                // created
                if (user.getId() != null)
                    if (email != null) {
                        user.setEmail(email);
                        repository.save(user);
                        // Should this have the same logic based on if the email address contains "@test.com"? If so,
                        // that logic should be moved into the email service class and called from within the sendConfirmation
                        // method
                        emailService.sendConfirmation(email);
                    } else
                        return user;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return user;
    }

    // What is this method for? Since I dont have requirements that would indicate that getting a map of id -> names is needed,
    // I would remove the method
    public Map<Long, String> getNames() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(toMap(User::getId, user -> {
                    return user.getLastName() + ", " + user.getFirstName();
                }));

    }

}
