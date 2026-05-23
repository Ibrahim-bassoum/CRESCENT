package view;

import controller.AuthController;
import util.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Vue de connexion — style Luxury Dark.
 */
public class LoginView extends JFrame {

    private final AuthController authController;

    private JTextField     tfUsername;
    private JPasswordField tfPassword;
    private JLabel         lblError;

    public LoginView(AuthController authController) {
        this.authController = authController;
        buildUI();
    }

    private void buildUI() {
        setTitle("Hôtel CRESCENT — Connexion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // ── Root panel ──────────────────────────────────────
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(Theme.BG_DARK);
        root.setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentPane(root);

        // ── Card ────────────────────────────────────────────
        JPanel card = Theme.cardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(400, 500));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.ACCENT_GOLD, 1),
                new EmptyBorder(50, 50, 50, 50)
        ));

        // Logo / Title
        JLabel logo = new JLabel("✦", SwingConstants.CENTER);
        logo.setFont(new Font("Georgia", Font.PLAIN, 36));
        logo.setForeground(Theme.ACCENT_GOLD);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = Theme.titleLabel("CRESCENT");
        title.setFont(new Font("Georgia", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = Theme.label("Système de gestion hôtelière");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(70, 60, 20));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        // Username
        JLabel lblUser = Theme.label("NOM D'UTILISATEUR");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblUser.setForeground(Theme.TEXT_MUTED);
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        tfUsername = Theme.styledTextField();
        tfUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        tfUsername.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Password
        JLabel lblPass = Theme.label("MOT DE PASSE");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblPass.setForeground(Theme.TEXT_MUTED);
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        tfPassword = Theme.styledPasswordField();
        tfPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        tfPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Error label
        lblError = new JLabel(" ");
        lblError.setFont(Theme.FONT_SMALL);
        lblError.setForeground(Theme.DANGER);
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Login button
        JButton btnLogin = Theme.primaryButton("SE CONNECTER");
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.addActionListener(e -> doLogin());

        // Enter key trigger
        tfPassword.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) doLogin();
            }
        });

        // ── Assemble card ───────────────────────────────────
        card.add(logo);
        card.add(Box.createVerticalStrut(8));
        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(30));
        card.add(sep);
        card.add(Box.createVerticalStrut(30));
        card.add(lblUser);
        card.add(Box.createVerticalStrut(6));
        card.add(tfUsername);
        card.add(Box.createVerticalStrut(20));
        card.add(lblPass);
        card.add(Box.createVerticalStrut(6));
        card.add(tfPassword);
        card.add(Box.createVerticalStrut(12));
        card.add(lblError);
        card.add(Box.createVerticalStrut(20));
        card.add(btnLogin);

        root.add(card);

        pack();
        setLocationRelativeTo(null);
    }

    private void doLogin() {
        String username = tfUsername.getText().trim();
        String password = new String(tfPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            lblError.setText("Veuillez remplir tous les champs.");
            return;
        }

        if (authController.login(username, password)) {
            dispose();
            new DashboardView(authController).setVisible(true);
        } else {
            lblError.setText("Identifiants incorrects. Réessayez.");
            tfPassword.setText("");
            tfPassword.requestFocus();
        }
    }
}
