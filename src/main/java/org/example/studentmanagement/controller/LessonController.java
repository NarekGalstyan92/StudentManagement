package org.example.studentmanagement.controller;

import org.example.studentmanagement.entity.Lesson;
import org.example.studentmanagement.entity.User;
import org.example.studentmanagement.enums.UserType;
import org.example.studentmanagement.repository.LessonRepository;
import org.example.studentmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class LessonController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @GetMapping("/lessons")
    public String lessonsPage(ModelMap modelMap) {
        modelMap.addAttribute("lessons", lessonRepository.findAll());
        modelMap.addAttribute("teachers", userRepository.findAllByUserType(UserType.TEACHER));
        return "lessons";
    }

    @GetMapping("/lesson/add")
    public String addLessonPage(ModelMap modelMap) {
        List<User> teacherList = userRepository.findAllByUserType(UserType.TEACHER);
        modelMap.addAttribute("teachers", teacherList);
        return "addLesson";
    }

    @PostMapping("/lesson/add")
    public String addLesson(@ModelAttribute Lesson lesson) {
        lessonRepository.save(lesson);
        return "redirect:/lessons";
    }


    @GetMapping("/lesson/update/{id}")
    public String updateLessonPage(@PathVariable("id") int id, ModelMap modelMap) {
        List<User> userList = userRepository.findAllByUserType(UserType.TEACHER);

        if (!userList.isEmpty()) {
            modelMap.addAttribute("users", userList);
        } else {
            return "redirect:/lessons";
        }
        Optional<Lesson> lessonOptional = lessonRepository.findById(id);

        if (lessonOptional.isPresent()) {
            modelMap.addAttribute("lesson", lessonOptional.get());
        } else {
            return "redirect:/lessons";
        }

        return "updateLesson";
    }

    @PostMapping("/lesson/update")
    public String updateTeachersPage(@ModelAttribute Lesson lesson) {
        if (lesson.getStartDate() == null) {
            lesson.setStartDate(lessonRepository.getLessonById(lesson.getId()).getStartDate());
        }
        lessonRepository.save(lesson);
        return "redirect:/lessons";
    }

    @GetMapping("/lesson/delete/{id}")
    public String deleteLesson(@PathVariable("id") int id) {
        lessonRepository.deleteById(id);
        return "redirect:/lessons";
    }

}
