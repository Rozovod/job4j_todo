package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@AllArgsConstructor
public class CrudRepository {
    private final SessionFactory sf;

    public void run(Consumer<Session> command) {
        tx(session -> {
            command.accept(session);
            return null;
        });
    }

    public boolean booleanRun(Function<Session, Boolean> command) {
        return tx(command);
    }

    public boolean booleanRun(String query, Map<String, Object> args) {
        Function<Session, Boolean> command = session -> {
            var sq = session.createQuery(query);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            return sq.executeUpdate() > 0;
        };
        return tx(command);
    }

    public <T> Optional<T> optional(String query, Class<T> cl, Map<String, Object> args) {
        Function<Session, Optional<T>> command = session -> {
            var sq = session.createQuery(query, cl);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            return sq.uniqueResultOptional();
        };
        return tx(command);
    }

    public <T> Optional<T> optional(Function<Session, Optional<T>> command) {
        return tx(command);
    }

    public <T> Collection<T> query(String query, Class<T> cl) {
        Function<Session, Collection<T>> command = session -> session
                .createQuery(query, cl)
                .getResultList();
        return tx(command);
    }

    public <T> Collection<T> query(String query, Class<T> cl, Map<String, Object> args) {
        Function<Session, Collection<T>> command = session -> {
            var sq = session.createQuery(query, cl);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            return sq.getResultList();
        };
        return tx(command);
    }

    public <T> T tx(Function<Session, T> command) {
        Session session = sf.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            T rsl = command.apply(session);
            transaction.commit();
            return rsl;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }
}