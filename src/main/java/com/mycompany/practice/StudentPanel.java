package com.mycompany.practice;

/*
 * ============================================================
 * WHAT THIS FILE DOES:
 *
 * This is the "Students" tab page. It lets the user:
 *   - SEE all students in a table
 *   - ADD a new student (name, email, phone, course)
 *   - UPDATE an existing student's details
 *   - DELETE a student
 *   - CLEAR the form to start fresh
 *
 * It connects to the SQLite database to save/load data.
 * ============================================================
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class StudentPanel extends JPanel {

    /*
     * FIELD VARIABLES
     *
     * txtName, txtEmail, txtPhone = text input fields
     *   where the user types student details
     *
     * cmbCourse = dropdown that lists all available courses
     *   (loaded from the database)
     *
     * table = a JTable that displays all students in a
     *   spreadsheet-like grid
     *
     * tableModel = the "data engine" behind the table.
     *   It holds the actual data and column names.
     *   The table just DISPLAYS whatever the model contains.
     */
    private JTextField txtName, txtEmail, txtPhone;
    private JComboBox<Course> cmbCourse;
    private JTable table;
    private DefaultTableModel tableModel;

    /*
     * Constructor
     * -----------
     * Runs when this panel is created.
     *   1. initComponents() - builds the UI
     *   2. loadCourses()    - fills the course dropdown from DB
     *   3. loadTable()      - loads student data from DB into table
     */
    public StudentPanel() {
        initComponents();
        loadCourses();
        loadTable();
    }

    /*
     * initComponents()
     * ----------------
     * Creates and arranges all the UI widgets.
     *
     * Layout structure:
     *   BorderLayout on the main panel:
     *     NORTH = top section (form fields + buttons)
     *     CENTER = scrollable table
     */
    private void initComponents() {
        /*
         * BorderLayout(10, 10) with 10px gaps.
         * EmptyBorder adds 15px padding around the edges.
         */
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ============================================================
        // FORM PANEL (uses GridBagLayout)
        // ============================================================
        /*
         * GridBagLayout is a flexible grid system.
         * It arranges components in rows and columns, where
         * each cell can have different sizes.
         *
         * GridBagConstraints controls WHERE each component goes
         * and how it behaves (stretching, padding, etc.)
         *
         * Our form has 4 rows and 2 columns:
         *
         *   Row 0:  Label "Name:"  |  [Text Field]
         *   Row 1:  Label "Email:" |  [Text Field]
         *   Row 2:  Label "Phone:" |  [Text Field]
         *   Row 3:  Label "Course:"|  [Dropdown]
         */
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Student Details"));

        /*
         * GridBagConstraints g = new GridBagConstraints()
         * We reuse this object for each component, updating
         * the gridx/gridy to place things in the right cell.
         *
         * insets = margins around each cell (5px each side)
         * fill = HORIZONTAL means the component stretches to
         *   fill the cell's width (but not height)
         */
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.fill = GridBagConstraints.HORIZONTAL;

        // --- Row 0: Name ---
        g.gridx = 0; g.gridy = 0;          // Column 0, Row 0
        formPanel.add(new JLabel("Name:"), g);  // Add label
        g.gridx = 1; g.weightx = 1;        // Column 1, allow horizontal stretching
        txtName = new JTextField(20);      // 20 columns wide
        formPanel.add(txtName, g);

        // --- Row 1: Email ---
        g.gridx = 0; g.gridy = 1; g.weightx = 0;  // Reset stretching
        formPanel.add(new JLabel("Email:"), g);
        g.gridx = 1; g.weightx = 1;
        txtEmail = new JTextField(20);
        formPanel.add(txtEmail, g);

        // --- Row 2: Phone ---
        g.gridx = 0; g.gridy = 2; g.weightx = 0;
        formPanel.add(new JLabel("Phone:"), g);
        g.gridx = 1; g.weightx = 1;
        txtPhone = new JTextField(20);
        formPanel.add(txtPhone, g);

        // --- Row 3: Course ---
        g.gridx = 0; g.gridy = 3; g.weightx = 0;
        formPanel.add(new JLabel("Course:"), g);
        g.gridx = 1; g.weightx = 1;
        cmbCourse = new JComboBox<>();      // Dropdown for courses
        formPanel.add(cmbCourse, g);

        // ============================================================
        // BUTTON PANEL
        // ============================================================
        /*
         * FlowLayout arranges buttons in a row, left to right.
         * When a button is clicked, its action listener runs.
         *
         * btnAdd    → calls addStudent()
         * btnUpdate → calls updateStudent()
         * btnDelete → calls deleteStudent()
         * btnClear  → calls clearForm()
         *
         * Each of these methods is defined later in this file.
         */
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

        btnAdd.addActionListener(e -> addStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnClear.addActionListener(e -> clearForm());

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        // ============================================================
        // TABLE
        // ============================================================
        /*
         * DefaultTableModel
         * -----------------
         * This is the DATA behind the table.
         *
         * Constructor arguments:
         *   String[]{"ID", "Name", ...} = column headers
         *   0 = initial number of rows (0 = empty)
         *
         * We override isCellEditable() to return false.
         * This prevents users from double-clicking cells
         * to edit them directly (we want them to use the
         * form instead).
         */
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Name", "Email", "Phone", "Course", "Enrolled"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        /*
         * JTable = the visual table component.
         * It gets its data from tableModel (the model).
         *
         * setSelectionMode(SINGLE_SELECTION) means only
         * one row can be selected at a time.
         *
         * addListSelectionListener listens for when the
         * user clicks a row. When they do, populateFromTable()
         * fills the form fields with that student's data
         * so they can edit it.
         *
         * getValueIsAdjusting() is a technical detail:
         * it prevents the event from firing twice during
         * a single click. We only react when it's false.
         */
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) populateFromTable();
        });

        // ============================================================
        // LAYOUT ASSEMBLY
        // ============================================================
        /*
         * We combine the form and buttons into a "top panel"
         * using BorderLayout:
         *   CENTER = form fields
         *   SOUTH  = buttons
         *
         * Then we add:
         *   topPanel  → NORTH of the main panel
         *   table     → CENTER of the main panel (in a scroll pane)
         */
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    // ============================================================
    // DATABASE OPERATIONS
    // ============================================================

    /*
     * loadCourses()
     * -------------
     * Reads ALL courses from the database and puts them
     * in the dropdown (JComboBox).
     *
     * removeAllItems() clears the dropdown first.
     * Then we loop through every row in the ResultSet and
     * create a Course object for each, adding it to the
     * combo box.
     *
     * Because Course.toString() returns the course name,
     * the dropdown displays course names to the user.
     *
     * Try-with-resources: Connection, Statement, AND
     * ResultSet are all auto-closed.
     */
    public void loadCourses() {
        cmbCourse.removeAllItems();
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM courses")) {

            /*
             * rs.next() moves to the next row.
             * It returns false when there are no more rows.
             * rs.getInt("id") reads the integer from the
             * column named "id" in the current row.
             */
            while (rs.next()) {
                cmbCourse.addItem(new Course(
                    rs.getInt("id"),
                    rs.getString("course_name"),
                    rs.getString("description"),
                    rs.getInt("credits")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * loadTable()
     * -----------
     * Reads students from the database and fills the table.
     *
     * The SQL query uses a LEFT JOIN:
     *   "SELECT s.id, s.name, s.email, s.phone, c.course_name, s.enrollment_date
     *    FROM students s LEFT JOIN courses c ON s.course_id = c.id"
     *
     * LEFT JOIN means: get ALL students, even if they don't
     * have a course assigned. If no course, course_name is null.
     *
     * tableModel.setRowCount(0) clears the table first.
     * Then for each database row, we add a row to the model.
     */
    public void loadTable() {
        tableModel.setRowCount(0);
        String sql = "SELECT s.id, s.name, s.email, s.phone, c.course_name, s.enrollment_date " +
                     "FROM students s LEFT JOIN courses c ON s.course_id = c.id ORDER BY s.id";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                /*
                 * addRow takes an Object[] array.
                 * Each element corresponds to a column in the table.
                 * The order must match the column headers defined
                 * in the DefaultTableModel constructor.
                 */
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("course_name"),
                    rs.getString("enrollment_date")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * addStudent()
     * ------------
     * Called when the user clicks "Add".
     *
     * Step 1: Get the text from the form fields.
     * Step 2: Validate that name is not empty.
     * Step 3: Get the selected course (if any).
     * Step 4: INSERT a new row into the students table.
     * Step 5: Refresh the table.
     * Step 6: Clear the form.
     *
     * PreparedStatement prevents SQL injection attacks.
     * The ? placeholders are filled in with setString/setObject.
     */
    private void addStudent() {
        String name = txtName.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required.");
            return;
        }
        Course course = (Course) cmbCourse.getSelectedItem();
        String sql = "INSERT INTO students (name, email, phone, course_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, txtEmail.getText().trim());
            stmt.setString(3, txtPhone.getText().trim());
            stmt.setObject(4, course != null ? course.getId() : null);
            stmt.executeUpdate();  // Runs the INSERT

            loadTable();   // Refresh table to show new student
            clearForm();   // Reset form fields
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * updateStudent()
     * ---------------
     * Called when the user clicks "Update".
     *
     * First checks that a row is selected in the table.
     * Gets the student's ID from the selected row (column 0).
     * Updates that student's data in the database.
     * The SQL "UPDATE ... WHERE id=?" ensures we only
     * change the specific student.
     */
    private void updateStudent() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a student from the table.");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        String name = txtName.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required.");
            return;
        }
        Course course = (Course) cmbCourse.getSelectedItem();
        String sql = "UPDATE students SET name=?, email=?, phone=?, course_id=? WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, txtEmail.getText().trim());
            stmt.setString(3, txtPhone.getText().trim());
            stmt.setObject(4, course != null ? course.getId() : null);
            stmt.setInt(5, id);
            stmt.executeUpdate();  // Runs the UPDATE

            loadTable();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * deleteStudent()
     * ---------------
     * Called when the user clicks "Delete".
     *
     * Confirms with the user first (YES/NO dialog).
     * If confirmed, deletes the row from the database
     * using "DELETE FROM ... WHERE id=?"
     */
    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a student from the table.");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(
            this, "Delete this student?", "Confirm", JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM students WHERE id=?")) {

            stmt.setInt(1, id);
            stmt.executeUpdate();  // Runs the DELETE

            loadTable();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * clearForm()
     * -----------
     * Resets all form fields to empty/blank.
     * setSelectedIndex(-1) deselects the course dropdown.
     * clearSelection() deselects any row in the table.
     */
    private void clearForm() {
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        cmbCourse.setSelectedIndex(-1);  // -1 = nothing selected
        table.clearSelection();
    }

    /*
     * populateFromTable()
     * -------------------
     * Called when the user clicks a row in the table.
     * Reads the values from the selected row and fills
     * the form fields so the user can edit them.
     *
     * For the course dropdown, we loop through the items
     * to find the one matching the course name shown in
     * the table.
     */
    private void populateFromTable() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        // Fill text fields from table columns 1-3
        txtName.setText((String) tableModel.getValueAt(row, 1));
        txtEmail.setText((String) tableModel.getValueAt(row, 2));
        txtPhone.setText((String) tableModel.getValueAt(row, 3));

        // Find and select the matching course in the dropdown
        String courseName = (String) tableModel.getValueAt(row, 4);
        for (int i = 0; i < cmbCourse.getItemCount(); i++) {
            if (cmbCourse.getItemAt(i).getCourseName().equals(courseName)) {
                cmbCourse.setSelectedIndex(i);
                return;
            }
        }
        cmbCourse.setSelectedIndex(-1);  // No match found
    }
}

/*
 * ============================================================
 * RECAP: HOW A CRUD OPERATION WORKS (example: Add Student)
 *
 *   1. User types name, email, phone, selects course
 *   2. User clicks "Add"
 *   3. addStudent() runs:
 *      a. Gets values from text fields (txtName.getText())
 *      b. Validates name is not empty
 *      c. Connects to database
 *      d. Runs: INSERT INTO students VALUES (?, ?, ?, ?)
 *      e. Refreshes the table to show new data
 *      f. Clears the form for the next entry
 *
 * CRUD = Create, Read, Update, Delete
 *   Create → INSERT (addStudent)
 *   Read   → SELECT (loadTable)
 *   Update → UPDATE (updateStudent)
 *   Delete → DELETE (deleteStudent)
 * ============================================================
 */
