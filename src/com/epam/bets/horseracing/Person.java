/*
 * Person
 *
 * Version 1.0
 *
 * Date 30 Jan 2020
 */

package com.epam.bets.horseracing;

import java.util.Objects;

/**
 * Person class.
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
public class Person {
    /**
     * Person id
     */
    private int id;
    /**
     * Person username
     */
    private String username;
    /**
     * Person email
     */
    private String email;
    /**
     * Person password
     */
    private String password;

    /**
     * Create a new person.
     *
     * @param username person username
     * @param email person email
     * @param password person password
     */
    public Person(String username, String email, String password) {
        id = -1;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * @return person id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id person id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return person username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param firstName person username
     */
    public void setUsername(String firstName) {
        this.username = username;
    }

    /**
     * @return person email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email person email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return person password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password person password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id &&
                username.equals(person.username) &&
                email.equals(person.email) &&
                password.equals(person.password);
    }

    @Override
    public int hashCode() {
        return id + username.hashCode() * 18 + email.hashCode() + password.hashCode();
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", username='" + username +
                "', email='" + email +
                "', password='" + password +
                "'}";
    }
}
