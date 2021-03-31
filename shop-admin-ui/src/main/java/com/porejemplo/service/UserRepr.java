package com.porejemplo.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.porejemplo.persist.model.Role;
import com.porejemplo.persist.model.User;

import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

// DTO
public class UserRepr {

    private Long id;

    @NotEmpty
    private String login;

    @NotEmpty
    private String password;

    @JsonIgnore
    @NotEmpty
    private String matchingPassword;

    private Set<Role> roles;

    public UserRepr() {
    }

    public UserRepr(String login) {
        this.login = login;
    }

    public UserRepr(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.roles = new HashSet<>(user.getRoles());
    }

    public UserRepr(Role role) {
        this.roles = new HashSet<>();
        this.roles.add(role);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRepr userRepr = (UserRepr) o;
        return id.equals(userRepr.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
