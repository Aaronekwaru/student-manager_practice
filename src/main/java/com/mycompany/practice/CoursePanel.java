package com.mycompany.practice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CoursePanel extends JPanel {

    private JTextField txtName, txtCredits;
    private JTextArea txtDescription;
    private JTable table;
    private DefaultTableModel tableModel;

    public CoursePanel() {
        initComponents();
        loadTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Course Details"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx = 0; g.gridy = 0;
        formPanel.add(new JLabel("Course Name:"), g);
        g.gridx = 1; g.weightx = 1;
        txtName = new JTextField(20);
        formPanel.add(txtName, g);

        g.gridx = 0; g.gridy = 1; g.weightx = 0;
        formPanel.add(new JLabel("Credits:"), g);
        g.gridx = 1; g.weightx = 1;
        txtCredits = new JTextField(5);
        formPanel.add(txtCredits, g);

        g.gridx = 0; g.gridy = 2; g.weightx = 0;
        g.anchor = GridBagConstraints.NORTH;
        formPanel.add(new JLabel("Description:"), g);
        g.gridx = 1; g.weightx = 1;
        txtDescription = new JTextArea(3, 20);
        txtDescription.setLineWrap(true);
        JScrollPane descScroll = new JScrollPane(txtDescription);
        formPanel.add(descScroll, g);

        // Button panel
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

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Course Name", "Credits", "Description"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) populateFromTable();
        });

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

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

    private void addCourse() {
        String name = txtName.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Course name is required.");
            return;
        }
        int credits = 3;
        try {
            credits = Integer.parseInt(txtCredits.getText().trim());
        } catch (NumberFormatException e) {
            // default to 3
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

    private void deleteCourse() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a course from the table.");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this course?", "Confirm", JOptionPane.YES_NO_OPTION);
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

    private void clearForm() {
        txtName.setText("");
        txtCredits.setText("");
        txtDescription.setText("");
        table.clearSelection();
    }

    private void populateFromTable() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        txtName.setText((String) tableModel.getValueAt(row, 1));
        txtCredits.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        txtDescription.setText((String) tableModel.getValueAt(row, 3));
    }
}
