package view;

import controller.AuthController;
import util.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Tableau de bord principal après connexion.
 * Contient la barre de navigation latérale et affiche les vues en contenu central.
 */
public class DashboardView extends JFrame {

    private final AuthController authController;
    private JPanel contentArea;

    // Vues instanciées une seule fois (lazy)
    private RoomManagementView  roomView;
    private CustomerView        customerView;
    private BookingView         bookingView;
    private RestaurantView      restaurantView;

    public DashboardView(AuthController authController) {
        this.authController = authController;
        buildUI();
    }

    private void buildUI() {
        setTitle("Hôtel CRESCENT — Tableau de bord");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG_DARK);
        setContentPane(root);

        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildContent(), BorderLayout.CENTER);

        // Afficher la vue Chambres par défaut
        showView("rooms");

        pack();
        setSize(1150, 720);
        setLocationRelativeTo(null);
    }

    // ── Sidebar ───────────────────────────────────────────────────────────

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Theme.BG_CARD);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(50, 50, 70)));

        // Hotel name
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(22, 22, 30));
        header.setBorder(new EmptyBorder(28, 24, 28, 24));
        JLabel hotelName = new JLabel("✦ CRESCENT");
        hotelName.setFont(new Font("Georgia", Font.BOLD, 18));
        hotelName.setForeground(Theme.ACCENT_GOLD);
        JLabel hotelSub = new JLabel("Gestion hôtelière");
        hotelSub.setFont(Theme.FONT_SMALL);
        hotelSub.setForeground(Theme.TEXT_MUTED);
        JPanel headerText = new JPanel();
        headerText.setLayout(new BoxLayout(headerText, BoxLayout.Y_AXIS));
        headerText.setOpaque(false);
        headerText.add(hotelName);
        headerText.add(Box.createVerticalStrut(4));
        headerText.add(hotelSub);
        header.add(headerText, BorderLayout.CENTER);
        sidebar.add(header);

        sidebar.add(Box.createVerticalStrut(16));

        // Nav items
        sidebar.add(navItem("🏨", "Chambres",    () -> showView("rooms")));
        sidebar.add(navItem("👤", "Clients",     () -> showView("customers")));
        sidebar.add(navItem("📋", "Réservations",() -> showView("bookings")));
        sidebar.add(navItem("🍽️", "Restaurant",  () -> showView("restaurant")));

        sidebar.add(Box.createVerticalGlue());

        // User info + logout
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(22, 22, 30));
        footer.setBorder(new EmptyBorder(16, 24, 20, 24));
        JLabel userLabel = new JLabel("👤 " + authController.getCurrentUser().getUsername());
        userLabel.setFont(Theme.FONT_BOLD);
        userLabel.setForeground(Theme.TEXT_PRIMARY);
        JButton btnLogout = new JButton("Déconnexion");
        btnLogout.setFont(Theme.FONT_SMALL);
        btnLogout.setBackground(new Color(50, 30, 30));
        btnLogout.setForeground(Theme.DANGER);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> {
            authController.logout();
            dispose();
            new LoginView(new AuthController()).setVisible(true);
        });
        footer.add(userLabel, BorderLayout.CENTER);
        footer.add(btnLogout, BorderLayout.SOUTH);
        sidebar.add(footer);

        return sidebar;
    }

    private JPanel navItem(String icon, String label, Runnable action) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        item.setBackground(Theme.BG_CARD);
        item.setBorder(new EmptyBorder(0, 8, 0, 8));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel ico  = new JLabel(icon);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));

        JLabel lbl  = new JLabel(label);
        lbl.setFont(Theme.FONT_BOLD);
        lbl.setForeground(Theme.TEXT_PRIMARY);

        item.add(ico);
        item.add(lbl);

        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                item.setBackground(new Color(40, 38, 22));
                lbl.setForeground(Theme.ACCENT_GOLD);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                item.setBackground(Theme.BG_CARD);
                lbl.setForeground(Theme.TEXT_PRIMARY);
            }
            public void mouseClicked(java.awt.event.MouseEvent e) {
                action.run();
            }
        });

        return item;
    }

    // ── Content area ──────────────────────────────────────────────────────

    private JPanel buildContent() {
        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(Theme.BG_DARK);
        return contentArea;
    }

    private void showView(String key) {
        contentArea.removeAll();
        JPanel view = switch (key) {
            case "rooms"      -> getRoomView();
            case "customers"  -> getCustomerView();
            case "bookings"   -> getBookingView();
            case "restaurant" -> getRestaurantView();
            default           -> getRoomView();
        };
        contentArea.add(view, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    private RoomManagementView getRoomView() {
        if (roomView == null) roomView = new RoomManagementView();
        return roomView;
    }

    private CustomerView getCustomerView() {
        if (customerView == null) customerView = new CustomerView();
        return customerView;
    }

    private BookingView getBookingView() {
        if (bookingView == null) bookingView = new BookingView();
        return bookingView;
    }

    private RestaurantView getRestaurantView() {
        if (restaurantView == null) restaurantView = new RestaurantView();
        return restaurantView;
    }
}
