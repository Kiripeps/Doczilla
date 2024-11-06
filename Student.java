package org.example;

import java.time.LocalDate;
import java.util.Date;

public class Student {
    private int id;
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate birthDate;
    private String groupNumber;
    private String uniqueNumber;

    // Конструкторы


    //Геттеры
    public int getId() {
        return id;
    }

    public String getBirthDate() {
        return birthDate.toString();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getUniqueNumber() {
        return uniqueNumber;
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setUniqueNumber(String uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }

    //
}