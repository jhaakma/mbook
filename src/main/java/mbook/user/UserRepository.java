package mbook.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    public User findByEmailIgnoreCase(String email);
    public User findByUsernameIgnoreCase(String email);
    
}
