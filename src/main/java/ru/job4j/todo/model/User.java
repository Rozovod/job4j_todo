package ru.job4j.todo.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "todo_users")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    private String name;
    @EqualsAndHashCode.Include
    private String login;
    private String password;
}
