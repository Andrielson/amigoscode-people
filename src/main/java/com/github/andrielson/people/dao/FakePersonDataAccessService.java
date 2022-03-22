package com.github.andrielson.people.dao;

import com.github.andrielson.people.model.Person;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("fakeDao")
public class FakePersonDataAccessService implements PersonDao {

    private static final List<Person> DB = new ArrayList<>();

    @Override
    public int insertPerson(UUID id, Person person) {
        DB.add(new Person(id, person.getName()));
        return 1;
    }

    @Override
    public List<Person> selectAllPeople() {
        return DB;
    }

    @Override
    public int deletePersonById(UUID id) {
        var optionalPerson = selectPersonById(id);
        if (optionalPerson.isEmpty()){
            return 0;
        }
        DB.remove(optionalPerson.get());
        return 1;
    }

    @Override
    public int updatePersonById(UUID id, Person updatedPerson) {
        return selectPersonById(id).map(p -> {
            var indexOfPersonToUpdate = DB.indexOf(p);
            if (indexOfPersonToUpdate >= 0) {
                DB.set(indexOfPersonToUpdate, new Person(id, updatedPerson.getName()));
                return 1;
            }
            return 0;
        }).orElse(0);
    }

    @Override
    public Optional<Person> selectPersonById(final UUID id) {
        return DB.stream().filter(person -> person.getId().equals(id)).findFirst();
    }
}
