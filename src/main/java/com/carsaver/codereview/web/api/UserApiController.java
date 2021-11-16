package com.carsaver.codereview.web.api;

import com.carsaver.codereview.model.User;
import com.carsaver.codereview.repository.UserRepository;
import com.carsaver.codereview.service.EmailService;
import com.carsaver.codereview.service.ZipCodeLookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

// Switch from @Controller to @RestController because there are no thymyleaf templates.  The alternative would be to
// create those templates
@RestController
public class UserApiController {

    //I would typically have the repository only visible by the service layer, and have the service available in the
    // controllers, but there is no real business logic in the service layer that is used (there is business logic,
    // but those methods are unused), so for consistency with the other controller, I am exposing the repository
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ZipCodeLookupService zipCodeLookupService;

    @GetMapping("/users/create")
    public User createUser(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email) {
        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);

        if (!email.contains("@test.com")) {
            newUser.setEnabled(true);
        }

        User user = userRepository.save(newUser);

        if (user.isEnabled()) {
            emailService.sendConfirmation(email);
        }

        return user;
    }

    /**
     * updates user's address
     *
     * @param id      - assume valid existing id
     * @param zipCode - assume valid zipCode
     * @param city    - assume valid if present otherwise null
     * @return updated User
     */
    @GetMapping("/users/updateLocation")
    public User updateUserLocation(@RequestParam Long id, @RequestParam String zipCode, @RequestParam(required = false) String city) {
        User user = userRepository.findById(id).orElseThrow();

        //Validate Zipcode
        try {
            Integer.parseInt(zipCode);
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Zip Codes are 5 digits");
        }
        if (zipCode.length() != 5)
            throw new RuntimeException("Zip Codes are 5 digits");

        user.setZipCode(zipCode);

        // Only run the zipCodeLookup if the city is not present
        if (Optional.ofNullable(city).isPresent()) {
            user.setCity(city);
        } else {
            user.setCity(zipCodeLookupService.lookupCityByZip(zipCode));
        }

        return userRepository.save(user);
    }

    @GetMapping("/users/delete")
    public void deleteUser(@RequestParam String userid) {
        userRepository.deleteById(Long.parseLong(userid));
    }
}
