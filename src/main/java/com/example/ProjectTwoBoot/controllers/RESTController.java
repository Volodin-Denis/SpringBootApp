package com.example.ProjectTwoBoot.controllers;

import com.example.ProjectTwoBoot.models.Person;
import com.example.ProjectTwoBoot.services.PeopleService;
import com.example.ProjectTwoBoot.util.PersonErrorResponse;
import com.example.ProjectTwoBoot.util.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RESTController {
    private final PeopleService peopleService;

    @Autowired
    public RESTController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping("/people/index")
    public List<Person> getAllPeople(){
        return peopleService.findAll();
    }

    @GetMapping("/people/{id}")
    public Person getPerson(@PathVariable int id){
        Optional<Person> person = Optional.ofNullable(peopleService.findOne(id));
        return person.orElseThrow(PersonNotFoundException::new);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e){
        PersonErrorResponse response = new PersonErrorResponse("Person with this id was not found",System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
