package com.porejemplo.persist.repo;

import com.porejemplo.persist.model.User;
import org.springframework.data.jpa.domain.Specification;

public final class UserSpecification {

    public static Specification<User> loginLike(String login) {
        return (root, query, cb) -> cb.like(root.get("login"), "%" + login + "%");
    }

}
