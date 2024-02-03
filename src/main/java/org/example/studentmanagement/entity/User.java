package org.example.studentmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.studentmanagement.enums.UserType;

@Entity
@Table(name = "user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private String email;
    private String picName;
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @ManyToOne(fetch = FetchType.EAGER)
    private Lesson lesson;

}
