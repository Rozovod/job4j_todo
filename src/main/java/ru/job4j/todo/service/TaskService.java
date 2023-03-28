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

    public void update(Task task) {
        taskRepository.update(task);
    }

    public void delete(int id) {
        taskRepository.delete(id);
    }

    public Optional<Task> findById(int id) {
        return taskRepository.findById(id);
    }

    public Collection<Task> findAll() {
        return taskRepository.findAll();
    }

    public Collection<Task> findAllCompleted() {
        return taskRepository.findAllCompleted();
    }

    public Collection<Task> findAllNew() {
        return taskRepository.findAllNew();
    }

    public void updateStates(Task task) {
        taskRepository.updateStates(task);
    }
}
