package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class TaskRepository {
    private final CrudRepository crudRepository;

    public void create(Task task) {
        crudRepository.run(session -> session.persist(task));
    }

    public boolean update(Task task) {
        return crudRepository.booleanRun(session -> {
            session.merge(task);
            return true;
        });
    }

    public boolean delete(int id) {
        return crudRepository.booleanRun("DELETE Task WHERE id = :tId", Map.of("tId", id));
    }

    public Optional<Task> findById(int id) {
        return crudRepository.optional(
                "from Task t JOIN FETCH t.priority JOIN FETCH t.categories where t.id = :tId",
                Task.class, Map.of("tId", id));
    }

    public Collection<Task> findAllForUser(User user) {
        return crudRepository.query("SELECT DISTINCT t from Task t JOIN FETCH t.priority "
                + "JOIN FETCH t.categories JOIN t.user u "
                + "WHERE u.id = :userId  ORDER BY t.id ASC", Task.class,
                Map.of("userId", user.getId())
        );
    }

    public Collection<Task> findAllByStateForUser(boolean state, User user) {
        return crudRepository.query(
                "SELECT DISTINCT t from Task as t JOIN FETCH t.priority "
                        + "JOIN FETCH t.categories JOIN t.user u "
                        + "where t.done = :tState AND u.id = :userId ORDER BY t.id ASC",
                Task.class, Map.of("tState", state, "userId", user.getId())
        );
    }

    public boolean updateStates(int id) {
        return crudRepository.booleanRun("UPDATE Task SET done = :tDone where id = :tId",
                Map.of("tDone", true, "tId", id));
    }
}
