package view;

import controller.RoomController;
import model.Room;
import model.Room.BedType;
import model.Room.RoomType;
import util.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Vue de gestion des chambres (CRUD).
 */
public class RoomManagementView extends JPanel {

    private final RoomController controller = new RoomController();

    private DefaultTableModel tableModel;
    private JTable            table;

    private JTextField  tfRoomNumber;
    private JComboBox<RoomType> cbRoomType;
    private JComboBox<BedType>  cbBedType;
    private JTextField  tfPrice;
    private JComboBox<Room.Status> cbStatus;

    public RoomManagementView() {
        setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout(0, 0));
        setBorder(new EmptyBorder(30, 30, 30, 30));
        buildUI();
        refreshTable();
    }

    private void buildUI() {
        // ── Header ──────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = Theme.titleLabel("Gestion des Chambres");
        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);
        add(Box.createVerticalStrut(20));

        // ── Center split ─────────────────────────────────────
        JPanel center = new JPanel(new BorderLayout(20, 0));
        center.setOpaque(false);

        // Table
        String[] cols = {"N°", "Type", "Lit", "Prix/nuit", "Statut"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        Theme.styleTable(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        center.add(Theme.styledScrollPane(table), BorderLayout.CENTER);

        // Form panel
        center.add(buildForm(), BorderLayout.EAST);

        add(center, BorderLayout.CENTER);

        // ── Bottom buttons ───────────────────────────────────
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        bottomBar.setOpaque(false);
        bottomBar.setBorder(new EmptyBorder(16, 0, 0, 0));

        JButton btnAdd    = Theme.primaryButton("+ Ajouter");
        JButton btnUpdate = Theme.ghostButton("Modifier");
        JButton btnDelete = Theme.dangerButton("Supprimer");
        JButton btnRefresh= Theme.ghostButton("↻ Rafraîchir");

        btnAdd.addActionListener(e    -> addRoom());
        btnUpdate.addActionListener(e -> updateRoom());
        btnDelete.addActionListener(e -> deleteRoom());
        btnRefresh.addActionListener(e -> refreshTable());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillFormFromSelection();
        });

        bottomBar.add(btnAdd);
        bottomBar.add(btnUpdate);
        bottomBar.add(btnDelete);
        bottomBar.add(btnRefresh);
        add(bottomBar, BorderLayout.SOUTH);
    }

    private JPanel buildForm() {
        JPanel form = Theme.cardPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setPreferredSize(new Dimension(280, 0));

        JLabel formTitle = Theme.sectionLabel("Détails chambre");
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(formTitle);
        form.add(Box.createVerticalStrut(20));

        // Room number
        form.add(fieldLabel("N° de chambre"));
        tfRoomNumber = Theme.styledTextField();
        tfRoomNumber.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        tfRoomNumber.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(tfRoomNumber);
        form.add(Box.createVerticalStrut(14));

        // Room type
        form.add(fieldLabel("Type de chambre"));
        cbRoomType = Theme.styledComboBox();
        for (RoomType t : RoomType.values()) cbRoomType.addItem(t);
        cbRoomType.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cbRoomType.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(cbRoomType);
        form.add(Box.createVerticalStrut(14));

        // Bed type
        form.add(fieldLabel("Type de lit"));
        cbBedType = Theme.styledComboBox();
        for (BedType b : BedType.values()) cbBedType.addItem(b);
        cbBedType.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cbBedType.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(cbBedType);
        form.add(Box.createVerticalStrut(14));

        // Price
        form.add(fieldLabel("Prix / nuit (€)"));
        tfPrice = Theme.styledTextField();
        tfPrice.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        tfPrice.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(tfPrice);
        form.add(Box.createVerticalStrut(14));

        // Status
        form.add(fieldLabel("Statut"));
        cbStatus = Theme.styledComboBox();
        for (Room.Status s : Room.Status.values()) cbStatus.addItem(s);
        cbStatus.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cbStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(cbStatus);
        form.add(Box.createVerticalGlue());

        return form;
    }

    private JLabel fieldLabel(String text) {
        JLabel lbl = Theme.label(text.toUpperCase());
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    // ── Actions ───────────────────────────────────────────────

    private void addRoom() {
        try {
            Room room = buildRoomFromForm();
            controller.addRoom(room);
            refreshTable();
            clearForm();
            showSuccess("Chambre ajoutée avec succès.");
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void updateRoom() {
        int row = table.getSelectedRow();
        if (row < 0) { showError("Sélectionnez une chambre."); return; }
        try {
            Room room = buildRoomFromForm();
            controller.updateRoom(room);
            refreshTable();
            showSuccess("Chambre mise à jour.");
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void deleteRoom() {
        int row = table.getSelectedRow();
        if (row < 0) { showError("Sélectionnez une chambre."); return; }
        int num = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Supprimer la chambre #" + num + " ?", "Confirmation",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.deleteRoom(num);
            refreshTable();
            clearForm();
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Room> rooms = controller.getAllRooms();
        for (Room r : rooms) {
            tableModel.addRow(new Object[]{
                    r.getRoomNumber(),
                    r.getRoomType(),
                    r.getBedType(),
                    String.format("%.2f €", r.getPricePerNight()),
                    r.getStatus()
            });
        }
    }

    private void fillFormFromSelection() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int num = (int) tableModel.getValueAt(row, 0);
        controller.getRoom(num).ifPresent(r -> {
            tfRoomNumber.setText(String.valueOf(r.getRoomNumber()));
            cbRoomType.setSelectedItem(r.getRoomType());
            cbBedType.setSelectedItem(r.getBedType());
            tfPrice.setText(String.valueOf(r.getPricePerNight()));
            cbStatus.setSelectedItem(r.getStatus());
        });
    }

    private Room buildRoomFromForm() {
        int roomNum;
        double price;
        try {
            roomNum = Integer.parseInt(tfRoomNumber.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le numéro de chambre doit être un entier.");
        }
        try {
            price = Double.parseDouble(tfPrice.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le prix doit être un nombre valide.");
        }
        Room room = new Room(roomNum, (RoomType) cbRoomType.getSelectedItem(),
                (BedType) cbBedType.getSelectedItem(), price);
        room.setStatus((Room.Status) cbStatus.getSelectedItem());
        return room;
    }

    private void clearForm() {
        tfRoomNumber.setText("");
        tfPrice.setText("");
        cbRoomType.setSelectedIndex(0);
        cbBedType.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
        table.clearSelection();
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Succès", JOptionPane.INFORMATION_MESSAGE);
    }
}
