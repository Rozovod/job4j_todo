package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepository {
    private final CrudRepository crudRepository;

    public Optional<User> save(User user) {
        return crudRepository.optional(session -> {
            session.persist(user);
            return Optional.ofNullable(user);
        });
    }

    public Optional<User> findByLoginAndPassword(String login, String password) {
        return crudRepository.optional(
                "from User where login = :uLogin and password = :uPassword", User.class,
                Map.of("uLogin", login, "uPassword", password)
        );
    }
}
