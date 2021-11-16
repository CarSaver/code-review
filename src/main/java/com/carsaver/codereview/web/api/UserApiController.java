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

@RestController
public class UserApiController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ZipCodeLookupService zipCodeLookupService;

    @GetMapping("/users/create")
    public User createuser(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email) {
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
