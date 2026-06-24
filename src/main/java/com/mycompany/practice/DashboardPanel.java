package com.mycompany.practice;

/*
 * ============================================================
 * WHAT THIS FILE DOES:
 *
 * This is a "Home" or "Welcome" page for the app.
 * It shows:
 *   - A title "Dashboard"
 *   - A dropdown to select a user
 *   - A funny message about "village person"
 *   - An activity log (text area) that records what you do
 *   - Buttons: Submit, Refresh, Exit
 *   - A status label
 *
 * Unlike dashboard.java (which is a JFrame), this one is a
 * JPanel. That means it can be PLACED INSIDE another container
 * like a JTabbedPane. A JPanel is not a window - it's a piece
 * of a window.
 * ============================================================
 */

import javax.swing.*;
import java.awt.*;

/*
 * DashboardPanel extends JPanel
 * ------------------------------
 * JPanel is a generic container. It can hold other components
 * (buttons, labels, text areas, etc.) and arrange them using
 * a "layout manager".
 *
 * Because it extends JPanel, we can add this object directly
 * to a JTabbedPane as a tab.
 */
public class DashboardPanel extends JPanel {

    /*
     * These are FIELD variables (instance variables).
     * They are declared at the class level so they can be
     * accessed from any method in this class.
     *
     * cmbUsers = a dropdown (JComboBox) showing user names
     * txtLog   = a text area that shows activity history
     * lblStatus = a label showing current status
     */
    private JComboBox<String> cmbUsers;
    private JTextArea txtLog;
    private JLabel lblStatus;

    /*
     * Constructor
     * Just calls initComponents() to build the UI.
     */
    public DashboardPanel() {
        initComponents();
    }

