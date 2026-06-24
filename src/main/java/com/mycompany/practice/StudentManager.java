package com.mycompany.practice;

/*
 * ============================================================
 * WHAT THIS FILE DOES:
 *
 * This is the MAIN ENTRY POINT of the application.
 * When you run this file, it creates a window (JFrame) with:
 *   - A tabbed pane containing 3 tabs:
 *       1. Dashboard  - user selection and activity log
 *       2. Students   - CRUD operations on students
 *       3. Courses    - CRUD operations on courses
 *   - A status bar at the bottom
 *
 * It's the "container" that holds everything together.
 * ============================================================
 */

// JFrame = the main window with title bar, minimize/maximize/close buttons
// JTabbedPane = a container with clickable tabs to switch between panels
// SwingUtilities = helper for thread-safe GUI operations
import javax.swing.*;

// Font, Color, Dimension = classes for styling the UI
import java.awt.*;

/*
 * StudentManager extends JFrame
 * -----------------------------
 * "extends" means StudentManager INHERITS from JFrame.
 * JFrame is a built-in Swing class that represents a window.
 * By extending it, our class IS a JFrame - it has all the
 * capabilities of a window (title bar, close button, etc.)
 * plus anything extra we add.
 */
public class StudentManager extends JFrame {

    /*
     * Constructor
     * -----------
     * This runs when we do:  new StudentManager()
     *
     * Step 1: DatabaseManager.initialize()
     *   Creates the database tables if they don't exist yet.
     *   We only do this once, at startup.
     *
     * Step 2: initComponents()
     *   Sets up all the GUI components (tabs, buttons, etc.)
     */
    public StudentManager() {
        DatabaseManager.initialize();  // Set up the database
        initComponents();              // Build the user interface
    }

    /*
     * initComponents()
     * ----------------
     * This is a private method - it can only be called from
     * within this class (not from outside).
     *
     * It creates and arranges all the visual elements of the
     * window. This is sometimes called "building the UI".
     */
    private void initComponents() {
        // Set the window title (appears in the title bar)
        setTitle("Student Manager");

        /*
         * EXIT_ON_CLOSE means: when the user clicks the X button,
         * the entire application stops running (System.exit()).
         * Without this, the window would close but the Java
         * process would keep running in the background.
         */
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*
         * setMinimumSize sets the smallest size the window
         * can be resized to. The Dimension class represents
         * a width and height in pixels.
         */
        setMinimumSize(new Dimension(800, 550));

        /*
         * JTabbedPane
         * -----------
         * Think of it like a file folder with tabs at the top.
         * Each tab holds a different "page" (JPanel).
         * Clicking a tab shows that page and hides the others.
         *
         * We create one panel for each tab:
         *   - DashboardPanel  (user selection + log)
         *   - StudentPanel    (student CRUD)
         *   - CoursePanel     (course CRUD)
         *
         * Then we add them to the tabbed pane with labels.
         */
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", 0, 14));

        // Create the three panels
        DashboardPanel dashboardPanel = new DashboardPanel();
        StudentPanel studentPanel = new StudentPanel();
        CoursePanel coursePanel = new CoursePanel();

        // Add them as tabs (order matters - index 0, 1, 2)
        tabbedPane.addTab("Dashboard", dashboardPanel);   // index 0
        tabbedPane.addTab("Students", studentPanel);       // index 1
        tabbedPane.addTab("Courses", coursePanel);         // index 2

        /*
         * addChangeListener
         * -----------------
         * This "listens" for when the user clicks a different tab.
         * The lambda expression (e -> { ... }) runs every time
         * the selected tab changes.
         *
         * Why do we need this?
         * --------------------
         * If the user adds a course in the Courses tab, then
         * switches to the Students tab, the course dropdown
         * should show the new course. This listener refreshes
         * the data when switching tabs.
         *
         * getSelectedIndex() returns which tab is showing:
         *   0 = Dashboard, 1 = Students, 2 = Courses
         */
        tabbedPane.addChangeListener(e -> {
            int idx = tabbedPane.getSelectedIndex();
            if (idx == 1) {                           // Switched TO Students
                studentPanel.loadCourses();            // Refresh course dropdown
                studentPanel.loadTable();              // Refresh student table
            } else if (idx == 2) {                    // Switched TO Courses
                coursePanel.loadTable();               // Refresh course table
            }
        });

        /*
         * Status Bar
         * ----------
         * A simple label at the bottom showing current status.
         * The font style "2" means ITALIC.
         * Color(100, 100, 100) is a medium gray (RGB values).
         * EmptyBorder adds padding around the text.
         */
        JLabel statusBar = new JLabel("Connected to SQLite database");
        statusBar.setFont(new Font("Segoe UI", 2, 12));    // 2 = ITALIC
        statusBar.setForeground(new Color(100, 100, 100)); // Gray text
        statusBar.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        /*
         * BorderLayout
         * ------------
         * Divides the window into 5 regions: NORTH, SOUTH,
         * EAST, WEST, and CENTER.
         *
         * We put the tabbed pane in CENTER (it fills the
         * main area) and the status bar in SOUTH (bottom).
         */
        add(tabbedPane, BorderLayout.CENTER);  // Main content
        add(statusBar, BorderLayout.SOUTH);    // Bottom bar

        /*
         * pack()
         * ------
         * Tells the window to resize itself to fit all
         * its contents perfectly (their "preferred sizes").
         *
         * setLocationRelativeTo(null)
         * ---------------------------
         * Centers the window on the screen.
         * null means "center relative to the screen" instead
         * of centering relative to another component.
         */
        pack();
        setLocationRelativeTo(null);
    }

