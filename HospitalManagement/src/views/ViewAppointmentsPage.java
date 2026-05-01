package views;

import dao.AppointmentDAO;
import models.Appointment;
import models.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ViewAppointmentsPage extends JFrame {
    private User currentUser;
    private AppointmentDAO aptDAO;
    private JTable aptTable;
    private DefaultTableModel tableModel;

    private final Color BG_COLOR = new Color(243, 244, 246);
    private final Color DANGER = new Color(239, 68, 68);
    private final Color TEXT_MAIN = new Color(31, 41, 55);

    public ViewAppointmentsPage(User user) {
        this.currentUser = user;
        this.aptDAO = new AppointmentDAO();
        setupFrame();
        addComponents();
        loadData();
    }

    private void setupFrame() {
        setTitle("My Appointments");
        setSize(800, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BG_COLOR);
        ((JComponent)getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    private void addComponents() {
        JLabel title = new JLabel("My Appointments");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT_MAIN);
        add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Doctor", "Specialization", "Date", "Time", "Status"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        aptTable = new JTable(tableModel);
        aptTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        aptTable.setRowHeight(30);
        aptTable.setShowGrid(false);
        aptTable.setSelectionBackground(new Color(254, 226, 226));
        aptTable.setIntercellSpacing(new Dimension(0, 5));
        add(new JScrollPane(aptTable), BorderLayout.CENTER);

        JButton cancelBtn = createModernButton("Cancel Selected Appointment", DANGER);
        cancelBtn.addActionListener(e -> {
            int row = aptTable.getSelectedRow();
            if(row == -1) return;
            String status = (String) tableModel.getValueAt(row, 5);
            if(status.equals("cancelled")) { JOptionPane.showMessageDialog(this, "Already cancelled!"); return; }
            
            if(JOptionPane.showConfirmDialog(this, "Cancel this appointment?") == 0) {
                aptDAO.cancelAppointment((int)tableModel.getValueAt(row, 0));
                loadData();
            }
        });
        add(cancelBtn, BorderLayout.SOUTH);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        for(Appointment a : aptDAO.getAppointmentsByPatientId(currentUser.getUserId())) {
            tableModel.addRow(new Object[]{a.getAppointmentId(), a.getDoctorName(), a.getSpecialization(), a.getAppointmentDate(), a.getAppointmentTime(), a.getStatus()});
        }
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
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false); b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }
}