package views;

import models.User;
import javax.swing.*;
import java.awt.*;

public class DoctorDashboard extends JFrame {
    private User currentUser;

    private final Color BG_COLOR = new Color(243, 244, 246);
    private final Color TEXT_MAIN = new Color(31, 41, 55);
    private final Color CARD_BG = Color.WHITE;

    public DoctorDashboard(User user) {
        this.currentUser = user;
        setupFrame();
        addComponents();
    }

    private void setupFrame() {
        setTitle("Dashboard");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(BG_COLOR);
    }

    private void addComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 30, 0, 30);

        // Header
        gbc.gridy = 0;
        JLabel welcomeLabel = new JLabel("Hello, " + currentUser.getFullName());
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        welcomeLabel.setForeground(TEXT_MAIN);
        add(welcomeLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 30, 25, 30);
        JLabel roleLabel = new JLabel("Role: " + currentUser.getRole().toUpperCase());
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleLabel.setForeground(new Color(107, 114, 128));
        add(roleLabel, gbc);

        // Cards
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 30, 15, 30);
        add(createMenuCard("Book Appointment", "Schedule with a doctor", new Color(37, 99, 235), e -> {
            dispose();
            new DoctorSelectionPage(currentUser).setVisible(true);
        }), gbc);

        gbc.gridy = 3;
        add(createMenuCard("My Appointments", "View your history", new Color(16, 185, 129), e -> {
            new ViewAppointmentsPage(currentUser).setVisible(true);
        }), gbc);

        gbc.gridy = 4;
        add(createMenuCard("Find Doctors", "Browse specialists", new Color(124, 58, 237), e -> {
            new FindDoctorsPage().setVisible(true);
        }), gbc);

        // Logout
        gbc.gridy = 5;
        gbc.insets = new Insets(25, 30, 20, 30);
        JButton logoutBtn = createModernButton("Logout", new Color(239, 68, 68));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginPage().setVisible(true);
        });
        add(logoutBtn, gbc);
    }

    private JButton createMenuCard(String title, String desc, Color accentColor, java.awt.event.ActionListener listener) {
        JButton card = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(240, 240, 240) : CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setLayout(new BorderLayout(15, 5));
        card.setContentAreaFilled(false);
        card.setBorderPainted(false);
        card.setFocusPainted(false);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(400, 80));
        card.addActionListener(listener);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_MAIN);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 0, 0));

        JLabel descLabel = new JLabel(desc);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(new Color(107, 114, 128));
        descLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 15, 0));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(descLabel, BorderLayout.CENTER);
        return card;
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
}