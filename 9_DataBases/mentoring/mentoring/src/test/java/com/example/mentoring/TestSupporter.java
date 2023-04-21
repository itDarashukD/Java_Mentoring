package com.example.mentoring;

import com.example.mentoring.model.Address;
import com.example.mentoring.model.ExamResult;
import com.example.mentoring.model.Student;
import com.example.mentoring.model.Subject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class TestSupporter {

    protected static final int DUMMY_AGE_10 = 10;
    protected static final int DUMMY_AGE_15 = 15;
    protected static final int DUMMY_AGE_20 = 20;
    protected static final int DUMMY_AGE_55 = 55;
    protected static final String DUMMY_NAME_ULAD = "Ulad";
    protected static final String DUMMY_NAME_OLGA = "Olga";
    protected static final String INSERT_INTO_TABLE = "INSERT INTO izolation (name, age) VALUES ( ?, ?) ;";
    protected static final String GET_AGE_FOR_NAME = "SELECT age FROM izolation WHERE name = ? ;";
    protected static final String GET_SUM_OF_AGE_FOR_TABLE = "SELECT SUM (age) FROM izolation ;";
    protected static final String GET_ROWS_COUNT_BY_AGE = "SELECT count (*) FROM izolation WHERE age > ? ";
    protected static final String UPDATE_AGE_FOR_NAME = "UPDATE izolation SET age = ? WHERE name = ? ;";
    protected static final String DELETE_BY_NAME = "DELETE FROM izolation WHERE name = ? ;";
    protected static final String INSERT_INTO_STUDENT = "INSERT INTO public.student(name, surname, phone_number, primary_skill, date_of_birth) VALUES (?, ?, ?, ?, ?);";
    protected static final String INSERT_INTO_SUBJECT = "INSERT INTO subjects (subject_name, tutor_name) VALUES (?, ?);";
    protected static final String GET_STUDENT_BY_SURNAME = "SELECT * FROM student WHERE surname = ? LIMIT 1;";
    protected static final String GET_STUDENT_BY_NAME = "SELECT * FROM student WHERE name = ? LIMIT 1;";
    protected static final String GET_STUDENT_BY_PHONE_NUMBER = "SELECT * FROM student WHERE phone_number = ? LIMIT 1;";
    protected static final String GET_SUBJECT_BY_NAME = "SELECT * FROM subjects WHERE subject_name = ? LIMIT 1;";
    protected static final String GET_EXAM_RESULTS_BY_STUDENT_SURNAME = "SELECT * FROM exam_result WHERE student_surname = ? LIMIT 1;";
    protected static final String INSERT_INTO_EXAM_RESULTS = "INSERT INTO exam_result(student_surname, subject, mark) VALUES ( ? ,? ,?) ;";
    protected static final String INSERT_INTO_STUDENT_ADDRESS = "INSERT INTO student_address (student_surname, address) VALUES (?, ?) ;";
    protected static final String GET_ADDRESS_BY_STUDENT_SURNAME = "SELECT * FROM student_address WHERE student_surname = ? LIMIT 1;";
    protected static final String UPDATE_ADDRESS = "UPDATE student_address SET address= ? WHERE student_surname= ? ;";
    protected static final String FAKE_SURNAME = "fakeSurName";

    protected static final Student studentWithDummyName = new Student() {{
        setName("dummyName");
        setSurName("dummySurname");
        setPhoneNumber("111111");
        setPrimarySkill("dummyPrimarySkill");
        setDateOfBirth(LocalDate.parse("1986-11-11"));
    }};

    protected static final Subject subjectWithDummyName = new Subject() {{
        setSubjectName("dummySubjectName");
        setTutorName("dummyTutorName");
    }};

    protected static final ExamResult resultWithDummySubject = new ExamResult() {{
        setSubject("dummySubject");
        setStudentSurname("dummySurname");
        setMark(1);
    }};

    protected static final Address addressWithDummySurName = new Address() {{
        setStudentSurname("dummySurname");
        setAddress("dummyAddress");
    }};

    protected static final List<Student> studentListWithDummyStudent = new ArrayList<>() {{
        add(studentWithDummyName);
    }};

    protected static final List<ExamResult> examResultListWithDummyExam = new ArrayList<>() {{
        add(resultWithDummySubject);
    }};

    @Autowired
    private DataSource dataSource;

    protected void revertDBInInitialState() {
        Connection connection = getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_AGE_FOR_NAME)) {
	   preparedStatement.setInt(1, DUMMY_AGE_10);
	   preparedStatement.setString(2, DUMMY_NAME_ULAD);
	   preparedStatement.executeUpdate();
        } catch (SQLException e) {
	   throw new RuntimeException(e);
        }
    }

    protected void revertDBInInitialStateForIsolation() {
        Connection connection = getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_NAME)) {
	   preparedStatement.setString(1, DUMMY_NAME_OLGA);
	   preparedStatement.execute();
        } catch (SQLException e) {
	   throw new RuntimeException(e);
        }
    }

    protected Connection getConnection() {
        try {
	   return dataSource.getConnection();
        } catch (SQLException e) {
	   throw new RuntimeException(e);
        }
    }


}
