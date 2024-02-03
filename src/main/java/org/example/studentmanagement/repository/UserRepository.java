package org.example.studentmanagement.repository;

import org.example.studentmanagement.entity.Lesson;
import org.example.studentmanagement.entity.User;
import org.example.studentmanagement.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findAllByUserType(UserType userType);

    User findByUserType(UserType userType);

    User findByEmailAndLesson(String email, Lesson lesson);
}
