import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class Adam implements ActionListener {
    private JTextField userText;
    private JPasswordField passwordText;
    private JLabel success;
    private JCheckBox showPasswordCheckBox;
    private JFrame frame;

    public static void main(String... args) {
        JPanel panel = new JPanel();
        JFrame frame = new JFrame();

        frame.setSize(800, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        panel.setLayout(null);

        Adam adam = new Adam();

        JLabel userLabel = new JLabel("User");
        adam.userText = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password");
        adam.passwordText = new JPasswordField(20);
        JButton button = new JButton("LOGIN");
        adam.success = new JLabel("");
        adam.showPasswordCheckBox = new JCheckBox("Show Password");

        button.addActionListener(adam);

        adam.showPasswordCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (adam.showPasswordCheckBox.isSelected()) {
                    adam.passwordText.setEchoChar((char) 0);
                } else {
                    adam.passwordText.setEchoChar('*');
                }
            }
        });

        userLabel.setBounds(100, 100, 80, 25);
        adam.userText.setBounds(180, 100, 165, 25);
        passwordLabel.setBounds(100, 140, 80, 25);
        adam.passwordText.setBounds(180, 140, 165, 25);
        button.setBounds(180, 180, 85, 25);
        adam.showPasswordCheckBox.setBounds(180, 210, 150, 25);
        adam.success.setBounds(180, 250, 300, 25);

        panel.add(userLabel);
        panel.add(adam.userText);
        panel.add(passwordLabel);
        panel.add(adam.passwordText);
        panel.add(button);
        panel.add(adam.success);
        panel.add(adam.showPasswordCheckBox);

        adam.frame = frame;
        frame.setVisible(true);

        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                int width = frame.getWidth();
                int height = frame.getHeight();

                int x = (width - 300) / 2;
                int y = (height - 250) / 2;

                userLabel.setBounds(x, y - 40, 80, 25);
                adam.userText.setBounds(x + 100, y - 40, 165, 25);
                passwordLabel.setBounds(x, y - 10, 80, 25);
                adam.passwordText.setBounds(x + 100, y - 10, 165, 25);
                button.setBounds(x + 100, y + 30, 85, 25);
                adam.showPasswordCheckBox.setBounds(x + 100, y + 60, 150, 25);
                adam.success.setBounds(x, y + 90, 300, 25);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = userText.getText();
        char[] password = passwordText.getPassword();

        if ("admin".equals(username) && "password".equals(new String(password))) {
            success.setText("Login Successful");
            frame.dispose();
            openCalculator();
        } else {
            success.setText("Login Failed");
        }
    }

    private void openCalculator() {
        JFrame calcFrame = new JFrame("Calculator");
        calcFrame.setSize(400, 500);
        calcFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel calcPanel = new JPanel();
        calcPanel.setLayout(new BorderLayout());

        JTextField calcDisplay = new JTextField();
        calcDisplay.setEditable(false);
        calcPanel.add(calcDisplay, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4));

        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
            "%", "CE", "C"
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String command = e.getActionCommand();
                    if (command.equals("=")) {
                        try {
                            calcDisplay.setText(formatResult(eval(calcDisplay.getText())));
                        } catch (Exception ex) {
                            calcDisplay.setText("Error");
                        }
                    } else if (command.equals("CE")) {
                        String currentText = calcDisplay.getText();
                        if (currentText.length() > 0) {
                            calcDisplay.setText(currentText.substring(0, currentText.length() - 1));
                        }
                    } else if (command.equals("C")) {
                        calcDisplay.setText("");
                    } else if (command.equals("%")) {
                        double value = Double.parseDouble(calcDisplay.getText());
                        calcDisplay.setText(formatResult(value / 100));
                    } else {
                        calcDisplay.setText(calcDisplay.getText() + command);
                    }
                }
            });
            buttonPanel.add(button);
        }

        calcPanel.add(buttonPanel, BorderLayout.CENTER);
        calcFrame.add(calcPanel);
        calcFrame.setVisible(true);
    }

    private String formatResult(double result) {
        if (result == (int) result) {
            return String.format("%d", (int) result);
        } else {
            return String.format("%s", result);
        }
    }

    private double eval(String expression) {
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

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char) c);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
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
                    throw new RuntimeException("Unexpected: " + (char) c);
                }

                return x;
            }
        }.parse();
    }
}
