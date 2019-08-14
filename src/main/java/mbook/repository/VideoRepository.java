package mbook.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import mbook.model.Video;

@Repository
public interface VideoRepository extends MongoRepository<Video, String> {
    Video findByIndex( String index );
}
