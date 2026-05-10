import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

class Student {
    String name;
    double marks;

    Student(String name, double marks) {
        this.name = name;
        this.marks = marks;
    }
}

public class StudentGradeTrackerGUI extends JFrame {

    private JTextField nameField, marksField;
    private JTable table;
    private DefaultTableModel model;
    private JLabel averageLabel, highestLabel, lowestLabel;

    private ArrayList<Student> students = new ArrayList<>();

    public StudentGradeTrackerGUI() {
        setTitle("Student Grade Tracker");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Student Grade Tracker", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        headerPanel.add(title, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        nameField = new JTextField();
        marksField = new JTextField();

        JButton addButton = new JButton("Add Student");
        JButton clearButton = new JButton("Clear All");

        inputPanel.add(new JLabel("Student Name:"));
        inputPanel.add(new JLabel("Marks:"));
        inputPanel.add(new JLabel("Action:"));

        inputPanel.add(nameField);
        inputPanel.add(marksField);
        inputPanel.add(addButton);

        headerPanel.add(inputPanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        model = new DefaultTableModel();
        model.addColumn("Student Name");
        model.addColumn("Marks");

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));

        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        averageLabel = new JLabel("Average: 0");
        highestLabel = new JLabel("Highest: 0");
        lowestLabel = new JLabel("Lowest: 0");

        averageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        highestLabel.setFont(new Font("Arial", Font.BOLD, 16));
        lowestLabel.setFont(new Font("Arial", Font.BOLD, 16));

        summaryPanel.add(averageLabel);
        summaryPanel.add(highestLabel);
        summaryPanel.add(lowestLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(clearButton);

        bottomPanel.add(summaryPanel);
        bottomPanel.add(buttonPanel);

        add(bottomPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addStudent());
        clearButton.addActionListener(e -> clearAll());

        setVisible(true);
    }

    private void addStudent() {
        String name = nameField.getText().trim();
        String marksText = marksField.getText().trim();

        if (name.isEmpty() || marksText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both name and marks.");
            return;
        }

        try {
            double marks = Double.parseDouble(marksText);

            if (marks < 0 || marks > 100) {
                JOptionPane.showMessageDialog(this, "Marks should be between 0 and 100.");
                return;
            }

            Student student = new Student(name, marks);
            students.add(student);

            model.addRow(new Object[]{name, marks});

            nameField.setText("");
            marksField.setText("");

            calculateSummary();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid marks.");
        }
    }

    private void calculateSummary() {
        if (students.isEmpty()) {
            averageLabel.setText("Average: 0");
            highestLabel.setText("Highest: 0");
            lowestLabel.setText("Lowest: 0");
            return;
        }

        double total = 0;
        double highest = students.get(0).marks;
        double lowest = students.get(0).marks;

        for (Student s : students) {
            total += s.marks;

            if (s.marks > highest) {
                highest = s.marks;
            }

            if (s.marks < lowest) {
                lowest = s.marks;
            }
        }

        double average = total / students.size();

        averageLabel.setText("Average: " + String.format("%.2f", average));
        highestLabel.setText("Highest: " + highest);
        lowestLabel.setText("Lowest: " + lowest);
    }

    private void clearAll() {
        students.clear();
        model.setRowCount(0);
        calculateSummary();
    }

    public static void main(String[] args) {
        new StudentGradeTrackerGUI();
    }
}
