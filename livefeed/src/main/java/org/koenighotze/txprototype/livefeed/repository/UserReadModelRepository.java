package org.koenighotze.txprototype.livefeed.repository;

import org.koenighotze.txprototype.livefeed.model.*;
import org.springframework.data.mongodb.repository.*;

public interface UserReadModelRepository extends MongoRepository<UserReadModel, String> {

}
