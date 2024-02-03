package org.example.studentmanagement.repository;

import org.example.studentmanagement.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    Lesson getLessonById(int id);
}
