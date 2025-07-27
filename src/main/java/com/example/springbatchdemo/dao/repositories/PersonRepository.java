package com.example.springbatchdemo.dao.repositories;

import com.example.springbatchdemo.dao.entities.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person,Long> {
}
