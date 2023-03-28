package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class TaskRepository {
    private final SessionFactory sf;

    public Task create(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return task;
    }

    public void update(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.update(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public void delete(int id) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE Task WHERE id = :tId")
                    .setParameter("tId", id)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public Optional<Task> findById(int id) {
        Session session = sf.openSession();
        Optional<Task> taskOptional = Optional.empty();
        try {
            session.beginTransaction();
            taskOptional = session.createQuery("from Task where id = :tId", Task.class)
                    .setParameter("tId", id)
                    .uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return taskOptional;
    }

    public Collection<Task> findAll() {
        Session session = sf.openSession();
        Collection<Task> tasks = new ArrayList<>();
        try {
            session.beginTransaction();
            tasks = session.createQuery("from Task", Task.class).getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return tasks;
    }

    public Collection<Task> findAllCompleted() {
        Session session = sf.openSession();
        Collection<Task> tasksDone = new ArrayList<>();
        try {
            session.beginTransaction();
            tasksDone = session.createQuery("from Task as t where t.done = true", Task.class)
                    .getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return tasksDone;
    }

    public Collection<Task> findAllNew() {
        Session session = sf.openSession();
        Collection<Task> tasksNew = new ArrayList<>();
        try {
            session.beginTransaction();
            tasksNew = session.createQuery("from Task as t where t.done = false", Task.class)
                    .getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return tasksNew;
    }

    public void updateStates(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("UPDATE Task SET done = :tDone where id = :tId")
                    .setParameter("tDone", true)
                    .setParameter("tId", task.getId())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }
}
