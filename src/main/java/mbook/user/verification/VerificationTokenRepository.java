package mbook.user.verification;

import org.springframework.data.mongodb.repository.MongoRepository;

import mbook.user.User;

public interface VerificationTokenRepository extends MongoRepository<VerificationToken, String> {
    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);

}
