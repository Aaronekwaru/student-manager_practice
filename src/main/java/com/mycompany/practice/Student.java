package com.mycompany.practice;

public class Student {
    private int id;
    private String name;
    private String email;
    private String phone;
    private int courseId;
    private String courseName;
    private String enrollmentDate;

    public Student() {}

    public Student(int id, String name, String email, String phone, int courseId, String enrollmentDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.courseId = courseId;
        this.enrollmentDate = enrollmentDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public String getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(String enrollmentDate) { this.enrollmentDate = enrollmentDate; }
}
