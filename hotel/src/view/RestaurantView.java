package view;

import controller.DishController;
import model.Dish;
import model.Dish.Category;
import util.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Vue de gestion du menu restaurant.
 */
public class RestaurantView extends JPanel {

    private final DishController controller = new DishController();

    private DefaultTableModel tableModel;
    private JTable            table;
    private JTextField        tfName;
    private JComboBox<Category> cbCategory;
    private JTextField        tfPrice;

    public RestaurantView() {
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

        header.add(Theme.titleLabel("Menu Restaurant"), BorderLayout.WEST);

        // Filter by category
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        filterBar.setOpaque(false);
        JComboBox<String> cbFilter = new JComboBox<>(
                new String[]{"Tous", "STARTER", "MAIN_COURSE", "DESSERT", "DRINK"});
        cbFilter.setFont(Theme.FONT_INPUT);
        cbFilter.setBackground(Theme.BG_INPUT);
        cbFilter.setForeground(Theme.TEXT_PRIMARY);
        cbFilter.addActionListener(e -> filterByCategory((String) cbFilter.getSelectedItem()));
        filterBar.add(Theme.label("Filtrer :"));
        filterBar.add(cbFilter);
        header.add(filterBar, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ── Center ───────────────────────────────────────────
        JPanel center = new JPanel(new BorderLayout(20, 0));
        center.setOpaque(false);

        String[] cols = {"ID", "Nom", "Catégorie", "Prix"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        Theme.styleTable(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(3).setPreferredWidth(90);
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

        btnAdd.addActionListener(e    -> addDish());
        btnUpdate.addActionListener(e -> updateDish());
        btnDelete.addActionListener(e -> deleteDish());

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

        JLabel title = Theme.sectionLabel("Détails plat");
        title.setAlignmentX(LEFT_ALIGNMENT);
        form.add(title);
        form.add(Box.createVerticalStrut(20));

        // Name
        form.add(fieldLbl("Nom du plat"));
        tfName = Theme.styledTextField();
        tfName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        tfName.setAlignmentX(LEFT_ALIGNMENT);
        form.add(tfName);
        form.add(Box.createVerticalStrut(14));

        // Category
        form.add(fieldLbl("Catégorie"));
        cbCategory = Theme.styledComboBox();
        for (Category c : Category.values()) cbCategory.addItem(c);
        cbCategory.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cbCategory.setAlignmentX(LEFT_ALIGNMENT);
        form.add(cbCategory);
        form.add(Box.createVerticalStrut(14));

        // Price
        form.add(fieldLbl("Prix (€)"));
        tfPrice = Theme.styledTextField();
        tfPrice.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        tfPrice.setAlignmentX(LEFT_ALIGNMENT);
        form.add(tfPrice);
        form.add(Box.createVerticalGlue());

        return form;
    }

    // ── Actions ───────────────────────────────────────────────

    private void addDish() {
        try {
            controller.addDish(buildDishFromForm());
            refreshTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Plat ajouté.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateDish() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Sélectionnez un plat.", "Erreur", JOptionPane.ERROR_MESSAGE); return; }
        try {
            Dish dish = buildDishFromForm();
            dish.setId((int) tableModel.getValueAt(row, 0));
            controller.updateDish(dish);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Plat mis à jour.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteDish() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Sélectionnez un plat.", "Erreur", JOptionPane.ERROR_MESSAGE); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Supprimer ce plat ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.deleteDish(id);
            refreshTable();
            clearForm();
        }
    }

    private void filterByCategory(String cat) {
        tableModel.setRowCount(0);
        List<Dish> dishes = cat.equals("Tous") ? controller.getAllDishes()
                : controller.getDishesByCategory(Category.valueOf(cat));
        for (Dish d : dishes) addRow(d);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Dish d : controller.getAllDishes()) addRow(d);
    }

    private void addRow(Dish d) {
        tableModel.addRow(new Object[]{d.getId(), d.getName(), d.getCategory(), String.format("%.2f €", d.getPrice())});
    }

    private void fillFormFromSelection() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int id = (int) tableModel.getValueAt(row, 0);
        controller.getDish(id).ifPresent(d -> {
            tfName.setText(d.getName());
            cbCategory.setSelectedItem(d.getCategory());
            tfPrice.setText(String.valueOf(d.getPrice()));
        });
    }

    private Dish buildDishFromForm() {
        double price;
        try { price = Double.parseDouble(tfPrice.getText().trim()); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("Prix invalide."); }
        return new Dish(0, tfName.getText().trim(), (Category) cbCategory.getSelectedItem(), price);
    }

    private void clearForm() {
        tfName.setText(""); tfPrice.setText("");
        cbCategory.setSelectedIndex(0); table.clearSelection();
    }

    private JLabel fieldLbl(String text) {
        JLabel lbl = new JLabel(text.toUpperCase());
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(Theme.TEXT_MUTED);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }
}
