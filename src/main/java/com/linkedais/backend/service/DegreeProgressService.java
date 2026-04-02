package com.linkedais.backend.service;

import com.linkedais.backend.model.User;
import com.linkedais.backend.model.Course;
import com.linkedais.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

@Service
public class DegreeProgressService {

    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> getDegreeProgressByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return calculateProgress(user);
    }

    public Map<String, Object> getDegreeProgressByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return calculateProgress(user);
    }

    private Map<String, Object> calculateProgress(User user) {
        List<Course> allCourses = user.getCourses();

        int totalCredits = allCourses.stream()
                .mapToInt(Course::getCredits)
                .sum();

        int completedCredits = 0;
        List<Long> completedCourseIds = user.getCompletedCourseIds();

        if (completedCourseIds != null && !completedCourseIds.isEmpty()) {
            completedCredits = allCourses.stream()
                    .filter(course -> completedCourseIds.contains(course.getId()))
                    .mapToInt(Course::getCredits)
                    .sum();
        }

        int remainingCredits = totalCredits - completedCredits;
        double percentage = totalCredits == 0 ? 0 :
                (double) completedCredits / totalCredits * 100;

        Map<String, Object> result = new HashMap<>();
        result.put("completedCredits", completedCredits);
        result.put("totalCredits", totalCredits);
        result.put("remainingCredits", remainingCredits);
        result.put("percentage", percentage);

        return result;
    }
}