    /*
     * main() method
     * -------------
     * This is where the program STARTS when you run it.
     * It's a "static" method, meaning it belongs to the
     * CLASS itself, not to any particular StudentManager
     * object.
     *
     * String[] args = command-line arguments (we don't use them)
     *
     * What happens step-by-step:
     *   1. We ask Swing to run our code later (invokeLater).
     *      This is important because GUI code must run on
     *      the "Event Dispatch Thread" (EDT), not the main
     *      thread.
     *
     *   2. We try to set the "look and feel" to the system's
     *      native style (e.g., Windows style on Windows).
     *      This makes the app look like it belongs on the
     *      user's operating system.
     *
     *   3. We create a new StudentManager() and make it visible.
     */
    public static void main(String[] args) {
        /*
         * SwingUtilities.invokeLater()
         * ----------------------------
         * This schedules our code to run on the EDT (Event
         * Dispatch Thread). Swing is NOT thread-safe, meaning
         * all GUI updates MUST happen on this special thread.
         *
         * The lambda () -> { ... } is the code that will run
         * on the EDT. It's like saying: "When you get a chance,
         * please run this code on the right thread."
         */
        SwingUtilities.invokeLater(() -> {
            try {
                /*
                 * setLookAndFeel changes how the UI looks.
                 * getSystemLookAndFeelClassName() returns the
                 * native style for the current OS (Windows,
                 * macOS, Linux).
                 *
                 * Without this, Swing uses "Metal" look and
                 * feel which looks old-fashioned.
                 */
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();  // If it fails, just continue with default look
            }

            /*
             * new StudentManager() creates our window.
             * .setVisible(true) makes it appear on screen.
             */
            new StudentManager().setVisible(true);
        });
    }
}

/*
 * ============================================================
 * HOW THE WHOLE APP FLOWS:
 *
 *   1. StudentManager.main() is called
 *   2. DatabaseManager.initialize() creates tables
 *   3. A JFrame window opens with 3 tabs
 *   4. User clicks "Courses" tab → adds a course
 *   5. User clicks "Students" tab → dropdown refreshes
 *   6. User fills form + clicks "Add" → student saved to DB
 *   7. Table refreshes to show the new student
 *
 * The tabs use a pattern called "navigation" - each tab
 * is like a different page in the app.
 * ============================================================
 */
