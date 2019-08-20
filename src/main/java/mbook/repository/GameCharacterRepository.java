package mbook.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import mbook.model.GameCharacter;

@Repository
public interface GameCharacterRepository extends MongoRepository<GameCharacter, String> {
    @Query(value="{ 'ownerId' : ?0, 'name' : ?1 }")
    public GameCharacter findByOwnerAndName(String owner, String name);
}
 