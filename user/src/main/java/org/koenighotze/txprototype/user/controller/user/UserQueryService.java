package org.koenighotze.txprototype.user.controller.user;

import io.vavr.collection.*;
import io.vavr.control.*;
import org.koenighotze.txprototype.user.model.*;
import org.koenighotze.txprototype.user.repository.*;
import org.springframework.stereotype.*;

@Component
public class UserQueryService {
    private final UserRepository userRepository;

    public UserQueryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers() {
        return List.ofAll(userRepository.findAll());
    }

    public Option<User> findByPublicId(String publicId) {
        return Option.of(userRepository.findByPublicId(publicId));
    }
}
