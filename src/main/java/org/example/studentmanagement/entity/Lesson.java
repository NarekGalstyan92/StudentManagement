package org.example.studentmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "lesson")
@Data
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private int duration;
    private double price;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startDate;

    @OneToMany(mappedBy = "lesson")
    private List<User> userList;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;
}
