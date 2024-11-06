package org.example;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentService {
    private static final String URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String USERNAME = "your_username";
    private static final String PASSWORD = "your_password";

    public List<Student> getAllStudents() throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM students");

            List<Student> students = new ArrayList<>();
            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getInt("id"));
                student.setFirstName(resultSet.getString("first_name"));
                student.setLastName(resultSet.getString("last_name"));
                student.setMiddleName(resultSet.getString("middle_name"));
                student.setBirthDate(resultSet.getDate("birth_date").toLocalDate());
                student.setGroupNumber(resultSet.getString("group_number"));
                student.setUniqueNumber(resultSet.getString("unique_number"));

                students.add(student);
            }
            return students;
        }
    }

    public void addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO students(first_name, last_name, middle_name, birth_date, group_number, unique_number)" +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, student.getFirstName());
            preparedStatement.setString(2, student.getLastName());
            preparedStatement.setString(3, student.getMiddleName());
            preparedStatement.setDate(4, Date.valueOf(student.getBirthDate()));
            preparedStatement.setString(5, student.getGroupNumber());
            preparedStatement.setString(6, student.getUniqueNumber());

            preparedStatement.executeUpdate();
        }
    }

    public boolean deleteStudentByUniqueNumber(String uniqueNumber) throws SQLException {
        if (uniqueNumber == null || uniqueNumber.isEmpty()) {
            throw new IllegalArgumentException("Уникальный номер не может быть пустым.");
        }

        String query = "DELETE FROM students WHERE unique_number = ?";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, uniqueNumber);
            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;
        }
    }
}
