package hotel;

import controller.AuthController;
import util.Theme;
import view.LoginView;

import javax.swing.*;

/**
 * Point d'entrée de l'application Hôtel CRESCENT.
 */
public class Main {

    public static void main(String[] args) {
        // Appliquer les valeurs par défaut du thème
        Theme.applyGlobalDefaults();

        // Lancer l'UI sur l'Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            AuthController auth = new AuthController();
            LoginView login = new LoginView(auth);
            login.setVisible(true);
        });
    }
}
