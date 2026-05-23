package controller;

import dao.DishDao;
import model.Dish;
import model.Dish.Category;

import java.util.List;
import java.util.Optional;

/**
 * Contrôleur gérant la logique métier des plats du restaurant.
 */
public class DishController {

    private final DishDao dishDao = new DishDao();

    public void addDish(Dish dish) {
        validate(dish);
        dishDao.save(dish);
    }

    public void updateDish(Dish dish) {
        validate(dish);
        dishDao.update(dish);
    }

    public void deleteDish(int id) {
        dishDao.delete(id);
    }

    public Optional<Dish> getDish(int id) {
        return dishDao.findById(id);
    }

    public List<Dish> getAllDishes() {
        return dishDao.findAll();
    }

    public List<Dish> getDishesByCategory(Category category) {
        return dishDao.findByCategory(category);
    }

    private void validate(Dish dish) {
        if (dish.getName() == null || dish.getName().isBlank()) {
            throw new IllegalArgumentException("Le nom du plat est obligatoire.");
        }
        if (dish.getPrice() <= 0) {
            throw new IllegalArgumentException("Le prix doit être positif.");
        }
        if (dish.getCategory() == null) {
            throw new IllegalArgumentException("La catégorie est obligatoire.");
        }
    }
}
