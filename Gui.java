import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Gui {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        frame.setSize(500, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
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

        JCheckBox showPassword = new JCheckBox("Show Password");
        gbc.gridx = 1;
        gbc.gridy = 2;
        loginPanel.add(showPassword, gbc);

        JButton loginButton = new JButton("Log In");
        gbc.gridx = 1;
        gbc.gridy = 3;
        loginPanel.add(loginButton, gbc);

        frame.add(loginPanel);

        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }
        });

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().toLowerCase();
            String password = new String(passwordField.getPassword()).toLowerCase();

            if ("adam".equals(username) && "tohadam".equals(password)) {
                loginPanel.setVisible(false);
                showCalculator(frame);
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    private static void showCalculator(JFrame frame) {
        JPanel calculatorPanel = new JPanel();
        calculatorPanel.setLayout(new BorderLayout());

        JTextField resultField = new JTextField();
        resultField.setEditable(false);
        resultField.setFont(new Font("Arial", Font.PLAIN, 36));
        resultField.setHorizontalAlignment(SwingConstants.CENTER);
        calculatorPanel.add(resultField, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 4, 5, 5));

        String[] buttons = {"7", "8", "9", "/",
                             "4", "5", "6", "*",
                             "1", "2", "3", "-",
                             "0", "=", "+", "C"};

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.PLAIN, 24));
            button.setFocusPainted(false);

            button.addActionListener(e -> {
                String currentText = resultField.getText();
                if (text.equals("C")) {
                    resultField.setText("");
                } else {
                    if (text.equals("=")) {
                        try {
                            // Evaluate the expression and remove decimals by casting to int
                            double result = evaluateExpression(currentText);
                            if (result == (int) result) { // If result is a whole number, show as integer
                                resultField.setText(String.valueOf((int) result));
                            } else {
                                resultField.setText(String.valueOf(result));
                            }
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
        frame.add(calculatorPanel, BorderLayout.CENTER);
        frame.revalidate();
    }

    private static double evaluateExpression(String expression) {
        try {
            if (expression.contains("+")) {
                String[] parts = expression.split("\\+");
                return (int) (Double.parseDouble(parts[0]) + Double.parseDouble(parts[1])); // cast to int
            } else if (expression.contains("-")) {
                String[] parts = expression.split("-");
                return (int) (Double.parseDouble(parts[0]) - Double.parseDouble(parts[1])); // cast to int
            } else if (expression.contains("*")) {
                String[] parts = expression.split("\\*");
                return (int) (Double.parseDouble(parts[0]) * Double.parseDouble(parts[1])); // cast to int
            } else if (expression.contains("/")) {
                String[] parts = expression.split("/");
                double denominator = Double.parseDouble(parts[1]);
                if (denominator != 0) {
                    return (int) (Double.parseDouble(parts[0]) / denominator); // cast to int
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }
}
