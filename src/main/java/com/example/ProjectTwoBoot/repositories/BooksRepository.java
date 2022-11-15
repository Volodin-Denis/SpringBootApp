package com.example.ProjectTwoBoot.repositories;


import com.example.ProjectTwoBoot.models.Book;
import com.example.ProjectTwoBoot.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findAllByOwner(Person owner);
    List<Book> findAllByNameStartingWith(String name);
}