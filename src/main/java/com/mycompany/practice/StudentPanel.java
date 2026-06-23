package com.mycompany.practice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class StudentPanel extends JPanel {

    private JTextField txtName, txtEmail, txtPhone;
    private JComboBox<Course> cmbCourse;
    private JTable table;
    private DefaultTableModel tableModel;

    public StudentPanel() {
        initComponents();
        loadCourses();
        loadTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Student Details"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx = 0; g.gridy = 0;
        formPanel.add(new JLabel("Name:"), g);
        g.gridx = 1; g.weightx = 1;
        txtName = new JTextField(20);
        formPanel.add(txtName, g);

        g.gridx = 0; g.gridy = 1; g.weightx = 0;
        formPanel.add(new JLabel("Email:"), g);
        g.gridx = 1; g.weightx = 1;
        txtEmail = new JTextField(20);
        formPanel.add(txtEmail, g);

        g.gridx = 0; g.gridy = 2; g.weightx = 0;
        formPanel.add(new JLabel("Phone:"), g);
        g.gridx = 1; g.weightx = 1;
        txtPhone = new JTextField(20);
        formPanel.add(txtPhone, g);

        g.gridx = 0; g.gridy = 3; g.weightx = 0;
        formPanel.add(new JLabel("Course:"), g);
        g.gridx = 1; g.weightx = 1;
        cmbCourse = new JComboBox<>();
        formPanel.add(cmbCourse, g);

        // Button panel
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

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Phone", "Course", "Enrolled"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) populateFromTable();
        });

        // Layout assembly
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadCourses() {
        cmbCourse.removeAllItems();
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM courses")) {
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

    private void loadTable() {
        tableModel.setRowCount(0);
        String sql = "SELECT s.id, s.name, s.email, s.phone, c.course_name, s.enrollment_date " +
                     "FROM students s LEFT JOIN courses c ON s.course_id = c.id ORDER BY s.id";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
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
            stmt.executeUpdate();
            loadTable();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
            stmt.executeUpdate();
            loadTable();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a student from the table.");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this student?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM students WHERE id=?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            loadTable();
            clearForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearForm() {
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        cmbCourse.setSelectedIndex(-1);
        table.clearSelection();
    }

    private void populateFromTable() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        txtName.setText((String) tableModel.getValueAt(row, 1));
        txtEmail.setText((String) tableModel.getValueAt(row, 2));
        txtPhone.setText((String) tableModel.getValueAt(row, 3));

        String courseName = (String) tableModel.getValueAt(row, 4);
        for (int i = 0; i < cmbCourse.getItemCount(); i++) {
            if (cmbCourse.getItemAt(i).getCourseName().equals(courseName)) {
                cmbCourse.setSelectedIndex(i);
                return;
            }
        }
        cmbCourse.setSelectedIndex(-1);
    }
}
