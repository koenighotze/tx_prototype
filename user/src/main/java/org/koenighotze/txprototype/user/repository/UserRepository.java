package org.koenighotze.txprototype.user.repository;

import org.koenighotze.txprototype.user.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author David Schmitz
 */
public interface UserRepository extends MongoRepository<User, String> {
    User findByPublicId(String publicId);
}
