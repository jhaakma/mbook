package mbook.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import mbook.model.GameCharacter;

@Repository
public interface GameCharacterRepository extends MongoRepository<GameCharacter, String> {
    @Query(value="{ 'owner' : {$regex : ?0, $options: 'i'}, 'name' : ?1 }")
    GameCharacter findByOwnerAndName(String owner, String name);

}