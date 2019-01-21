package io.github.daniloarcidiacono.hazelcast.monitor.sample.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Person implements Serializable {
    private String name;
    private String surname;
    private Address address;
    private List<String> contacts = new ArrayList<>();

    public Person() {
    }

    public Person contact(final String contact) {
        contacts.add(contact);
        return this;
    }

    public String getName() {
        return name;
    }

    public Person setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public Person setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public Address getAddress() {
        return address;
    }

    public Person setAddress(Address address) {
        this.address = address;
        return this;
    }

    public List<String> getContacts() {
        return contacts;
    }

    public Person setContacts(List<String> contacts) {
        this.contacts = contacts;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name) &&
                Objects.equals(surname, person.surname) &&
                Objects.equals(address, person.address) &&
                Objects.equals(contacts, person.contacts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, address, contacts);
    }
}
