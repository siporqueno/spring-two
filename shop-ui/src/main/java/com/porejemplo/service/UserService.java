package com.porejemplo.service;

import com.porejemplo.persist.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findUserByLogin(String login);

}
