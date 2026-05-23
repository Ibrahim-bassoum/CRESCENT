package view;

import controller.BookingController;
import controller.CustomerController;
import controller.RoomController;
import model.Booking;
import model.Customer;
import model.Room;
import util.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Vue de gestion des réservations de chambres.
 */
public class BookingView extends JPanel {

    private final BookingController  bookingController  = new BookingController();
    private final CustomerController customerController = new CustomerController();
    private final RoomController     roomController     = new RoomController();

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private DefaultTableModel  tableModel;
    private JTable             table;
    private JComboBox<String>  cbCustomer;
    private JComboBox<String>  cbRoom;
    private JTextField         tfCheckIn;
    private JTextField         tfCheckOut;
    private JLabel             lblTotal;

    private List<Customer> customers;
    private List<Room>     availableRooms;

    public BookingView() {
        setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout(0, 0));
        setBorder(new EmptyBorder(30, 30, 30, 30));
        buildUI();
        refreshAll();
    }

    private void buildUI() {
        // ── Header ──────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));
        header.add(Theme.titleLabel("Réservations"), BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // ── Center split ─────────────────────────────────────
        JPanel center = new JPanel(new BorderLayout(20, 0));
        center.setOpaque(false);

        String[] cols = {"ID", "Client", "Chambre", "Arrivée", "Départ", "Nuits", "Total", "Statut"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        Theme.styleTable(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(6).setPreferredWidth(90);
        center.add(Theme.styledScrollPane(table), BorderLayout.CENTER);
        center.add(buildForm(), BorderLayout.EAST);
        add(center, BorderLayout.CENTER);

        // ── Bottom bar ───────────────────────────────────────
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        bottomBar.setOpaque(false);
        bottomBar.setBorder(new EmptyBorder(16, 0, 0, 0));

        JButton btnBook     = Theme.primaryButton("+ Réserver");
        JButton btnCheckout = Theme.ghostButton("Check-out");
        JButton btnCancel   = Theme.dangerButton("Annuler");
        JButton btnRefresh  = Theme.ghostButton("↻ Rafraîchir");

        btnBook.addActionListener(e    -> createBooking());
        btnCheckout.addActionListener(e-> doCheckout());
        btnCancel.addActionListener(e  -> cancelBooking());
        btnRefresh.addActionListener(e -> refreshAll());

        // Update total when fields change
        tfCheckIn.getDocument().addDocumentListener(simpleListener(() -> updateTotalPreview()));
        tfCheckOut.getDocument().addDocumentListener(simpleListener(() -> updateTotalPreview()));
        cbRoom.addActionListener(e -> updateTotalPreview());

        bottomBar.add(btnBook);
        bottomBar.add(btnCheckout);
        bottomBar.add(btnCancel);
        bottomBar.add(btnRefresh);
        add(bottomBar, BorderLayout.SOUTH);
    }

    private JPanel buildForm() {
        JPanel form = Theme.cardPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setPreferredSize(new Dimension(280, 0));

        JLabel title = Theme.sectionLabel("Nouvelle réservation");
        title.setAlignmentX(LEFT_ALIGNMENT);
        form.add(title);
        form.add(Box.createVerticalStrut(20));

        // Client
        form.add(fieldLbl("Client"));
        cbCustomer = Theme.styledComboBox();
        cbCustomer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cbCustomer.setAlignmentX(LEFT_ALIGNMENT);
        form.add(cbCustomer);
        form.add(Box.createVerticalStrut(14));

        // Room
        form.add(fieldLbl("Chambre disponible"));
        cbRoom = Theme.styledComboBox();
        cbRoom.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cbRoom.setAlignmentX(LEFT_ALIGNMENT);
        form.add(cbRoom);
        form.add(Box.createVerticalStrut(14));

        // Check-in
        form.add(fieldLbl("Arrivée (jj/mm/aaaa)"));
        tfCheckIn = Theme.styledTextField();
        tfCheckIn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        tfCheckIn.setAlignmentX(LEFT_ALIGNMENT);
        LocalDate today = LocalDate.now();
        tfCheckIn.setText(today.format(FMT));
        form.add(tfCheckIn);
        form.add(Box.createVerticalStrut(14));

        // Check-out
        form.add(fieldLbl("Départ (jj/mm/aaaa)"));
        tfCheckOut = Theme.styledTextField();
        tfCheckOut.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        tfCheckOut.setAlignmentX(LEFT_ALIGNMENT);
        tfCheckOut.setText(today.plusDays(1).format(FMT));
        form.add(tfCheckOut);
        form.add(Box.createVerticalStrut(20));

        // Total preview
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(60, 60, 80));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        form.add(sep);
        form.add(Box.createVerticalStrut(16));

        lblTotal = new JLabel("Total estimé : —");
        lblTotal.setFont(new Font("Georgia", Font.BOLD, 15));
        lblTotal.setForeground(Theme.ACCENT_GOLD);
        lblTotal.setAlignmentX(LEFT_ALIGNMENT);
        form.add(lblTotal);
        form.add(Box.createVerticalGlue());

        return form;
    }

    // ── Actions ───────────────────────────────────────────────

    private void createBooking() {
        try {
            Customer customer = getSelectedCustomer();
            Room room = getSelectedRoom();
            LocalDate ci = parseDate(tfCheckIn.getText());
            LocalDate co = parseDate(tfCheckOut.getText());
            Booking booking = bookingController.createBooking(customer, room, ci, co);
            refreshAll();
            JOptionPane.showMessageDialog(this,
                    "Réservation créée !\nTotal : " + String.format("%.2f €", booking.getTotalAmount()),
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doCheckout() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Sélectionnez une réservation.", "Erreur", JOptionPane.ERROR_MESSAGE); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        try {
            bookingController.checkout(id);
            refreshAll();
            JOptionPane.showMessageDialog(this, "Check-out effectué.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelBooking() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Sélectionnez une réservation.", "Erreur", JOptionPane.ERROR_MESSAGE); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Annuler cette réservation ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                bookingController.cancelBooking(id);
                refreshAll();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateTotalPreview() {
        try {
            Room room = getSelectedRoom();
            LocalDate ci = parseDate(tfCheckIn.getText());
            LocalDate co = parseDate(tfCheckOut.getText());
            long nights = java.time.temporal.ChronoUnit.DAYS.between(ci, co);
            double total = nights * room.getPricePerNight();
            lblTotal.setText(String.format("Total estimé : %.2f €", total));
        } catch (Exception e) {
            lblTotal.setText("Total estimé : —");
        }
    }

    private void refreshAll() {
        refreshTable();
        refreshCombos();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Booking b : bookingController.getAllBookings()) {
            tableModel.addRow(new Object[]{
                    b.getId(),
                    b.getCustomer() != null ? b.getCustomer().getFullName() : "?",
                    b.getRoom() != null ? "Ch. " + b.getRoom().getRoomNumber() : "?",
                    b.getCheckIn() != null ? b.getCheckIn().format(FMT) : "",
                    b.getCheckOut() != null ? b.getCheckOut().format(FMT) : "",
                    b.getNights(),
                    String.format("%.2f €", b.getTotalAmount()),
                    b.getStatus()
            });
        }
    }

    private void refreshCombos() {
        customers = customerController.getAllCustomers();
        availableRooms = roomController.getAvailableRooms();

        cbCustomer.removeAllItems();
        for (Customer c : customers) cbCustomer.addItem(c.getId() + " — " + c.getFullName());

        cbRoom.removeAllItems();
        for (Room r : availableRooms)
            cbRoom.addItem("Ch." + r.getRoomNumber() + " [" + r.getRoomType() + "] " + String.format("%.0f€/nuit", r.getPricePerNight()));
    }

    // ── Helpers ───────────────────────────────────────────────

    private Customer getSelectedCustomer() {
        int idx = cbCustomer.getSelectedIndex();
        if (idx < 0 || customers == null || idx >= customers.size())
            throw new IllegalArgumentException("Sélectionnez un client.");
        return customers.get(idx);
    }

    private Room getSelectedRoom() {
        int idx = cbRoom.getSelectedIndex();
        if (idx < 0 || availableRooms == null || idx >= availableRooms.size())
            throw new IllegalArgumentException("Sélectionnez une chambre disponible.");
        return availableRooms.get(idx);
    }

    private LocalDate parseDate(String text) {
        try {
            return LocalDate.parse(text.trim(), FMT);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Format de date invalide. Utilisez jj/mm/aaaa.");
        }
    }

    private JLabel fieldLbl(String text) {
        JLabel lbl = new JLabel(text.toUpperCase());
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(Theme.TEXT_MUTED);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }

    private javax.swing.event.DocumentListener simpleListener(Runnable r) {
        return new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { r.run(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { r.run(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { r.run(); }
        };
    }
}
