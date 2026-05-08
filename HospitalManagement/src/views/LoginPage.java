package views;

import dao.UserDAO;
import models.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserDAO userDAO;

    // Modern Colors
    private final Color BG_COLOR = new Color(243, 244, 246);
    private final Color PRIMARY_COLOR = new Color(37, 99, 235);
    private final Color SECONDARY_COLOR = new Color(124, 58, 237);
    private final Color TEXT_MAIN = new Color(31, 41, 55);
    private final Color TEXT_MUTED = new Color(107, 114, 128);

    public LoginPage() {
        userDAO = new UserDAO();
        setupFrame();
        addComponents();
    }

    private void setupFrame() {
        setTitle("Hospital Management");
        setSize(420, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(BG_COLOR);
    }

    private void addComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 40, 10, 40);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Icon/Title
        
        gbc.gridy++;
        JLabel titleLabel = new JLabel("Welcome Back");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_MAIN);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 40, 30, 40);
        JLabel subtitleLabel = new JLabel("Sign in to your account");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_MUTED);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(subtitleLabel, gbc);

        // Username
        gbc.gridy++;
        gbc.insets = new Insets(0, 50, 15, 50);
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userLabel.setForeground(TEXT_MUTED);
        add(userLabel, gbc);

        gbc.gridy++;
        usernameField = createModernTextField();
        add(usernameField, gbc);

        // Password
        gbc.gridy++;
        gbc.insets = new Insets(15, 50, 15, 50);
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        passLabel.setForeground(TEXT_MUTED);
        add(passLabel, gbc);

        gbc.gridy++;
        passwordField = createModernPasswordField();
        add(passwordField, gbc);

        // Login Button
        gbc.gridy++;
        gbc.insets = new Insets(25, 50, 10, 50);
        JButton loginBtn = createModernButton("Sign In", PRIMARY_COLOR);
        loginBtn.addActionListener(new LoginActionListener());
        add(loginBtn, gbc);

        // Sign Up Button
        gbc.gridy++;
        gbc.insets = new Insets(10, 50, 30, 50);
        JButton signUpBtn = createModernButton("Create Account", SECONDARY_COLOR);
        signUpBtn.addActionListener(e -> {
            dispose();
            new SignUpPage().setVisible(true);
        });
        add(signUpBtn, gbc);
    }

    private JTextField createModernTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(8, 5, 8, 5)
        ));
        field.setOpaque(false);
        field.setBackground(BG_COLOR);
        return field;
    }

    private JPasswordField createModernPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(8, 5, 8, 5)
        ));
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
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 45));
        return button;
    }

    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginPage.this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User user = userDAO.loginUser(username, password);
            if (user != null) {
                dispose();
                new MainMenu(user).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(LoginPage.this, "Invalid credentials!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}