    /*
     * initComponents()
     * ----------------
     * Builds all the UI elements and arranges them.
     *
     * Layout strategy:
     *   We use a BorderLayout for the main panel (this).
     *   The top section (NORTH) has the title, dropdown,
     *   message, and buttons arranged in nested panels.
     *   The center (CENTER) has the activity log.
     *   The bottom (SOUTH) has a status label.
     */
    private void initComponents() {
        /*
         * BorderLayout with gaps
         * ----------------------
         * The numbers (10, 10) are horizontal and vertical gaps
         * between the regions (in pixels). This adds breathing
         * room between the top, center, and bottom sections.
         */
        setLayout(new BorderLayout(10, 10));

        /*
         * EmptyBorder adds padding around the edges of the panel.
         * Think of it like margines in a webpage.
         * (top, left, bottom, right) = 15 pixels each side.
         */
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ============================================================
        // TITLE LABEL
        // ============================================================
        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Segoe UI", 1, 24));  // 1 = BOLD, size 24

        // ============================================================
        // USER SELECTION (dropdown)
        // ============================================================
        JLabel lblUser = new JLabel("Select User:");
        lblUser.setFont(new Font("Segoe UI", 0, 14));  // 0 = PLAIN

        cmbUsers = new JComboBox<>();
        cmbUsers.setFont(new Font("Segoe UI", 0, 14));

        /*
         * DefaultComboBoxModel holds the list of items in the
         * dropdown. We provide an array of strings to show.
         */
        cmbUsers.setModel(new DefaultComboBoxModel<>(new String[]{
            "Simon bida", "Miraaa", "Aaron Ka", "Unemi"
        }));

        // ============================================================
        // MESSAGE DISPLAY
        // ============================================================
        JLabel lblMsg = new JLabel("Select your village person...");
        lblMsg.setFont(new Font("Segoe UI", 2, 13));  // 2 = ITALIC

        /*
         * JTextField - a single-line text input.
         * We set it non-editable so the user can't type in it,
         * but it still shows the funny message.
         */
        JTextField txtMsg = new JTextField(
            "The person u select is the one who is responsible for your problems \uD83D\uDE02\uD83D\uDE02"
        );
        txtMsg.setFont(new Font("Segoe UI", 0, 14));
        txtMsg.setEditable(false);  // Read-only display

        // ============================================================
        // ACTIVITY LOG
        // ============================================================
        /*
         * JTextArea - a multi-line text area.
         * (8, 40) means 8 rows and 40 columns wide.
         * We wrap it in a JScrollPane so it scrolls when the
         * text overflows (vertical scrollbar appears).
         */
        txtLog = new JTextArea(8, 40);
        txtLog.setFont(new Font("Segoe UI", 0, 13));
        txtLog.setText("Welcome! Select a user to begin.\n");
        JScrollPane scrollLog = new JScrollPane(txtLog);

        // ============================================================
        // BUTTONS WITH EVENT HANDLERS
        // ============================================================
        /*
         * JButton creates a clickable button.
         * addActionListener attaches an "event handler" - code
         * that runs when the button is clicked.
         *
         * The lambda (e -> { ... }) is a shorter way of writing:
         *   new ActionListener() {
         *       public void actionPerformed(ActionEvent e) {
         *           ...
         *       }
         *   }
         */
        JButton btnSubmit = new JButton("Submit");
        JButton btnRefresh = new JButton("Refresh");
        JButton btnExit = new JButton("Exit");

        /*
         * Submit button: writes to the activity log
         * when clicked. cmbUsers.getSelectedItem() returns
         * the currently selected string in the dropdown.
         */
        btnSubmit.addActionListener(e -> {
            String user = (String) cmbUsers.getSelectedItem();
            txtLog.append("Submitted: " + user + "\n");  // Add text to log
        });

        /*
         * Refresh button: clears the log and resets it.
         * setText("") clears all text.
         * append("...") adds new text.
         */
        btnRefresh.addActionListener(e -> {
            txtLog.setText("");                     // Clear everything
            txtLog.append("Log refreshed.\n");      // Add fresh start message
        });

        /*
         * Dropdown selection: when the user picks a different
         * name from the dropdown, this handler logs it.
         */
        cmbUsers.addActionListener(e -> {
            String user = (String) cmbUsers.getSelectedItem();
            txtLog.append("Selected: " + user + "\n");
        });

        /*
         * Exit button: finds the parent JFrame window and
         * closes it.
         *
         * SwingUtilities.getWindowAncestor(this) walks up the
         * component tree from this panel to find the window
         * (JFrame) that contains it. Then we call dispose()
         * to close that window.
         */
        btnExit.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JFrame) {
                ((JFrame) window).dispose();
            }
        });

        // ============================================================
        // STATUS LABEL
        // ============================================================
        lblStatus = new JLabel("Status: Ready");
        lblStatus.setFont(new Font("Segoe UI", 2, 12));       // Italic
        lblStatus.setForeground(new Color(100, 100, 100));    // Gray

        // ============================================================
        // LAYOUT: ARRANGING EVERYTHING
        // ============================================================
        /*
         * HEADER PANEL (uses GroupLayout)
         *
         * GroupLayout is a powerful layout manager that arranges
         * components in horizontal and vertical "groups".
         *
         * Think of it like a grid where you define the rows and
         * columns explicitly:
         *
         *   Horizontal: all items are stacked vertically
         *     [Dashboard (title)          ]
         *     [Select User: [dropdown]    ]
         *     [Select your village person.]
         *     [text field                 ]
         *
         *   Vertical: items go top to bottom in sequence
         *     [Dashboard]
         *     [Select User: [dropdown]]
         *     [Select your...]
         *     [text field]
         */
        JPanel headerPanel = new JPanel();
        GroupLayout gl = new GroupLayout(headerPanel);
        headerPanel.setLayout(gl);
        gl.setAutoCreateGaps(true);      // Auto-add gaps between components
        gl.setAutoCreateContainerGaps(false);

        /*
         * Horizontal Group
         * ----------------
         * createParallelGroup means all items are ALIGNED
         * (stacked on the left by default), one per "row".
         *
         * createSequentialGroup means items go SIDE BY SIDE
         * (like "Select User:" then the dropdown next to it).
         */
        gl.setHorizontalGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(title)     // Row 1: title
            .addGroup(gl.createSequentialGroup()     // Row 2: label + dropdown
                .addComponent(lblUser)
                .addComponent(cmbUsers, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
            )
            .addComponent(lblMsg)    // Row 3: instruction text
            .addComponent(txtMsg)    // Row 4: funny message
        );

        /*
         * Vertical Group
         * --------------
         * createSequentialGroup means items go TOP TO BOTTOM
         * in the order we add them.
         *
         * PREFERRED_SIZE means "use the component's own
         * preferred size" (don't stretch it).
         */
        gl.setVerticalGroup(gl.createSequentialGroup()
            .addComponent(title)
            .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(lblUser)
                .addComponent(cmbUsers, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
            )
            .addComponent(lblMsg)
            .addComponent(txtMsg, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        );

        /*
         * BUTTON PANEL
         * FlowLayout arranges buttons left to right in a row.
         * (FlowLayout.LEFT makes them align to the left).
         */
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnPanel.add(btnSubmit);
        btnPanel.add(btnRefresh);
        btnPanel.add(btnExit);

        /*
         * TOP PANEL
         * Combines the header (title + dropdown + message) and
         * the buttons into one vertical stack.
         * BorderLayout puts header in CENTER and buttons in SOUTH.
         */
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(headerPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);

        /*
         * MAIN PANEL ASSEMBLY
         * -------------------
         * Finally, we assemble everything into this panel:
         *   NORTH  = top section (title, dropdown, message, buttons)
         *   CENTER = scrollable activity log
         *   SOUTH  = status label
         */
        add(topPanel, BorderLayout.NORTH);
        add(scrollLog, BorderLayout.CENTER);
        add(lblStatus, BorderLayout.SOUTH);
    }
}
