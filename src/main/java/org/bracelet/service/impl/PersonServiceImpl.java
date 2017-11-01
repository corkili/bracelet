package org.bracelet.service.impl;

import org.bracelet.entity.Person;
import org.bracelet.repository.PersonRepository;
import org.bracelet.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 李浩然 on 2017/8/7.
 */
@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    public Long savePerson() {
        Person person = new Person();
        person.setUsername("李浩然");
        person.setPhone("18275394839");
        person.setAddress("贵州省贵阳市花溪区");
        person.setRemark("I'm a student.");
        Long id =  personRepository.save(person);
        person = personRepository.get(id);
        person.setRemark("test");
        personRepository.saveOrUpdate(person);
        return id;
    }
}
