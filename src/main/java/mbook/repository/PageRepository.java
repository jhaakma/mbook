package mbook.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import mbook.model.Page;

@Repository
public interface PageRepository extends MongoRepository<Page, String> {

}
