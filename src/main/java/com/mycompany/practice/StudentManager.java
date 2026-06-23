package com.mycompany.practice;

import javax.swing.*;
import java.awt.*;

public class StudentManager extends JFrame {

    public StudentManager() {
        DatabaseManager.initialize();
        initComponents();
    }

    private void initComponents() {
        setTitle("Student Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 550));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", 0, 14));
        StudentPanel studentPanel = new StudentPanel();
        tabbedPane.addTab("Students", studentPanel);
        tabbedPane.addTab("Courses", new CoursePanel());
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 0) {
                studentPanel.loadCourses();
                studentPanel.loadTable();
            }
        });

        JLabel statusBar = new JLabel("Connected to SQLite database");
        statusBar.setFont(new Font("Segoe UI", 2, 12));
        statusBar.setForeground(new Color(100, 100, 100));
        statusBar.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        add(tabbedPane, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new StudentManager().setVisible(true);
        });
    }
}
