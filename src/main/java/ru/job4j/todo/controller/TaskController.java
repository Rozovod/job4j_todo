package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;

import java.time.ZoneId;
import java.util.List;
import java.util.TimeZone;

@Controller
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final PriorityService priorityService;
    private final CategoryService categoryService;

    @GetMapping({"/", "/index"})
    public String getAll(Model model, @SessionAttribute User user) {
        var tasks = taskService.findAll();
        tasks.forEach(t -> setTimeZone(t, user));
        model.addAttribute("tasks", tasks);
        return "tasks/list";
    }

    @GetMapping("/completed")
    public String getCompleted(Model model, @SessionAttribute User user) {
        var completedTasks = taskService.findAllByState(true);
        completedTasks.forEach(t -> setTimeZone(t, user));
        model.addAttribute("tasks", completedTasks);
        return "tasks/list";
    }

    @GetMapping("/new")
    public String getNew(Model model, @SessionAttribute User user) {
        var newTasks = taskService.findAllByState(false);
        newTasks.forEach(t -> setTimeZone(t, user));
        model.addAttribute("tasks", newTasks);
        return "tasks/list";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task, @SessionAttribute User user,
                         @RequestParam("categoriesId") List<Integer> categoriesId, Model model) {
        try {
            List<Category> categories = (List<Category>) categoryService.findByIdList(categoriesId);
            task.setUser(user);
            task.setCategories(categories);
            taskService.create(task);
            model.addAttribute("message", "Задание добавлено успешно!");
            return "tasks/success";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id, @SessionAttribute User user) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Задание не найдено");
            return "errors/404";
        }
        setTimeZone(taskOptional.get(), user);
        model.addAttribute("task", taskOptional.get());
        return "tasks/one";
    }

    @GetMapping("/update/{id}")
    public String getUpdatePage(Model model, @PathVariable int id) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Задание не найдено");
            return "errors/404";
        }
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("task", taskOptional.get());
        return "tasks/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Task task, @SessionAttribute User user,
                         @RequestParam("categoriesId") List<Integer> categoriesId, Model model) {
        List<Category> categories = (List<Category>) categoryService.findByIdList(categoriesId);
        task.setUser(user);
        task.setCategories(categories);
        var isUpdated = taskService.update(task);
        if (!isUpdated) {
            model.addAttribute("message", "Ошибка. Задание не обновлено");
            return "errors/404";
        }
        model.addAttribute("message", "Задание обновлено успешно");
        return "tasks/success";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        var isDeleted = taskService.delete(id);
        if (!isDeleted) {
            model.addAttribute("message", "Ошибка. Задание не удалено");
            return "errors/404";
        }
        model.addAttribute("message", "Задание удалено успешно");
        return "tasks/success";
    }

    @GetMapping("/state/{id}")
    public String updateState(Model model, @PathVariable int id) {
        var isUpdatedState = taskService.updateStates(id);
        if (!isUpdatedState) {
            model.addAttribute("message", "Ошибка. Статус задания не обновлен");
            return "errors/404";
        }
        model.addAttribute("message", "Статус задания изменен на Выполнено");
        return "tasks/success";
    }

    private static void setTimeZone(Task task, User user) {
        var defTz = TimeZone.getDefault().toZoneId();
        var userTimeZone = ZoneId.of(user.getTimezone());
        var dateTime = task.getCreated()
                .atZone(defTz)
                .withZoneSameInstant(userTimeZone)
                .toLocalDateTime();
        task.setCreated(dateTime);
    }
}
