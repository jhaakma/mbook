package mbook.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import mbook.model.User;
import mbook.model.VerificationToken;

public interface VerificationTokenRepository extends MongoRepository<VerificationToken, String> {
    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);

}
