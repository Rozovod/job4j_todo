package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.repository.CategoryRepository;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Collection<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Collection<Category> findByIdList(List<Integer> categoriesIds) {
        return categoryRepository.findByIdList(categoriesIds);
    }
}
