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

    public boolean update(Task task) {
        boolean result = false;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            int affectedRows = session.createQuery(
                            "UPDATE Task SET title = :tTitle, description = :tDescription,"
                                    + "done = :tDone WHERE id = :tId")
                    .setParameter("tTitle", task.getTitle())
                    .setParameter("tDescription", task.getDescription())
                    .setParameter("tDone", task.isDone())
                    .setParameter("tId", task.getId())
                    .executeUpdate();
            session.getTransaction().commit();
            result = affectedRows > 0;
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    public boolean delete(int id) {
        boolean result = false;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            int affectedRows = session.createQuery("DELETE Task WHERE id = :tId")
                    .setParameter("tId", id)
                    .executeUpdate();
            session.getTransaction().commit();
            result = affectedRows > 0;
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
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

    public Collection<Task> findAllByState(boolean state) {
        Session session = sf.openSession();
        Collection<Task> result = new ArrayList<>();
        try {
            session.beginTransaction();
            result = session.createQuery("from Task as t where t.done = :tState", Task.class)
                    .setParameter("tState", state)
                    .getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    public boolean updateStates(int id) {
        boolean result = false;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            int affectedRows = session.createQuery("UPDATE Task SET done = :tDone where id = :tId")
                    .setParameter("tDone", true)
                    .setParameter("tId", id)
                    .executeUpdate();
            session.getTransaction().commit();
            result = affectedRows > 0;
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }
}
