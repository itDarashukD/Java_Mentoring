package com.example.mentoring.repository;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.mentoring.MentoringApplication;
import com.example.mentoring.TestSupporter;
import com.example.mentoring.model.Address;
import com.example.mentoring.model.ExamResult;
import com.example.mentoring.model.Student;
import com.example.mentoring.model.Subject;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@DisplayName("integration tests for interoperability with DB class")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {MentoringApplication.class})
public class StudentRepositoryIT extends TestSupporter {


    private static final ExamResult examResultWithDummySubject = new ExamResult() {{
        setSubject("dummySubject");
        setStudentSurname("dummySurname");
        setMark(1);
    }};

    private Connection connection;

    @BeforeEach
    void setUp() {
        connection = getConnection();
    }

    @DisplayName("addStudent(), check if Student was added to DB")
    @Test
    public void addStudent_dummyStudent_studentAddedToDb() {
        try (PreparedStatement prepareStatement = connection.prepareStatement(INSERT_INTO_STUDENT)) {
	   prepareStatement.setString(1, studentWithDummyName.getName());
	   prepareStatement.setString(2, studentWithDummyName.getSurName());
	   prepareStatement.setString(3, studentWithDummyName.getPhoneNumber());
	   prepareStatement.setString(4, studentWithDummyName.getPrimarySkill());
	   prepareStatement.setDate(5, Date.valueOf(studentWithDummyName.getDateOfBirth()));

	   prepareStatement.execute();
        } catch (SQLException e) {
	   throw new RuntimeException(e);
        }
        Student studentFromDb = getStudentBySurname();

        assertEquals(studentWithDummyName, studentFromDb);
    }

