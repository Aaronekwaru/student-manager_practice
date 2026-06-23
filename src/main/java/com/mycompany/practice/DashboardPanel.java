package com.mycompany.practice;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    private JComboBox<String> cmbUsers;
    private JTextArea txtLog;
    private JLabel lblStatus;

    public DashboardPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title
        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Segoe UI", 1, 24));

        // User selection
        JLabel lblUser = new JLabel("Select User:");
        lblUser.setFont(new Font("Segoe UI", 0, 14));

        cmbUsers = new JComboBox<>();
        cmbUsers.setFont(new Font("Segoe UI", 0, 14));
        cmbUsers.setModel(new DefaultComboBoxModel<>(new String[]{"Simon bida", "Miraaa", "Aaron Ka", "Unemi"}));

        JLabel lblMsg = new JLabel("Select your village person...");
        lblMsg.setFont(new Font("Segoe UI", 2, 13));

        JTextField txtMsg = new JTextField("The person u select is the one who is responsible for your problems \uD83D\uDE02\uD83D\uDE02");
        txtMsg.setFont(new Font("Segoe UI", 0, 14));
        txtMsg.setEditable(false);

        // Activity log
        txtLog = new JTextArea(8, 40);
        txtLog.setFont(new Font("Segoe UI", 0, 13));
        txtLog.setText("Welcome! Select a user to begin.\n");
        JScrollPane scrollLog = new JScrollPane(txtLog);

        // Buttons
        JButton btnSubmit = new JButton("Submit");
        JButton btnRefresh = new JButton("Refresh");
        JButton btnExit = new JButton("Exit");

        btnSubmit.addActionListener(e -> {
            String user = (String) cmbUsers.getSelectedItem();
            txtLog.append("Submitted: " + user + "\n");
        });

        btnRefresh.addActionListener(e -> {
            txtLog.setText("");
            txtLog.append("Log refreshed.\n");
        });

        cmbUsers.addActionListener(e -> {
            String user = (String) cmbUsers.getSelectedItem();
            txtLog.append("Selected: " + user + "\n");
        });

        btnExit.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JFrame) {
                ((JFrame) window).dispose();
            }
        });

        // Status
        lblStatus = new JLabel("Status: Ready");
        lblStatus.setFont(new Font("Segoe UI", 2, 12));
        lblStatus.setForeground(new Color(100, 100, 100));

        // Layout using GroupLayout for the top section
        JPanel headerPanel = new JPanel();
        GroupLayout gl = new GroupLayout(headerPanel);
        headerPanel.setLayout(gl);
        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(false);

        gl.setHorizontalGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(title)
            .addGroup(gl.createSequentialGroup()
                .addComponent(lblUser)
                .addComponent(cmbUsers, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
            .addComponent(lblMsg)
            .addComponent(txtMsg)
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
            .addComponent(title)
            .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblUser)
                .addComponent(cmbUsers, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
            .addComponent(lblMsg)
            .addComponent(txtMsg, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        );

        // Button panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnPanel.add(btnSubmit);
        btnPanel.add(btnRefresh);
        btnPanel.add(btnExit);

        // Assemble
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(headerPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollLog, BorderLayout.CENTER);
        add(lblStatus, BorderLayout.SOUTH);
    }
}
