package com.mycompany.practice;

/*
 * ============================================================
 * WHAT THIS FILE DOES:
 *
 * Like Student.java, this is a "model" class. It holds data
 * about ONE course.
 *
 * A Course has:
 *   - id          (unique number)
 *   - courseName  (e.g. "Mathematics 101")
 *   - description (what the course is about)
 *   - credits     (how many credit hours it's worth)
 * ============================================================
 */
public class Course {

    /*
     * Private fields - data for one course.
     * Only accessible through getters/setters.
     */
    private int id;                // Unique course ID
    private String courseName;     // Course title
    private String description;    // Course description
    private int credits;           // Credit hours (default 3)

    /*
     * Empty constructor - creates a blank course.
     * Useful when we'll fill in the details later.
     */
    public Course() {}

    /*
     * Full constructor - creates a course with all
     * its information at once.
     */
    public Course(int id, String courseName, String description, int credits) {
        this.id = id;
        this.courseName = courseName;
        this.description = description;
        this.credits = credits;
    }

    // --- Getters and Setters ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    /*
     * toString()
     * ----------
     *
     * This method is SPECIAL. Every Java class has it.
     * It returns a "string representation" of the object.
     *
     * When we put Course objects in a JComboBox (dropdown),
     * Java calls toString() on each item to decide what
     * text to display.
     *
     * Without this override, it would show something like:
     *   com.mycompany.practice.Course@1a2b3c4
     *
     * With this override, it shows the course name like:
     *   Mathematics 101
     *
     * The @Override annotation tells Java: "I know this
     * method already exists in the parent class, but I
     * want to replace it with my own version."
     */
    @Override
    public String toString() {
        return courseName;
    }
}
