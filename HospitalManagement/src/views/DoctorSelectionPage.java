package views;

import dao.DoctorDAO;
import models.Doctor;
import models.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorSelectionPage extends JFrame {
    private User currentUser;
    private DoctorDAO doctorDAO;
    private JTable doctorTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> specCombo; // Fixed: Now properly tracked

    private final Color BG_COLOR = new Color(243, 244, 246);
    private final Color PRIMARY = new Color(37, 99, 235);
    private final Color TEXT_MAIN = new Color(31, 41, 55);

    public DoctorSelectionPage(User user) {
        this.currentUser = user;
        this.doctorDAO = new DoctorDAO();
        setupFrame();
        addComponents();
        loadDoctors();
    }

    private void setupFrame() {
        setTitle("Select Doctor");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BG_COLOR);
        ((JComponent)getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    private void addComponents() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(BG_COLOR);
        
        JButton backBtn = new JButton("← Back");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> { dispose(); new MainMenu(currentUser).setVisible(true); });
        
        toolBar.add(backBtn);
        toolBar.addSeparator();
        toolBar.add(new JLabel("Filter: "));
        
        specCombo = new JComboBox<>();
        List<String> specs = doctorDAO.getAllSpecializations();
        specCombo.addItem("All Specializations");
        for(String s : specs) specCombo.addItem(s);
        specCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        specCombo.addActionListener(e -> loadDoctors());
        
        toolBar.add(specCombo);
        add(toolBar, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Doctor Name", "Specialization", "Experience", "Schedule"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        doctorTable = new JTable(tableModel);
        doctorTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        doctorTable.setRowHeight(30);
        doctorTable.setSelectionBackground(new Color(219, 234, 254));
        doctorTable.setSelectionForeground(new Color(31, 41, 55));
        doctorTable.setShowGrid(false);
        doctorTable.setIntercellSpacing(new Dimension(0, 5));
        
        add(new JScrollPane(doctorTable), BorderLayout.CENTER);

        JButton selectBtn = createModernButton("Continue to Booking", PRIMARY);
        selectBtn.addActionListener(e -> {
            int row = doctorTable.getSelectedRow();
            if(row == -1) { JOptionPane.showMessageDialog(this, "Select a doctor first!"); return; }
            int id = (int) tableModel.getValueAt(row, 0);
            String name = (String) tableModel.getValueAt(row, 1);
            dispose();
            new BookAppointment(currentUser, id, name).setVisible(true);
        });
        add(selectBtn, BorderLayout.SOUTH);
    }

    private void loadDoctors() {
        tableModel.setRowCount(0);
        String spec = (String) specCombo.getSelectedItem(); 
        List<Doctor> docs = spec.equals("All Specializations") ? doctorDAO.getAllDoctors() : doctorDAO.getDoctorsBySpecialization(spec);
        for(Doctor d : docs) tableModel.addRow(new Object[]{d.getDoctorId(), d.getFullName(), d.getSpecialization(), d.getExperienceYears()+" yrs", d.getAvailableDays()});
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
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}