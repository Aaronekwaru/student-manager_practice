package com.mycompany.practice;

/*
 * ============================================================
 * WHAT THIS FILE DOES:
 *
 * This is the "Courses" tab page. It lets the user:
 *   - SEE all courses in a table
 *   - ADD a new course (name, credits, description)
 *   - UPDATE an existing course
 *   - DELETE a course
 *   - CLEAR the form
 *
 * It follows the SAME pattern as StudentPanel, but
 * operates on the "courses" table instead of "students".
 * ============================================================
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CoursePanel extends JPanel {

    /*
     * txtName      = input for course name
     * txtCredits   = input for credit hours
     * txtDescription = multi-line input for course description
     * table        = shows all courses in a grid
     * tableModel   = data behind the table
     */
    private JTextField txtName, txtCredits;
    private JTextArea txtDescription;
    private JTable table;
    private DefaultTableModel tableModel;

    /*
     * Constructor
     *   1. Build the UI
     *   2. Load courses from DB into table
     */
    public CoursePanel() {
        initComponents();
        loadTable();
    }

    /*
     * initComponents()
     * ----------------
     * Same layout pattern as StudentPanel:
     *   NORTH = form fields + buttons
     *   CENTER = scrollable table
     *
     * GridBagLayout for the form with 3 rows, 2 columns:
     *   Row 0: "Course Name:" | [text field]
     *   Row 1: "Credits:"     | [text field]
     *   Row 2: "Description:" | [text area (scrollable)]
     */
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ============================================================
        // FORM PANEL
        // ============================================================
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Course Details"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Course Name
        g.gridx = 0; g.gridy = 0;
        formPanel.add(new JLabel("Course Name:"), g);
        g.gridx = 1; g.weightx = 1;
        txtName = new JTextField(20);
        formPanel.add(txtName, g);

        // Row 1: Credits
        g.gridx = 0; g.gridy = 1; g.weightx = 0;
        formPanel.add(new JLabel("Credits:"), g);
        g.gridx = 1; g.weightx = 1;
        txtCredits = new JTextField(5);
        formPanel.add(txtCredits, g);

        // Row 2: Description
        g.gridx = 0; g.gridy = 2; g.weightx = 0;
        g.anchor = GridBagConstraints.NORTH;  // Align to top of cell
        formPanel.add(new JLabel("Description:"), g);
        g.gridx = 1; g.weightx = 1;

        /*
         * JTextArea is a multi-line text input.
         * (3, 20) = 3 rows visible, 20 columns wide.
         * setLineWrap(true) means long text wraps to next line
         * instead of going off-screen.
         *
         * We wrap it in a JScrollPane so it scrolls if text
         * exceeds the visible area.
         */
        txtDescription = new JTextArea(3, 20);
        txtDescription.setLineWrap(true);
        JScrollPane descScroll = new JScrollPane(txtDescription);
        formPanel.add(descScroll, g);

        // ============================================================
        // BUTTON PANEL
        // ============================================================
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

        btnAdd.addActionListener(e -> addCourse());
        btnUpdate.addActionListener(e -> updateCourse());
        btnDelete.addActionListener(e -> deleteCourse());
        btnClear.addActionListener(e -> clearForm());

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        // ============================================================
        // TABLE
        // ============================================================
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Course Name", "Credits", "Description"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) populateFromTable();
        });

        // ============================================================
        // ASSEMBLY
        // ============================================================
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    /*
     * loadTable()
     * -----------
     * Reads all courses from the database and fills the table.
     *
     * SQL: "SELECT * FROM courses ORDER BY id"
     * ORDER BY id ensures they appear in the order they
     * were added (1, 2, 3...).
     */
    public void loadTable() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM courses ORDER BY id")) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("course_name"),
                    rs.getInt("credits"),
                    rs.getString("description")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * addCourse()
     * -----------
     * INSERTs a new course into the database.
     *
     * Validates that course name is not empty.
     * Tries to parse credits as an integer.
     * If parsing fails (NumberFormatException), defaults to 3.
     */
    private void addCourse() {
        String name = txtName.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Course name is required.");
            return;
        }

        /*
         * Parse credits from text field.
         * If the user typed something that's not a number,
         * Integer.parseInt() throws NumberFormatException.
         * We catch it and default to 3 credits.
         */
        int credits = 3;
        try {
            credits = Integer.parseInt(txtCredits.getText().trim());
        } catch (NumberFormatException e) {
            // Keep default value of 3
        }

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT INTO courses (course_name, description, credits) VALUES (?, ?, ?)")) {

            stmt.setString(1, name);
            stmt.setString(2, txtDescription.getText().trim());
            stmt.setInt(3, credits);
            stmt.executeUpdate();

            loadTable();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * updateCourse()
     * --------------
     * UPDATEs the selected course in the database.
     *
     * Gets the course ID from the selected table row (column 0).
     * Uses a parameterized UPDATE query to change only that course.
     */
    private void updateCourse() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a course from the table.");
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        String name = txtName.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Course name is required.");
            return;
        }

        int credits = 3;
        try {
            credits = Integer.parseInt(txtCredits.getText().trim());
        } catch (NumberFormatException e) {}

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "UPDATE courses SET course_name=?, description=?, credits=? WHERE id=?")) {

            stmt.setString(1, name);
            stmt.setString(2, txtDescription.getText().trim());
            stmt.setInt(3, credits);
            stmt.setInt(4, id);
            stmt.executeUpdate();

            loadTable();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * deleteCourse()
     * --------------
     * DELETEs the selected course from the database.
     * Prompts the user for confirmation first.
     *
     * IMPORTANT: If a student is assigned to this course,
     * the student's course_id will become null (since we
     * didn't specify ON DELETE CASCADE). The student record
     * itself is NOT deleted.
     */
    private void deleteCourse() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a course from the table.");
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(
            this, "Delete this course?", "Confirm", JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM courses WHERE id=?")) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

            loadTable();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * clearForm()
     * -----------
     * Resets all form fields to empty.
     */
    private void clearForm() {
        txtName.setText("");
        txtCredits.setText("");
        txtDescription.setText("");
        table.clearSelection();
    }

    /*
     * populateFromTable()
     * -------------------
     * Fills the form fields from the selected table row.
     * Allows the user to edit and then Update.
     */
    private void populateFromTable() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        txtName.setText((String) tableModel.getValueAt(row, 1));
        txtCredits.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        txtDescription.setText((String) tableModel.getValueAt(row, 3));
    }
}

/*
 * ============================================================
 * COMPARISON: StudentPanel vs CoursePanel
 *
 * Both follow the SAME structure:
 *   Form fields (GridBagLayout)
 *     → Buttons (FlowLayout)
 *       → Table (JTable with DefaultTableModel)
 *
 * Both have CRUD methods:
 *   addXxx()    → INSERT
 *   loadXxx()   → SELECT
 *   updateXxx() → UPDATE
 *   deleteXxx() → DELETE
 *
 * The difference is just which database table they operate on
 * and what fields they display.
 * ============================================================
 */
