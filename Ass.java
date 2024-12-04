import javax.swing.*;
import java.awt.*;

public class Gui {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Login and Calculator");
        frame.setSize(500, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel loginPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(255, 255, 102), getWidth(), getHeight(), new Color(102, 204, 255));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
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
        showPassword.setBackground(new Color(173, 216, 230));
        showPassword.setOpaque(false);
        showPassword.setBorderPainted(false);
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
        resultField.setHorizontalAlignment(SwingConstants.RIGHT);
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
            button.addActionListener(e -> {
                String currentText = resultField.getText();
                if (text.equals("C")) {
                    resultField.setText("");
                } else {
                    if (text.equals("=")) {
                        try {
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
        frame.add(calculatorPanel, BorderLayout.CENTER);
        frame.revalidate();
    }

    private static int evaluateExpression(String expression) {
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
                return 0;
            }
        }
        return 0;
    }
}
