package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepository {
    private final SessionFactory sf;

    public Optional<User> save(User user) {
        Optional<User> userOptional = Optional.empty();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            userOptional = Optional.of(user);
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return userOptional;
    }

    public Optional<User> findByLoginAndPassword(String login, String password) {
        Optional<User> userOptional = Optional.empty();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            userOptional = session.createQuery(
                    "from User where login = :uLogin and password = :uPassword", User.class)
                    .setParameter("uLogin", login)
                    .setParameter("uPassword", password)
                    .uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return userOptional;
    }
}
