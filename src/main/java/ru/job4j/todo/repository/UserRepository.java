package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepository {
    private final CrudRepository crudRepository;
    private static final Logger LOG = LoggerFactory.getLogger(UserRepository.class);

    public Optional<User> save(User user) {
        Optional<User> userOptional = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(user));
            userOptional = Optional.of(user);
        } catch (Exception e) {
            LOG.error("Пользователь с таким лошином уже существует", e);
        }
        return userOptional;
    }

    public Optional<User> findByLoginAndPassword(String login, String password) {
        return crudRepository.optional(
                "from User where login = :uLogin and password = :uPassword", User.class,
                Map.of("uLogin", login, "uPassword", password)
        );
    }
}
