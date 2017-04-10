package org.koenighotze.txprototype.user.repository;

import org.koenighotze.txprototype.user.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByPublicId(String publicId);
}
