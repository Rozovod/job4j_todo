package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.TaskRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public Task create(Task task) {
        return taskRepository.create(task);
    }

    public boolean update(Task task) {
        return taskRepository.update(task);
    }

    public boolean delete(int id) {
        return taskRepository.delete(id);
    }

    public Optional<Task> findById(int id) {
        return taskRepository.findById(id);
    }

    public Collection<Task> findAll() {
        return taskRepository.findAll();
    }

    public Collection<Task> findAllByState(boolean state) {
        return taskRepository.findAllByState(state);
    }

    public boolean updateStates(int id) {
        return taskRepository.updateStates(id);
    }
}