    @DisplayName("findByName(), check if method found correct User in DB")
    @Test
    public void findByName_dummyStudent_studentFoundInDB() {
        Student studentFromDb = new Student();

        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_STUDENT_BY_NAME)) {
	   preparedStatement.setString(1, studentWithDummyName.getName());

	   ResultSet resultSet = preparedStatement.executeQuery();
	   while (resultSet.next()) {
	       studentFromDb.setName(resultSet.getString(2));
	       studentFromDb.setSurName(resultSet.getString(3));
	       studentFromDb.setPhoneNumber(resultSet.getString(4));
	       studentFromDb.setPrimarySkill(resultSet.getString(5));
	       studentFromDb.setDateOfBirth(LocalDate.parse(resultSet.getString(6)));
	   }
        } catch (SQLException e) {
	   throw new RuntimeException(e);
        }
        assertEquals(studentWithDummyName, studentFromDb);
    }

    @DisplayName("findBySurName(), check if method found correct User in DB")
    @Test
    public void findBySurName_dummyStudent_studentFoundInDB() {
        Student studentFromDb = getStudentBySurname();

        assertEquals(studentWithDummyName, studentFromDb);
    }

    @DisplayName("findByPhoneNumber(), check if method found correct User in DB")
    @Test
    public void findByPhoneNumber_dummyStudent_studentFoundInDB() {
        Student studentFromDb = new Student();

        try (PreparedStatement preparedStatement = connection.prepareStatement(
	       GET_STUDENT_BY_PHONE_NUMBER)) {
	   preparedStatement.setString(1, studentWithDummyName.getPhoneNumber());

	   ResultSet resultSet = preparedStatement.executeQuery();
	   while (resultSet.next()) {
	       studentFromDb.setName(resultSet.getString(2));
	       studentFromDb.setSurName(resultSet.getString(3));
	       studentFromDb.setPhoneNumber(resultSet.getString(4));
	       studentFromDb.setPrimarySkill(resultSet.getString(5));
	       studentFromDb.setDateOfBirth(LocalDate.parse(resultSet.getString(6)));
	   }
        } catch (SQLException e) {
	   throw new RuntimeException(e);
        }
        assertEquals(studentWithDummyName, studentFromDb);
    }

    @DisplayName("addSubject(), check if Subject was added to DB")
    @Test
    public void addSubject_dummySubject_subjectAddedToDb() {
        Subject subjectFromDb = new Subject();

        try (PreparedStatement prepareStatement = connection.prepareStatement(INSERT_INTO_SUBJECT)) {
	   prepareStatement.setString(1, subjectWithDummyName.getSubjectName());
	   prepareStatement.setString(2, subjectWithDummyName.getTutorName());

	   prepareStatement.execute();
        } catch (SQLException e) {
	   throw new RuntimeException(e);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_SUBJECT_BY_NAME)) {
	   preparedStatement.setString(1, subjectWithDummyName.getSubjectName());

	   ResultSet resultSet = preparedStatement.executeQuery();
	   while (resultSet.next()) {
	       subjectFromDb.setSubjectName(resultSet.getString(2));
	       subjectFromDb.setTutorName(resultSet.getString(3));
	   }
        } catch (SQLException e) {
	   throw new RuntimeException(e);
        }
        assertEquals(subjectWithDummyName, subjectFromDb);
    }

    @DisplayName("addExamResult(), check if ExamResult was added to DB")
    @Test
    public void addExamResult_dummyExamResult_examResultAddedToDb() {
        try (PreparedStatement prepareStatement = connection.prepareStatement(
	       INSERT_INTO_EXAM_RESULTS)) {
	   prepareStatement.setString(1, examResultWithDummySubject.getStudentSurname());
	   prepareStatement.setString(2, examResultWithDummySubject.getSubject());
	   prepareStatement.setInt(3, examResultWithDummySubject.getMark());

	   prepareStatement.execute();
        } catch (SQLException e) {
	   throw new RuntimeException(e);
        }

        ExamResult examResultFromDb = getExamResultBySurname();

        assertEquals(examResultWithDummySubject, examResultFromDb);
    }

    @DisplayName("findExamResultBySurname(), check if method found correct Student in DB")
    @Test
    public void findExamResultBySurname_dummyStudent_examResultFoundInDB() {
        ExamResult examResultFromDb = getExamResultBySurname();

        assertEquals(examResultWithDummySubject, examResultFromDb);
    }

    @DisplayName("addAddress(), check if  Address was added to DB")
    @Test
    public void addAddress_dummyAddress_addressAddedToDb()  {
        try (PreparedStatement prepareStatement = connection.prepareStatement(
	       INSERT_INTO_STUDENT_ADDRESS)) {
	   prepareStatement.setString(1, addressWithDummySurName.getStudentSurname());
	   prepareStatement.setString(2, addressWithDummySurName.getAddress());

	   prepareStatement.execute();
        } catch (SQLException e) {
	   throw new RuntimeException(e);
        }
        Address addressFromDb = getAddressBySurname();

        assertEquals(addressWithDummySurName, addressFromDb);
    }

    @DisplayName("findAddressBySurname(), check if method found correct Address in DB")
    @Test
    public void findAddressBySurname_dummyAddress_AddressFoundInDB(){
        Address addressFromDb = getAddressBySurname();

        try (PreparedStatement preparedStatement = connection.prepareStatement(
	       GET_ADDRESS_BY_STUDENT_SURNAME)) {
	   preparedStatement.setString(1, addressWithDummySurName.getStudentSurname());

	   ResultSet resultSet = preparedStatement.executeQuery();
	   while (resultSet.next()) {
	       addressFromDb.setStudentSurname(resultSet.getString(2));
	       addressFromDb.setAddress(resultSet.getString(3));
	   }
        } catch (SQLException e) {
	   throw new RuntimeException(e);
        }
        assertEquals(addressWithDummySurName, addressFromDb);
    }

    @DisplayName("updatedAddress(), check if method didn't update Address table in DB, because of created avoiding update trigger ")
    @Test
    public void updatedAddress_dummyAddress_updatedAddressDidNotUpdated() {
        Address initialAddressFromDB = getAddressBySurname();

        try (PreparedStatement prepareStatement = connection.prepareStatement(UPDATE_ADDRESS)) {
	   prepareStatement.setString(1, "updated Address");
	   prepareStatement.setString(2, addressWithDummySurName.getStudentSurname());

	   prepareStatement.executeUpdate();
        } catch (SQLException e) {
	   throw new RuntimeException(e);
        }
        Address updatedAddressFromDB = getAddressBySurname();

        assertEquals(initialAddressFromDB, updatedAddressFromDB);
    }

    private Address getAddressBySurname() {
        Address addressFromDb = new Address();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
	       GET_ADDRESS_BY_STUDENT_SURNAME)) {
	   preparedStatement.setString(1, addressWithDummySurName.getStudentSurname());

	   ResultSet resultSet = preparedStatement.executeQuery();
	   while (resultSet.next()) {
	       addressFromDb.setStudentSurname(resultSet.getString(2));
	       addressFromDb.setAddress(resultSet.getString(3));
	   }
        } catch (SQLException e) {
	   throw new RuntimeException(e);
        }
        return addressFromDb;
    }

    private ExamResult getExamResultBySurname() {
        ExamResult examResultFromDb = new ExamResult();

        try (PreparedStatement preparedStatement = connection.prepareStatement(
	       GET_EXAM_RESULTS_BY_STUDENT_SURNAME)) {
	   preparedStatement.setString(1, examResultWithDummySubject.getStudentSurname());

	   ResultSet resultSet = preparedStatement.executeQuery();
	   while (resultSet.next()) {
	       examResultFromDb.setStudentSurname(resultSet.getString(2));
	       examResultFromDb.setSubject(resultSet.getString(3));
	       examResultFromDb.setMark(resultSet.getInt(4));
	   }
        } catch (SQLException e) {
	   throw new RuntimeException(e);
        }
        return examResultFromDb;
    }

    private Student getStudentBySurname() {
        Student studentFromDb = new Student();

        try (PreparedStatement preparedStatement = connection.prepareStatement(
	       GET_STUDENT_BY_SURNAME)) {
	   preparedStatement.setString(1, studentWithDummyName.getSurName());

	   ResultSet resultSet = preparedStatement.executeQuery();

	   while (resultSet.next()) {
	       studentFromDb.setName(resultSet.getString(2));
	       studentFromDb.setSurName(resultSet.getString(3));
	       studentFromDb.setPhoneNumber(resultSet.getString(4));
	       studentFromDb.setPrimarySkill(resultSet.getString(5));
	       studentFromDb.setDateOfBirth(LocalDate.parse(resultSet.getString(6)));
	   }
        } catch (SQLException e) {
	   throw new RuntimeException(e);
        }
        return studentFromDb;
    }


}