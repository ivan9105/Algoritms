package com.patterns.structural.proxy;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProxyUsage {
    /**
     * proxy дополняет логику при этом
     * мы работаем с ним как с обычным объектом ничего не подозревая
     */
    public static void main(String[] args) {
        ProxyUsage executor = new ProxyUsage();
        executor.execute();
    }

    private void execute() {
        PersonCache cache = new PersonCache();
        PersonService personService = new PersonService();

        PersonServiceProxy proxy = PersonServiceProxy.builder()
                .internal(personService)
                .cache(cache)
                .build();

        proxy.findByPassportInfo("4245", "567312");
        proxy.findByPassportInfo("4245", "567312");
    }
}

@Data
@Builder
class PersonServiceProxy extends PersonService {
    PersonCache cache;
    PersonService internal;

    @Override
    Person findByPassportInfo(String passportSerial, String passportNumber) {
        Optional<Person> personOptional = cache.findByPassportInfo(passportSerial, passportNumber);
        if (personOptional.isPresent()) {
            System.out.println("Get person from cache");
            return personOptional.get();
        }
        System.out.println("Get person from database");
        return cache.put(internal.findByPassportInfo(passportSerial, passportNumber));
    }
}

class PersonService {
    Person findByPassportInfo(String passportSerial, String passportNumber) {
        //very long operation
        return Person.builder()
                .passportSerial(passportSerial)
                .passportNumber(passportNumber)
                .build();
    }
}

@Data
@Builder
class Person {
    String firstName;
    String lastName;
    String middleName;
    String passportSerial;
    String passportNumber;
}

class PersonCache {
    private List<Person> cache = new ArrayList<>();

    Optional<Person> findByPassportInfo(String passportSerial, String passportNumber) {
        return cache.stream().filter(person ->
                person.passportSerial.equals(passportSerial) &&
                        person.passportNumber.equals(passportNumber)).findFirst();
    }

    Person put(Person person) {
        cache.add(person);
        return person;
    }
}