package com.mycompany.practice;

public class dashboard extends javax.swing.JFrame {

    public dashboard() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabelTitle = new javax.swing.JLabel();
        jLabelUser = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaLog = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jButtonAdd = new javax.swing.JButton();
        jButtonRefresh = new javax.swing.JButton();
        jButtonView = new javax.swing.JButton();
        jButtonExit = new javax.swing.JButton();
        jLabelStatus = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Dashboard");

        jLabelTitle.setFont(new java.awt.Font("Segoe UI", 1, 24));
        jLabelTitle.setText("Dashboard");

        jLabelUser.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabelUser.setText("Select User:");

        jComboBox1.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Simon bida", "Miraaa", "Aaron Ka", "Unemi" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 13));
        jLabel1.setText("Select your village person...");

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jTextField1.setText("The person u select is the one who is responsible for your problems \ud83d\ude02\ud83d\ude02");
        jTextField1.setEnabled(false);

        jSeparator1.setForeground(new java.awt.Color(180, 180, 180));

        jTextAreaLog.setColumns(20);
        jTextAreaLog.setFont(new java.awt.Font("Segoe UI", 0, 13));
        jTextAreaLog.setRows(6);
        jTextAreaLog.setText("Welcome! Select a user to see their details.\n");
        jScrollPane1.setViewportView(jTextAreaLog);

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jButton1.setText("Submit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButtonAdd.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jButtonAdd.setText("Add User");
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        jButtonRefresh.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jButtonRefresh.setText("Refresh");
        jButtonRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefreshActionPerformed(evt);
            }
        });

        jButtonView.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jButtonView.setText("View Details");
        jButtonView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewActionPerformed(evt);
            }
        });

        jButtonExit.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jButtonExit.setText("Exit");
        jButtonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExitActionPerformed(evt);
            }
        });

        jLabelStatus.setFont(new java.awt.Font("Segoe UI", 2, 12));
        jLabelStatus.setForeground(new java.awt.Color(100, 100, 100));
        jLabelStatus.setText("Status: Ready");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTitle)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelUser)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonRefresh)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonView)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                        .addComponent(jButtonExit))
                    .addComponent(jLabelStatus))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitle)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelUser)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAdd)
                    .addComponent(jButtonRefresh)
                    .addComponent(jButtonView)
                    .addComponent(jButton1)
                    .addComponent(jButtonExit))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelStatus)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {
        String selected = (String) jComboBox1.getSelectedItem();
        jTextAreaLog.append("Selected: " + selected + "\n");
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        String selected = (String) jComboBox1.getSelectedItem();
        jTextAreaLog.append("Submitted: " + selected + "\n");
    }

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {
        jTextAreaLog.append("Add User clicked.\n");
    }

    private void jButtonRefreshActionPerformed(java.awt.event.ActionEvent evt) {
        jTextAreaLog.setText("");
        jTextAreaLog.append("Log refreshed.\n");
    }

    private void jButtonViewActionPerformed(java.awt.event.ActionEvent evt) {
        String selected = (String) jComboBox1.getSelectedItem();
        jTextAreaLog.append("Details for: " + selected + "\n");
    }

    private void jButtonExitActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new dashboard().setVisible(true);
        });
    }

    public javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonExit;
    private javax.swing.JButton jButtonRefresh;
    private javax.swing.JButton jButtonView;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JLabel jLabelUser;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTextAreaLog;
    private javax.swing.JTextField jTextField1;
}
