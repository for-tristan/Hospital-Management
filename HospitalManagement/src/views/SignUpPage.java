package views;

import dao.UserDAO;
import models.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpPage extends JFrame {
    private JTextField fullNameField, usernameField, emailField, phoneField;
    private JPasswordField passwordField, confirmPasswordField;
    private UserDAO userDAO;

    private final Color BG_COLOR = new Color(243, 244, 246);
    private final Color PRIMARY_COLOR = new Color(37, 99, 235);
    private final Color BACK_COLOR = new Color(156, 163, 175);
    private final Color TEXT_MAIN = new Color(31, 41, 55);
    private final Color TEXT_MUTED = new Color(107, 114, 128);

    public SignUpPage() {
        userDAO = new UserDAO();
        setupFrame();
        addComponents();
    }

    private void setupFrame() {
        setTitle("Sign Up");
        setSize(450, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(BG_COLOR);
    }

    private void addComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 40, 8, 40);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_MAIN);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, gbc);

        gbc.gridy = 1;
        add(createLabelField("Full Name"), gbc);
        gbc.gridy = 2;
        fullNameField = createModernTextField(); add(fullNameField, gbc);

        gbc.gridy = 3;
        add(createLabelField("Username"), gbc);
        gbc.gridy = 4;
        usernameField = createModernTextField(); add(usernameField, gbc);

        gbc.gridy = 5;
        add(createLabelField("Password"), gbc);
        gbc.gridy = 6;
        passwordField = createModernPasswordField(); add(passwordField, gbc);

        gbc.gridy = 7;
        add(createLabelField("Confirm Password"), gbc);
        gbc.gridy = 8;
        confirmPasswordField = createModernPasswordField(); add(confirmPasswordField, gbc);

        gbc.gridy = 9;
        add(createLabelField("Email"), gbc);
        gbc.gridy = 10;
        emailField = createModernTextField(); add(emailField, gbc);

        gbc.gridy = 11;
        add(createLabelField("Phone"), gbc);
        gbc.gridy = 12;
        phoneField = createModernTextField(); add(phoneField, gbc);

        gbc.gridy = 13;
        gbc.insets = new Insets(20, 40, 5, 40);
        JButton signUpBtn = createModernButton("Sign Up", PRIMARY_COLOR);
        signUpBtn.addActionListener(new SignUpListener());
        add(signUpBtn, gbc);

        gbc.gridy = 14;
        gbc.insets = new Insets(5, 40, 20, 40);
        JButton backBtn = createModernButton("Back to Login", BACK_COLOR);
        backBtn.addActionListener(e -> { dispose(); new LoginPage().setVisible(true); });
        add(backBtn, gbc);
    }

    private JLabel createLabelField(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(TEXT_MUTED);
        return label;
    }

    private JTextField createModernTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(6, 5, 6, 5)));
        field.setOpaque(false);
        field.setBackground(BG_COLOR);
        return field;
    }

    private JPasswordField createModernPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(6, 5, 6, 5)));
        field.setOpaque(false);
        field.setBackground(BG_COLOR);
        return field;
    }

    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bgColor.darker() : bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private class SignUpListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String fullName = fullNameField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirm = new String(confirmPasswordField.getPassword());
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();

            if(fullName.isEmpty()||username.isEmpty()||password.isEmpty()||email.isEmpty()) {
                JOptionPane.showMessageDialog(SignUpPage.this, "Fill all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(!password.equals(confirm)) {
                JOptionPane.showMessageDialog(SignUpPage.this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(userDAO.usernameExists(username) || userDAO.emailExists(email)) {
                JOptionPane.showMessageDialog(SignUpPage.this, "Username or Email already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(userDAO.registerUser(new User(username, password, fullName, email, phone, "patient"))) {
                JOptionPane.showMessageDialog(SignUpPage.this, "Account created!");
                dispose();
                new LoginPage().setVisible(true);
            }
        }
    }
}