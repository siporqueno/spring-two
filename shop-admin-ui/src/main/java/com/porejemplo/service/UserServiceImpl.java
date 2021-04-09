package com.porejemplo.service;

import com.porejemplo.controller.repr.UserRepr;
import com.porejemplo.persist.model.User;
import com.porejemplo.persist.repo.UserRepository;
import com.porejemplo.persist.repo.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserRepr> findAll() {
        return userRepository.findAll().stream()
                .map(UserRepr::new)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserRepr> findWithFilter(String loginFilter, Integer page, Integer size, String sortField) {
        Specification<User> spec = Specification.where(null);
        if (loginFilter != null && !loginFilter.isBlank()) {
            spec = spec.and(UserSpecification.loginLike(loginFilter));
        }

        if (sortField != null && !sortField.isBlank()) {
            return userRepository.findAll(spec, PageRequest.of(page, size, Sort.by(sortField)))
                    .map(UserServiceImpl::mapToReprWithoutRoles);
        }
        return userRepository.findAll(spec, PageRequest.of(page, size))
                .map(UserServiceImpl::mapToReprWithoutRoles);
    }

    @Transactional
    @Override
    public Optional<UserRepr> findById(long id) {
        return userRepository.findById(id)
                .map(UserRepr::new);
    }

    @Transactional
    @Override
    public void save(UserRepr user) {
        User userToSave = new User(user.getId(), user.getLogin(), user.getPassword(), user.getRoles());
        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));
        userRepository.save(userToSave);
        if (user.getId() == null) {
            user.setId(userToSave.getId());
        }
    }

    @Transactional
    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    private static UserRepr mapToReprWithoutRoles(User u) {
        return new UserRepr(
                u.getId(),
                u.getLogin());
    }
}
