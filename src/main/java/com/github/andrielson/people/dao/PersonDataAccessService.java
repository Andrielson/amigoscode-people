package com.github.andrielson.people.dao;

import com.github.andrielson.people.model.Person;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("postgres")
public class PersonDataAccessService implements PersonDao {

    private final JdbcTemplate jdbcTemplate;

    public PersonDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertPerson(UUID id, Person person) {
        var sql = "INSERT INTO person(id,name) VALUES (?,?)";
        var params = new Object[]{id, person.getName()};
        return jdbcTemplate.update(sql, params);
    }

    @Override
    public List<Person> selectAllPeople() {
        var sql = "SELECT id, name FROM person";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            var id = UUID.fromString(rs.getString("id"));
            var name = rs.getString("name");
            return new Person(id, name);
        });
    }

    @Override
    public Optional<Person> selectPersonById(UUID id) {
        var sql = "SELECT id, name FROM person WHERE id = ?";
        var params = new Object[]{id};
        var person = jdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> {
            var uuid = UUID.fromString(rs.getString("id"));
            var name = rs.getString("name");
            return new Person(uuid, name);
        });
        return Optional.ofNullable(person);
    }

    @Override
    public int deletePersonById(UUID id) {
        var sql = "DELETE FROM person WHERE id = ?";
        var params = new Object[]{id};
        return jdbcTemplate.update(sql, params);
    }

    @Override
    public int updatePersonById(UUID id, Person person) {
        var sql = "UPDATE person SET name = ? WHERE id = ?";
        var params = new Object[]{person.getName(), id};
        return jdbcTemplate.update(sql, params);
    }
}
