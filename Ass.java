import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ass {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginCalculatorApp::createLoginWindow);
    }

    private static void createLoginWindow() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setSize(300, 200);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel usernamePanel = new JPanel();
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(15);
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);
        usernamePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel passwordPanel = new JPanel();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        passwordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(usernamePanel);
        loginPanel.add(Box.createVerticalStrut(10));
        loginPanel.add(passwordPanel);
        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(loginButton);

        loginFrame.add(loginPanel);
        loginFrame.setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if ("adam".equals(username) && "adam".equals(password)) {
                    loginFrame.dispose();
                    createCalculatorWindow();
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid Username or Password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private static void createCalculatorWindow() {
        JFrame calculatorFrame = new JFrame("Calculator");
        calculatorFrame.setSize(400, 500);
        calculatorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        calculatorFrame.setLocationRelativeTo(null);

        JPanel calculatorPanel = new JPanel();
        calculatorPanel.setLayout(new BorderLayout());

        JTextField display = new JTextField();
        display.setEditable(false);
        display.setFont(new Font("Arial", Font.PLAIN, 30));
        display.setBackground(new Color(240, 240, 240));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setPreferredSize(new Dimension(400, 80));
        calculatorPanel.add(display, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        String[] buttonLabels = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "C", "+",
                "%",
                "="
        };

        int buttonIndex = 0;
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 4; col++) {
                if (buttonIndex < buttonLabels.length - 1) {
                    String label = buttonLabels[buttonIndex];
                    JButton button = new JButton(label);
                    button.setFont(new Font("Arial", Font.PLAIN, 20));
                    button.setPreferredSize(new Dimension(80, 80));
                    button.setBackground(new Color(200, 200, 255));
                    button.setForeground(new Color(0, 0, 0));

                    gbc.gridx = col;
                    gbc.gridy = row;
                    gbc.weightx = 1.0;
                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String command = e.getActionCommand();
                            String currentText = display.getText();

                            if ("=".equals(command)) {
                                try {
                                    double result = evaluateExpression(currentText);
                                    if (result == (int) result) {
                                        display.setText(String.valueOf((int) result));
                                    } else {
                                        display.setText(String.valueOf(result));
                                    }
                                } catch (Exception ex) {
                                    display.setText("Error");
                                }
                            } else if ("C".equals(command)) {
                                display.setText("");
                            } else if ("%".equals(command)) {
                                try {
                                    double currentVal = Double.parseDouble(currentText);
                                    double percentage = currentVal / 100;
                                    if (percentage == (int) percentage) {
                                        display.setText(String.valueOf((int) percentage));
                                    } else {
                                        display.setText(String.valueOf(percentage));
                                    }
                                } catch (Exception ex) {
                                    display.setText("Error");
                                }
                            } else {
                                display.setText(currentText + command);
                            }
                        }
                    });
                    buttonPanel.add(button, gbc);
                    buttonIndex++;
                }
            }
        }

        JButton equalsButton = new JButton("=");
        equalsButton.setFont(new Font("Arial", Font.PLAIN, 20));
        equalsButton.setPreferredSize(new Dimension(80, 80));
        equalsButton.setBackground(new Color(255, 200, 200));
        equalsButton.setForeground(new Color(0, 0, 0));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        equalsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentText = display.getText();
                try {
                    double result = evaluateExpression(currentText);
                    if (result == (int) result) {
                        display.setText(String.valueOf((int) result));
                    } else {
                        display.setText(String.valueOf(result));
                    }
                } catch (Exception ex) {
                    display.setText("Error");
                }
            }
        });
        buttonPanel.add(equalsButton, gbc);

        calculatorPanel.add(buttonPanel, BorderLayout.CENTER);
        calculatorFrame.add(calculatorPanel);
        calculatorFrame.setVisible(true);
    }

    private static double evaluateExpression(String expression) throws Exception {
        return new Object() {
            int pos = -1, c;

            void nextChar() {
                c = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (c == ' ') nextChar();
                if (c == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() throws Exception {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length()) throw new Exception("Unexpected: " + (char)c);
                return x;
            }

            double parseExpression() throws Exception {
                double x = parseTerm();
                while (true) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() throws Exception {
                double x = parseFactor();
                while (true) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() throws Exception {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((c >= '0' && c <= '9') || c == '.') {
                    while ((c >= '0' && c <= '9') || c == '.') nextChar();
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else {
                    throw new Exception("Unexpected: " + (char)c);
                }

                return x;
            }
        }.parse();
    }
}
