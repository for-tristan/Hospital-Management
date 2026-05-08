package views;

import dao.DoctorDAO;
import models.Doctor;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FindDoctorsPage extends JFrame {
    private DoctorDAO docDAO;
    private JTable docTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    private final Color BG_COLOR = new Color(243, 244, 246);
    private final Color PRIMARY = new Color(37, 99, 235);
    private final Color TEXT_MAIN = new Color(31, 41, 55);

    public FindDoctorsPage() {
        this.docDAO = new DoctorDAO();
        setupFrame();
        addComponents();
        loadAll();
    }

    private void setupFrame() {
        setTitle("Find Doctors");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BG_COLOR);
        ((JComponent)getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    private void addComponents() {
        JLabel title = new JLabel("Available Doctors");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT_MAIN);
        add(title, BorderLayout.NORTH);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(BG_COLOR);
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        searchField.setOpaque(false);
        searchField.setBackground(BG_COLOR);
        searchField.addActionListener(e -> search());
        
        JButton searchBtn = createModernButton("Search", PRIMARY);
        searchBtn.addActionListener(e -> search());
        
        JButton showAllBtn = createModernButton("Show All", new Color(16, 185, 129));
        showAllBtn.addActionListener(e -> loadAll());

        toolBar.add(searchField);
        toolBar.addSeparator();
        toolBar.add(searchBtn);
        toolBar.add(showAllBtn);
        add(toolBar, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Name", "Specialization", "Experience", "Phone", "Schedule"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        docTable = new JTable(tableModel);
        docTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        docTable.setRowHeight(30);
        docTable.setShowGrid(false);
        docTable.setSelectionBackground(new Color(219, 234, 254));
        docTable.setIntercellSpacing(new Dimension(0, 5));
        add(new JScrollPane(docTable), BorderLayout.CENTER);
    }

    private void loadAll() {
        tableModel.setRowCount(0);
        for(Doctor d : docDAO.getAllDoctors()) {
            tableModel.addRow(new Object[]{d.getFullName(), d.getSpecialization(), d.getExperienceYears()+" yrs", d.getPhone(), d.getAvailableDays()});
        }
    }

    private void search() {
        String txt = searchField.getText().trim().toLowerCase(); // Forces search text to lowercase
        if(txt.isEmpty()) { loadAll(); return; }
        
        tableModel.setRowCount(0);
        List<Doctor> allDocs = docDAO.getAllDoctors();
        
        for(Doctor d : allDocs) {
            // Check if the text is in the Name OR Specialization (ignoring case)
            if(d.getFullName().toLowerCase().contains(txt) || d.getSpecialization().toLowerCase().contains(txt)) {
                tableModel.addRow(new Object[]{d.getFullName(), d.getSpecialization(), d.getExperienceYears()+" yrs", d.getPhone(), d.getAvailableDays()});
            }
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
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setForeground(Color.WHITE);
        b.setContentAreaFilled(false); b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }
}