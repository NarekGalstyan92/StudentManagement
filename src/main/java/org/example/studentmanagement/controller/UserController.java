package org.example.studentmanagement.controller;

import org.example.studentmanagement.entity.Lesson;
import org.example.studentmanagement.entity.User;
import org.example.studentmanagement.enums.UserType;
import org.example.studentmanagement.repository.LessonRepository;
import org.example.studentmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Value("/Users/narekgalstyan/Documents/IdeaProjects/StudentManagement/pictures")
    private String uploadDirectory;

    @GetMapping("/teachers")
    public String teachersPage(ModelMap modelMap) {
        List<User> teachers = userRepository.findAllByUserType(UserType.TEACHER);
        List<Lesson> lessons = lessonRepository.findAll();
        modelMap.addAttribute("teachers", teachers);
        modelMap.addAttribute("lessons", lessons);
        return "teachers";
    }

    @GetMapping("/teacher/add")
    public String addTeachersPage(ModelMap modelMap) {
        modelMap.addAttribute("lessons", lessonRepository.findAll());
        return "addTeacher";
    }

    @PostMapping("teacher/add")
    public String addTeachersPage(@ModelAttribute User user,
                                  @RequestParam("picture") MultipartFile multipartFile,
                                  @RequestParam("lesson.id") Lesson lesson,
                                  RedirectAttributes redirectAttributes) throws IOException {
        User byEmailAndLesson = userRepository.findByEmailAndLesson(user.getEmail(), lesson);
        if (byEmailAndLesson != null) {
            redirectAttributes.addFlashAttribute("msg", "Teacher with same email and lesson already exist!");
            return "redirect:/teachers";
        }
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            File file = new File(uploadDirectory, picName);
            multipartFile.transferTo(file);
            User userFromDB = userRepository.findByUserType(UserType.TEACHER);
            userFromDB.setPicName(picName);
        }
        user.setUserType(UserType.TEACHER);
        user.setLesson(lessonRepository.getLessonById(lesson.getId()));
        userRepository.save(user);
        return "redirect:/teachers";
    }

    @GetMapping("/teacher/delete/{id}")
    public String deleteTeacher(@PathVariable("id") int id) {
        userRepository.deleteById(id);
        return "redirect:/teachers";
    }

    @GetMapping("/teacher/update/{id}")
    public String updateTeacherPage(@PathVariable("id") int id, ModelMap modelMap) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            modelMap.addAttribute("teacher", user);
            modelMap.addAttribute("lessons", lessonRepository.findAll());
        } else {
            return "redirect:/teachers";
        }
        return "updateTeacher";
    }

    @PostMapping("/teacher/update")
    public String updateTeachersPage(@ModelAttribute User user,
                                     @RequestParam("picture") MultipartFile multipartFile,
                                     @RequestParam("lesson.id") Lesson lesson) throws IOException {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            File file = new File(uploadDirectory, picName);
            multipartFile.transferTo(file);
            user.setPicName(picName);
        } else {
            Optional<User> fromDB = userRepository.findById(user.getId());
            user.setPicName(fromDB.get().getPicName());
        }
        user.setUserType(UserType.TEACHER);
        user.setLesson(lessonRepository.getLessonById(lesson.getId()));
        userRepository.save(user);
        return "redirect:/teachers";
    }

    @GetMapping("/teacher/image/delete")
    public String deleteTeacherImage(@RequestParam("id") int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty()) {
            return "redirect:/teachers";
        } else {
            User user = byId.get();
            String picName = user.getPicName();
            if (picName != null) {
                user.setPicName(null);
                userRepository.save(user);
                File file = new File(uploadDirectory, picName);
                if (file.exists()) {
                    file.delete();
                }
            }
            return "redirect:/teacher/update/" + user.getId();
        }
    }

    @GetMapping("/students")
    public String studentsPage(ModelMap modelMap) {
        List<User> students = userRepository.findAllByUserType(UserType.STUDENT);
        List<Lesson> lessons = lessonRepository.findAll();
        modelMap.addAttribute("students", students);
        modelMap.addAttribute("lessons", lessons);
        return "students";
    }

    @GetMapping("/student/add")
    public String addStudentPage(ModelMap modelMap) {
        modelMap.addAttribute("lessons", lessonRepository.findAll());
        return "addStudent";
    }

    @PostMapping("student/add")
    public String addStudentPage(@ModelAttribute User user,
                                 @RequestParam("picture") MultipartFile multipartFile,
                                 @RequestParam("lesson.id") Lesson lesson) throws IOException {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            File file = new File(uploadDirectory, picName);
            multipartFile.transferTo(file);
            User userFromDB = userRepository.findByUserType(UserType.STUDENT);
            userFromDB.setPicName(picName);
        }
        user.setUserType(UserType.STUDENT);
        user.setLesson(lessonRepository.getLessonById(lesson.getId()));
        userRepository.save(user);
        return "redirect:/students";
    }

    @GetMapping("/student/delete/{id}")
    public String deleteStudent(@PathVariable("id") int id) {
        userRepository.deleteById(id);
        return "redirect:/students";
    }

    @GetMapping("/student/update/{id}")
    public String updateStudentPage(@PathVariable("id") int id, ModelMap modelMap) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            modelMap.addAttribute("student", user);
            modelMap.addAttribute("lessons", lessonRepository.findAll());
        } else {
            return "redirect:/students";
        }
        return "updateStudent";
    }

    @PostMapping("/student/update")
    public String updateStudentsPage(@ModelAttribute User user,
                                     @RequestParam("picture") MultipartFile multipartFile,
                                     @RequestParam("lesson.id") Lesson lesson) throws IOException {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            File file = new File(uploadDirectory, picName);
            multipartFile.transferTo(file);
            user.setPicName(picName);
        } else {
            Optional<User> fromDB = userRepository.findById(user.getId());
            user.setPicName(fromDB.get().getPicName());
        }
        user.setUserType(UserType.STUDENT);
        user.setLesson(lessonRepository.getLessonById(lesson.getId()));
        userRepository.save(user);
        return "redirect:/students";
    }

    @GetMapping("/student/image/delete")
    public String deleteStudentImage(@RequestParam("id") int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty()) {
            return "redirect:/students";
        } else {
            User user = byId.get();
            String picName = user.getPicName();
            if (picName != null) {
                user.setPicName(null);
                userRepository.save(user);
                File file = new File(uploadDirectory, picName);
                if (file.exists()) {
                    file.delete();
                }
            }
            return "redirect:/student/update/" + user.getId();
        }
    }
}
