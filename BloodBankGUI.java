import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class BloodBankGUI extends JFrame {
    private static List<Donor> donors = new ArrayList<>();
    private static BloodInventory inventory = new BloodInventory();
    
    private JTabbedPane tabbedPane;
    private JTable donorsTable;
    private JTable inventoryTable;
    private DefaultTableModel donorsTableModel;
    private DefaultTableModel inventoryTableModel;

    public BloodBankGUI() {
        setTitle("Blood Bank Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        initializeComponents();
        setupLayout();
        updateTables();
    }

    private void initializeComponents() {
        tabbedPane = new JTabbedPane();
        
        // Create tabs
        tabbedPane.addTab("Add Donor", createAddDonorPanel());
        tabbedPane.addTab("Donate Blood", createDonateBloodPanel());
        tabbedPane.addTab("Request Blood", createRequestBloodPanel());
        tabbedPane.addTab("View Donors", createViewDonorsPanel());
        tabbedPane.addTab("View Inventory", createViewInventoryPanel());
    }

    private JPanel createAddDonorPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title
        JLabel titleLabel = new JLabel("Add New Donor");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Name field
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Name:"), gbc);
        JTextField nameField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        // Blood Group field
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Blood Group:"), gbc);
        JComboBox<String> bloodGroupCombo = new JComboBox<>(
            new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        gbc.gridx = 1;
        panel.add(bloodGroupCombo, gbc);
        
        // Contact field
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Contact:"), gbc);
        JTextField contactField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(contactField, gbc);
        
        // Add button
        JButton addButton = new JButton("Add Donor");
        addButton.setBackground(new Color(34, 139, 34));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String bloodGroup = (String) bloodGroupCombo.getSelectedItem();
                String contact = contactField.getText().trim();
                
                if (name.isEmpty() || contact.isEmpty()) {
                    showErrorMessage("Please fill all fields!");
                    return;
                }
                
                donors.add(new Donor(name, bloodGroup, contact));
                nameField.setText("");
                contactField.setText("");
                bloodGroupCombo.setSelectedIndex(0);
                showSuccessMessage("Donor added successfully!");
                updateTables();
            }
        });
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(addButton, gbc);
        
        return panel;
    }

    private JPanel createDonateBloodPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title
        JLabel titleLabel = new JLabel("Donate Blood");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Blood Group field
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Blood Group:"), gbc);
        JComboBox<String> bloodGroupCombo = new JComboBox<>(
            new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        gbc.gridx = 1;
        panel.add(bloodGroupCombo, gbc);
        
        // Units field
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Units:"), gbc);
        JSpinner unitsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        gbc.gridx = 1;
        panel.add(unitsSpinner, gbc);
        
        // Donate button
        JButton donateButton = new JButton("Donate Blood");
        donateButton.setBackground(new Color(220, 20, 60));
        donateButton.setForeground(Color.WHITE);
        donateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bloodGroup = (String) bloodGroupCombo.getSelectedItem();
                int units = (Integer) unitsSpinner.getValue();
                
                inventory.addBlood(bloodGroup, units);
                unitsSpinner.setValue(1);
                bloodGroupCombo.setSelectedIndex(0);
                showSuccessMessage("Blood donated successfully!");
                updateTables();
            }
        });
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(donateButton, gbc);
        
        return panel;
    }

    private JPanel createRequestBloodPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title
        JLabel titleLabel = new JLabel("Request Blood");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Blood Group field
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Blood Group:"), gbc);
        JComboBox<String> bloodGroupCombo = new JComboBox<>(
            new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        gbc.gridx = 1;
        panel.add(bloodGroupCombo, gbc);
        
        // Units field
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Units:"), gbc);
        JSpinner unitsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        gbc.gridx = 1;
        panel.add(unitsSpinner, gbc);
        
        // Request button
        JButton requestButton = new JButton("Request Blood");
        requestButton.setBackground(new Color(30, 144, 255));
        requestButton.setForeground(Color.WHITE);
        requestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bloodGroup = (String) bloodGroupCombo.getSelectedItem();
                int units = (Integer) unitsSpinner.getValue();
                
                boolean success = inventory.requestBlood(bloodGroup, units);
                if (success) {
                    showSuccessMessage("Blood request fulfilled successfully!");
                    updateTables();
                } else {
                    showErrorMessage("Insufficient blood units available!");
                }
                
                unitsSpinner.setValue(1);
                bloodGroupCombo.setSelectedIndex(0);
            }
        });
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(requestButton, gbc);
        
        return panel;
    }

    private JPanel createViewDonorsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Title
        JLabel titleLabel = new JLabel("Donors List", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Table
        String[] columnNames = {"Name", "Blood Group", "Contact"};
        donorsTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        donorsTable = new JTable(donorsTableModel);
        donorsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        donorsTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(donorsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createViewInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Title
        JLabel titleLabel = new JLabel("Blood Inventory", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Table
        String[] columnNames = {"Blood Group", "Units Available"};
        inventoryTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        inventoryTable = new JTable(inventoryTableModel);
        inventoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        inventoryTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
        
        // Add a header
        JLabel headerLabel = new JLabel("Blood Bank Management System", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(220, 20, 60));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(headerLabel, BorderLayout.NORTH);
    }

    private void updateTables() {
        // Update donors table
        donorsTableModel.setRowCount(0);
        for (Donor donor : donors) {
            Object[] row = {donor.getName(), donor.getBloodGroup(), donor.getContact()};
            donorsTableModel.addRow(row);
        }
        
        // Update inventory table
        inventoryTableModel.setRowCount(0);
        for (Map.Entry<String, Integer> entry : inventory.getEntries()) {
            Object[] row = {entry.getKey(), entry.getValue()};
            inventoryTableModel.addRow(row);
        }
    }

    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BloodBankGUI().setVisible(true);
            }
        });
    }
}