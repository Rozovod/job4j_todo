package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class CategoryRepository {
    private final CrudRepository crudRepository;

    public Collection<Category> findAll() {
        return crudRepository.query("from Category", Category.class);
    }

    public Collection<Category> findByIdList(List<Integer> categoriesIds) {
        return crudRepository.query("from Category where id IN :cId", Category.class,
                Map.of("cId", categoriesIds));
    }
}
