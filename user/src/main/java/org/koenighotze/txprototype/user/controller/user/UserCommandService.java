package org.koenighotze.txprototype.user.controller.user;

import javaslang.control.*;
import org.koenighotze.txprototype.user.events.*;
import org.koenighotze.txprototype.user.model.*;
import org.koenighotze.txprototype.user.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.*;
import org.springframework.stereotype.*;

@Component
public class UserCommandService {

    private final UserRepository userRepository;
    private final UserEventProducer userEventProducer;

    @Autowired
    public UserCommandService(UserRepository userRepository, UserEventProducer userEventProducer) {
        this.userRepository = userRepository;
        this.userEventProducer = userEventProducer;
    }

    public Try<User> newUser(String publicId, User userData) {
        User foundUser = userRepository.findByPublicId(publicId);

        if (foundUser != null) {
            return Try.failure(new DuplicateKeyException("User " + publicId + " already exists"));
        }
        userData.setPublicId(publicId);

        User created = userRepository.save(userData);
        userEventProducer.publish(UserCreated.of(created));
        return Try.success(created);
    }

    public Try<User> updateUser(String publicId, User userData) {
        User foundUser = userRepository.findByPublicId(publicId);

        if (foundUser == null) {
            return Try.failure(new RuntimeException("Cannot find user " + publicId));
        }

        foundUser.setFirstname(userData.getFirstname());
        foundUser.setLastname(userData.getLastname());
        foundUser.setEmail(userData.getEmail());

        User updated = userRepository.save(foundUser);
        userEventProducer.publish(UserUpdated.of(publicId, updated));
        return Try.success(updated);
    }

    public boolean deleteUser(String publicId) {
        return Option.of(userRepository.findByPublicId(publicId))
                     .map(user -> {
                         userRepository.delete(user);
                         userEventProducer.publish(UserDeleted.of(user));
                         return true;
                     })
                     .getOrElse(false);
    }

}
