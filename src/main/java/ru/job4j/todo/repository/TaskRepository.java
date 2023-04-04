package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

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
                "from Task t JOIN FETCH t.priority where t.id = :tId", Task.class, Map.of("tId", id));
    }

    public Collection<Task> findAll() {
        return crudRepository.query("from Task t JOIN FETCH t.priority", Task.class);
    }

    public Collection<Task> findAllByState(boolean state) {
        return crudRepository.query(
                "from Task as t JOIN FETCH t.priority where t.done = :tState", Task.class,
                Map.of("tState", state)
        );
    }

    public boolean updateStates(int id) {
        return crudRepository.booleanRun("UPDATE Task SET done = :tDone where id = :tId",
                Map.of("tDone", true, "tId", id));
    }
}
