package dao;

import config.DatabaseConnection;
import model.Dish;
import model.Dish.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO pour la gestion des plats du restaurant.
 */
public class DishDao implements Dao<Dish, Integer> {

    private Connection getConn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Dish dish) {
        String sql = "INSERT INTO dishes (name, category, price) VALUES (?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, dish.getName());
            ps.setString(2, dish.getCategory().name());
            ps.setDouble(3, dish.getPrice());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) dish.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout du plat.", e);
        }
    }

    @Override
    public void update(Dish dish) {
        String sql = "UPDATE dishes SET name=?, category=?, price=? WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, dish.getName());
            ps.setString(2, dish.getCategory().name());
            ps.setDouble(3, dish.getPrice());
            ps.setInt(4, dish.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du plat.", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM dishes WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du plat.", e);
        }
    }

    @Override
    public Optional<Dish> findById(Integer id) {
        String sql = "SELECT * FROM dishes WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du plat.", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Dish> findAll() {
        List<Dish> list = new ArrayList<>();
        String sql = "SELECT * FROM dishes ORDER BY category, name";
        try (Statement st = getConn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du chargement des plats.", e);
        }
        return list;
    }

    /**
     * Filtre les plats par catégorie.
     */
    public List<Dish> findByCategory(Category category) {
        List<Dish> list = new ArrayList<>();
        String sql = "SELECT * FROM dishes WHERE category=? ORDER BY name";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, category.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du filtrage des plats.", e);
        }
        return list;
    }

    private Dish mapRow(ResultSet rs) throws SQLException {
        return new Dish(
                rs.getInt("id"),
                rs.getString("name"),
                Category.valueOf(rs.getString("category")),
                rs.getDouble("price")
        );
    }
}
