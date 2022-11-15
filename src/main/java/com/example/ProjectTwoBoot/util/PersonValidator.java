package com.example.ProjectTwoBoot.util;


import com.example.ProjectTwoBoot.models.Person;
import com.example.ProjectTwoBoot.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        Optional<Person> personWithSameName = peopleService.getPersonByName(person.getName());
        if(personWithSameName.isPresent()){
            if(personWithSameName.get().getId()!=person.getId()){
                errors.rejectValue("name","","Человек с таким именем уже существует");
            }
        }
    }
}
