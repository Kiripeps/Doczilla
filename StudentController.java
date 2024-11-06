package org.example;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getAllStudents(HttpServletResponse response) throws SQLException {
        try {
            return studentService.getAllStudents();
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }
    }

    @PostMapping
    public void addStudent(@RequestBody Student student, HttpServletResponse response) throws IOException, SQLException {
        try {
            studentService.addStudent(student);
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{uniqueNumber}")
    public void deleteStudent(@PathVariable String uniqueNumber, HttpServletResponse response) throws IOException, SQLException {
        try {
            if (!studentService.deleteStudentByUniqueNumber(uniqueNumber)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Студента с таким уникальным номером не существует.");
            } else {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        } catch (IllegalArgumentException | SQLException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
}
