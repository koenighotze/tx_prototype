package org.koenighotze.txprototype.user.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Represents a user.
 *
 * @author David Schmitz
 */
public class User {
    @Id
    private String userId;
    private String firstname;
    private String lastname;
    private String username;
    private String email;

    @JsonCreator
    public User(String firstname, String lastname, String username, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", userId)
            .append("username", username)
            .append("email", email)
            .append("firstname", firstname)
            .append("lastname", lastname)
            .toString();
    }
}
