package mbook.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import mbook.model.GameCharacter;

@Repository
public interface GameCharacterRepository extends MongoRepository<GameCharacter, String> {
}
