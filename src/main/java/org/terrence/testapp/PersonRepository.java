package org.terrence.testapp;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.terrence.testapp.Person;

@Repository
public interface PersonRepository extends MongoRepository<Person, String> {
}