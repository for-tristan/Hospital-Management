package views;

import dao.AppointmentDAO;
import dao.DoctorDAO;
import models.Appointment;
import models.Doctor;
import models.User;
import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class BookAppointment extends JFrame {
    private User currentUser;
    private int docId;
    private String docName;
    private JTextField dateField;
    private JComboBox<String> timeCombo;
    private JTextArea reasonArea;

    private final Color BG_COLOR = new Color(243, 244, 246);
    private final Color PRIMARY = new Color(37, 99, 235);
    private final Color MUTED = new Color(107, 114, 128);
    private final Color TEXT_MAIN = new Color(31, 41, 55);

    public BookAppointment(User user, int doctorId, String doctorName) {
        this.currentUser = user; this.docId = doctorId; this.docName = doctorName;
        setupFrame();
        addComponents();
    }

    private void setupFrame() {
        setTitle("Book Appointment");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(BG_COLOR);
    }

    private void addComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 40, 5, 40);

        gbc.gridy = 0;
        JLabel title = new JLabel("Book with " + docName);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT_MAIN);
        add(title, gbc);

        gbc.gridy = 1;
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD)");
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(MUTED);
        add(dateLabel, gbc);

        gbc.gridy = 2;
        dateField = createModernTextField();
        dateField.setText(new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
        add(dateField, gbc);

        gbc.gridy = 3;
        JLabel timeLabel = new JLabel("Time");
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        timeLabel.setForeground(MUTED);
        add(timeLabel, gbc);

        gbc.gridy = 4;
        String[] times = {"09:00","09:30","10:00","10:30","11:00","11:30","14:00","14:30","15:00","15:30"};
        timeCombo = new JComboBox<>(times);
        timeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        timeCombo.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
        timeCombo.setBackground(BG_COLOR);
        add(timeCombo, gbc);

        gbc.gridy = 5;
        JLabel reasonLabel = new JLabel("Reason (Optional)");
        reasonLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        reasonLabel.setForeground(MUTED);
        add(reasonLabel, gbc);

        gbc.gridy = 6;
        reasonArea = new JTextArea(3, 20);
        reasonArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);
        reasonArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        reasonArea.setBackground(BG_COLOR);
        add(reasonArea, gbc);

        gbc.gridy = 7;
        gbc.insets = new Insets(20, 40, 10, 40);
        JButton bookBtn = createModernButton("Confirm Booking", PRIMARY);
        bookBtn.addActionListener(e -> bookAppointment());
        add(bookBtn, gbc);

        gbc.gridy = 8;
        gbc.insets = new Insets(5, 40, 20, 40);
        JButton backBtn = createModernButton("Cancel", new Color(156, 163, 175));
        backBtn.addActionListener(ev -> { dispose(); new DoctorSelectionPage(currentUser).setVisible(true); });
        add(backBtn, gbc);
    }

    private void bookAppointment() {
        try {
            Date aptDate = Date.valueOf(dateField.getText().trim());
            String time = (String) timeCombo.getSelectedItem();
            String reason = reasonArea.getText().trim();
            AppointmentDAO dao = new AppointmentDAO();
            
            if(!dao.isTimeSlotAvailable(docId, aptDate, time)) {
                JOptionPane.showMessageDialog(this, "Time slot is already taken!"); return;
            }
            
            if(dao.bookAppointment(new Appointment(currentUser.getUserId(), docId, aptDate, time, reason.isEmpty()?"General":reason))) {
                JOptionPane.showMessageDialog(this, "Appointment Booked!");
                dispose();
                new MainMenu(currentUser).setVisible(true);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format!");
        }
    }

    private JTextField createModernTextField() {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(6, 5, 6, 5)));
        f.setOpaque(false); f.setBackground(BG_COLOR);
        return f;
    }

    private JButton createModernButton(String text, Color bgColor) {
        JButton b = new JButton(text) {
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
        b.setFont(new Font("Segoe UI", Font.BOLD, 15));
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false); b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }
}