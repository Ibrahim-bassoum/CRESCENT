package controller;

import dao.UserDao;
import model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * Contrôleur gérant l'authentification des utilisateurs.
 * Les mots de passe sont hashés en SHA-256.
 */
public class AuthController {

    private final UserDao userDao = new UserDao();
    private User currentUser;

    /**
     * Tente de connecter l'utilisateur.
     * @return true si les identifiants sont corrects.
     */
    public boolean login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return false;
        }

        Optional<User> found = userDao.findByUsername(username.trim());
        if (found.isEmpty()) {
            System.out.println("DEBUG: utilisateur '" + username + "' introuvable en base");
            return false;
        }

        User user = found.get();
        System.out.println("DEBUG: password en base = [" + user.getPasswordHash() + "]");
        System.out.println("DEBUG: password saisi   = [" + password + "]");
        System.out.println("DEBUG: égaux ? " + password.equals(user.getPasswordHash()));

        if (password.equals(user.getPasswordHash())) {
            this.currentUser = user;
            return true;
        }
        return false;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return isLoggedIn() && "ADMIN".equals(currentUser.getRole());
    }

    /**
     * Hash SHA-256 d'un mot de passe.
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithme SHA-256 introuvable.", e);
        }
    }
}