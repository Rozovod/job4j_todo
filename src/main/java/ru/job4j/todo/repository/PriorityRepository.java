package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;

import java.util.Collection;

@Repository
@AllArgsConstructor
public class PriorityRepository {
    private final CrudRepository crudRepository;

    public Collection<Priority> findAll() {
        return crudRepository.query("from Priority", Priority.class);
    }
}
