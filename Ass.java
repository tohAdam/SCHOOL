import javax.swing.*;
import java.awt.*;

public class Gui {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Login and Calculator");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create login panel with abstract gradient background
        JPanel loginPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Create an abstract colorful gradient
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(255, 102, 102), // Red color
                        getWidth(), getHeight(), new Color(102, 204, 255)); // Light blue color
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        loginPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add spacing between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        loginPanel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        loginPanel.add(passwordField, gbc);

        // Invisible Show Password checkbox
        JCheckBox showPassword = new JCheckBox("Show Password");
        showPassword.setBackground(new Color(173, 216, 230)); // Match panel background
        showPassword.setOpaque(false); // Make the checkbox itself invisible
        showPassword.setBorderPainted(false); // Remove border
        gbc.gridx = 1;
        gbc.gridy = 2;
        loginPanel.add(showPassword, gbc);

        JButton loginButton = new JButton("Log In");
        gbc.gridx = 1;
        gbc.gridy = 3;
        loginPanel.add(loginButton, gbc);

        // Add the login panel to the frame
        frame.add(loginPanel);

        // Toggle password visibility based on the checkbox state
        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0); // Show password
            } else {
                passwordField.setEchoChar('*'); // Hide password
            }
        });

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().toLowerCase();  // Convert to lowercase
            String password = new String(passwordField.getPassword()).toLowerCase(); // Convert to lowercase

            // Check if username and password match the pre-defined ones (case-insensitive)
            if ("adam".equals(username) && "tohadam".equals(password)) {
                JOptionPane.showMessageDialog(frame, "Login Successful!");
                loginPanel.setVisible(false); // Hide the login panel
                showCalculator(frame); // Show the calculator panel
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    // Method to show the calculator after successful login
    private static void showCalculator(JFrame frame) {
        // Create a new panel to display the calculator
        JPanel calculatorPanel = new JPanel();
        calculatorPanel.setLayout(new BorderLayout());

        JTextField resultField = new JTextField();
        resultField.setEditable(false);
        calculatorPanel.add(resultField, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 4, 5, 5));  // A grid layout with 4x4 buttons

        // Buttons for the calculator
        String[] buttons = {"7", "8", "9", "/",
                             "4", "5", "6", "*",
                             "1", "2", "3", "-",
                             "0", "=", "+", "C"};

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.PLAIN, 24)); // Set a bigger font for clarity
            button.addActionListener(e -> {
                String currentText = resultField.getText();
                if (text.equals("C")) {
                    resultField.setText(""); // Clear the text field
                } else {
                    // Add the number or operator to the result field
                    if (text.equals("=")) {
                        try {
                            // Evaluate the expression and show result immediately
                            resultField.setText(String.valueOf(evaluateExpression(currentText)));
                        } catch (Exception ex) {
                            resultField.setText("Error");
                        }
                    } else {
                        resultField.setText(currentText + text);
                    }
                }
            });
            buttonPanel.add(button);
        }

        calculatorPanel.add(buttonPanel, BorderLayout.CENTER);
        frame.add(calculatorPanel, BorderLayout.SOUTH);
        frame.revalidate();  // Refresh the frame to show the calculator
    }

    // Method to evaluate the expression without decimals
    private static int evaluateExpression(String expression) {
        // If the expression contains an operator and operands, calculate the result
        if (expression.contains("+")) {
            String[] parts = expression.split("\\+");
            return Integer.parseInt(parts[0]) + Integer.parseInt(parts[1]);
        } else if (expression.contains("-")) {
            String[] parts = expression.split("-");
            return Integer.parseInt(parts[0]) - Integer.parseInt(parts[1]);
        } else if (expression.contains("*")) {
            String[] parts = expression.split("\\*");
            return Integer.parseInt(parts[0]) * Integer.parseInt(parts[1]);
        } else if (expression.contains("/")) {
            String[] parts = expression.split("/");
            int denominator = Integer.parseInt(parts[1]);
            if (denominator != 0) {
                return Integer.parseInt(parts[0]) / denominator;
            } else {
                return 0; // Avoid division by zero
            }
        }
        return 0;
    }
}
