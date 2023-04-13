package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.TaskRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public void create(Task task) {
        taskRepository.create(task);
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

    public Collection<Task> findAllForUser(User user) {
        return taskRepository.findAllForUser(user);
    }

    public Collection<Task> findAllByStateForUser(boolean state, User user) {
        return taskRepository.findAllByStateForUser(state, user);
    }

    public boolean updateStates(int id) {
        return taskRepository.updateStates(id);
    }
}
