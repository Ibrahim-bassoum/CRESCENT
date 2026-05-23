package view;

import controller.CustomerController;
import model.Customer;
import util.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Vue de gestion des clients (CRUD + recherche).
 */
public class CustomerView extends JPanel {

    private final CustomerController controller = new CustomerController();

    private DefaultTableModel tableModel;
    private JTable            table;

    private JTextField tfName;
    private JTextField tfAddress;
    private JTextField tfPhone;
    private JTextField tfEmail;
    private JTextField tfSearch;

    public CustomerView() {
        setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout(0, 0));
        setBorder(new EmptyBorder(30, 30, 30, 30));
        buildUI();
        refreshTable();
    }

    private void buildUI() {
        // ── Header ──────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));

        header.add(Theme.titleLabel("Gestion des Clients"), BorderLayout.WEST);

        // Search bar
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchBar.setOpaque(false);
        tfSearch = Theme.styledTextField();
        tfSearch.setPreferredSize(new Dimension(220, 36));
        tfSearch.setToolTipText("Rechercher par nom...");
        JButton btnSearch = Theme.ghostButton("Rechercher");
        btnSearch.addActionListener(e -> searchCustomers());
        JButton btnReset  = Theme.ghostButton("↺");
        btnReset.addActionListener(e -> { tfSearch.setText(""); refreshTable(); });
        searchBar.add(tfSearch);
        searchBar.add(btnSearch);
        searchBar.add(btnReset);
        header.add(searchBar, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ── Center split ─────────────────────────────────────
        JPanel center = new JPanel(new BorderLayout(20, 0));
        center.setOpaque(false);

        String[] cols = {"ID", "Nom complet", "Adresse", "Téléphone", "Email"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        Theme.styleTable(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        center.add(Theme.styledScrollPane(table), BorderLayout.CENTER);
        center.add(buildForm(), BorderLayout.EAST);
        add(center, BorderLayout.CENTER);

        // ── Bottom bar ───────────────────────────────────────
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        bottomBar.setOpaque(false);
        bottomBar.setBorder(new EmptyBorder(16, 0, 0, 0));

        JButton btnAdd    = Theme.primaryButton("+ Ajouter");
        JButton btnUpdate = Theme.ghostButton("Modifier");
        JButton btnDelete = Theme.dangerButton("Supprimer");

        btnAdd.addActionListener(e    -> addCustomer());
        btnUpdate.addActionListener(e -> updateCustomer());
        btnDelete.addActionListener(e -> deleteCustomer());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillFormFromSelection();
        });

        bottomBar.add(btnAdd);
        bottomBar.add(btnUpdate);
        bottomBar.add(btnDelete);
        add(bottomBar, BorderLayout.SOUTH);
    }

    private JPanel buildForm() {
        JPanel form = Theme.cardPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setPreferredSize(new Dimension(280, 0));

        JLabel title = Theme.sectionLabel("Détails client");
        title.setAlignmentX(LEFT_ALIGNMENT);
        form.add(title);
        form.add(Box.createVerticalStrut(20));

        tfName    = addField(form, "Nom complet");
        tfAddress = addField(form, "Adresse");
        tfPhone   = addField(form, "Téléphone");
        tfEmail   = addField(form, "Email");
        form.add(Box.createVerticalGlue());

        return form;
    }

    private JTextField addField(JPanel form, String label) {
        JLabel lbl = new JLabel(label.toUpperCase());
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(Theme.TEXT_MUTED);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        form.add(lbl);
        form.add(Box.createVerticalStrut(5));
        JTextField tf = Theme.styledTextField();
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        tf.setAlignmentX(LEFT_ALIGNMENT);
        form.add(tf);
        form.add(Box.createVerticalStrut(14));
        return tf;
    }

    // ── Actions ───────────────────────────────────────────────

    private void addCustomer() {
        try {
            controller.addCustomer(buildCustomerFromForm());
            refreshTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Client ajouté avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCustomer() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Sélectionnez un client.", "Erreur", JOptionPane.ERROR_MESSAGE); return; }
        try {
            Customer c = buildCustomerFromForm();
            c.setId((int) tableModel.getValueAt(row, 0));
            controller.updateCustomer(c);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Client mis à jour.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCustomer() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Sélectionnez un client.", "Erreur", JOptionPane.ERROR_MESSAGE); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Supprimer ce client ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.deleteCustomer(id);
            refreshTable();
            clearForm();
        }
    }

    private void searchCustomers() {
        String q = tfSearch.getText().trim();
        tableModel.setRowCount(0);
        List<Customer> list = q.isEmpty() ? controller.getAllCustomers() : controller.searchCustomers(q);
        for (Customer c : list) addRow(c);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Customer c : controller.getAllCustomers()) addRow(c);
    }

    private void addRow(Customer c) {
        tableModel.addRow(new Object[]{c.getId(), c.getFullName(), c.getAddress(), c.getPhone(), c.getEmail()});
    }

    private void fillFormFromSelection() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        tfName.setText((String) tableModel.getValueAt(row, 1));
        tfAddress.setText((String) tableModel.getValueAt(row, 2));
        tfPhone.setText((String) tableModel.getValueAt(row, 3));
        tfEmail.setText((String) tableModel.getValueAt(row, 4));
    }

    private Customer buildCustomerFromForm() {
        Customer c = new Customer();
        c.setFullName(tfName.getText().trim());
        c.setAddress(tfAddress.getText().trim());
        c.setPhone(tfPhone.getText().trim());
        c.setEmail(tfEmail.getText().trim());
        return c;
    }

    private void clearForm() {
        tfName.setText(""); tfAddress.setText("");
        tfPhone.setText(""); tfEmail.setText("");
        table.clearSelection();
    }
}
