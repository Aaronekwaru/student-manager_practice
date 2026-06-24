package com.mycompany.practice;

/*
 * ============================================================
 * WHAT THIS FILE DOES:
 *
 * This is a "model" class (also called a POJO - Plain Old
 * Java Object). It simply HOLDS DATA about a student.
 *
 * It doesn't have any logic or buttons or database code.
 * Its only job is to carry data between different parts
 * of the program.
 *
 * Think of it like a paper form with fields:
 *   Name: _______
 *   Email: ______
 *   Phone: ______
 *   Course: _____
 *
 * We fill in the fields here and pass the form around.
 * ============================================================
 */
public class Student {

    /*
     * These are called "fields" or "properties".
     * They store the actual data for ONE student.
     *
     * Each field is "private", meaning only code inside
     * this class can access them directly. To read or
     * write them from outside, we use "getter" and
     * "setter" methods (see below).
     */
    private int id;               // Unique student ID (from database)
    private String name;          // Student's full name
    private String email;         // Email address
    private String phone;         // Phone number
    private int courseId;         // ID of the course they're in
    private String courseName;    // Name of the course (from JOIN query)
    private String enrollmentDate; // When they enrolled (from database)

    /*
     * Constructor (no arguments)
     * ---------------------------
     * Creates an "empty" student object.
     * Useful when we want to create a Student and then
     * set each field individually using setter methods.
     *
     * Example:
     *   Student s = new Student();
     *   s.setName("John");
     *   s.setEmail("john@email.com");
     */
    public Student() {}

    /*
     * Constructor (with all fields)
     * ------------------------------
     * Creates a Student and fills in ALL fields at once.
     * This is convenient when we read data from the
     * database and want to create a Student object in
     * one line of code.
     *
     * The "this" keyword refers to the object being created.
     * So "this.id = id" means: "set THIS object's id field
     * to the value passed as the id parameter".
     */
    public Student(int id, String name, String email, String phone, int courseId, String enrollmentDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.courseId = courseId;
        this.enrollmentDate = enrollmentDate;
    }

    /*
     * GETTER and SETTER methods
     * --------------------------
     *
     * Getters (getId, getName, etc.) return the value of
     * a private field so other classes can READ it.
     *
     * Setters (setId, setName, etc.) let other classes
     * WRITE a new value to a private field.
     *
     * This pattern is called "encapsulation" - we hide
     * the internal data and only expose controlled ways
     * to access it.
     */

    // --- ID ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    // --- Name ---
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    // --- Email ---
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // --- Phone ---
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    // --- Course ID (number) ---
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    /*
     * Course Name
     * -----------
     * This field is NOT stored directly in the students
     * table. Instead, it comes from a SQL JOIN query
     * that combines the students and courses tables.
     *
     * For example, the query:
     *   SELECT s.name, c.course_name
     *   FROM students s JOIN courses c
     *   ON s.course_id = c.id
     *
     * This lets us display "Math 101" instead of just "3".
     */
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    // --- Enrollment Date ---
    public String getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(String enrollmentDate) { this.enrollmentDate = enrollmentDate; }
}
