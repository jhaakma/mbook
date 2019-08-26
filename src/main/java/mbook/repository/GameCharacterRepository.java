package mbook.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import mbook.morrowind.model.CharacterRecord;

@Repository
public interface GameCharacterRepository extends MongoRepository<CharacterRecord, String> {
    @Query(value="{ 'owner' : {$regex : ?0, $options: 'i'}, 'gameCharacter.name' : ?1 }")
    public CharacterRecord findByOwnerAndName(String owner, String name);
}