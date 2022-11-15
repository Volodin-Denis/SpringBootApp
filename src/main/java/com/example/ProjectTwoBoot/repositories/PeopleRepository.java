package com.example.ProjectTwoBoot.repositories;


import com.example.ProjectTwoBoot.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Person findByName(String name);
}
