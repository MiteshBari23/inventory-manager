package ui;

import model.Product;
import dao.ProductDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InventoryApp extends JFrame {

    private JTextField nameField, quantityField, priceField, idField;
    private JTable table;
    private DefaultTableModel model;
    private ProductDAO dao = new ProductDAO();

    public InventoryApp() {
        setTitle("Inventory Management System");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        idField = new JTextField();
        nameField = new JTextField();
        quantityField = new JTextField();
        priceField = new JTextField();

        formPanel.add(new JLabel("Product ID (for Update/Delete):"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Product Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);
        formPanel.add(new JLabel("Price:"));
        formPanel.add(priceField);

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton viewButton = new JButton("View All");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);

        formPanel.add(buttonPanel);

        add(formPanel, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(new String[]{"ID", "Name", "Quantity", "Price"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Action Listeners
        addButton.addActionListener(e -> addProduct());
        updateButton.addActionListener(e -> updateProduct());
        deleteButton.addActionListener(e -> deleteProduct());
        viewButton.addActionListener(e -> loadProducts());

        setVisible(true);
    }

    private void addProduct() {
        try {
            String name = nameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());

            dao.addProduct(new Product(0, name, quantity, price));
            JOptionPane.showMessageDialog(this, "Product added successfully.");
            loadProducts();
            clearFields();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void updateProduct() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());

            dao.updateProduct(new Product(id, name, quantity, price));
            JOptionPane.showMessageDialog(this, "Product updated.");
            loadProducts();
            clearFields();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void deleteProduct() {
        try {
            int id = Integer.parseInt(idField.getText());
            dao.deleteProduct(id);
            JOptionPane.showMessageDialog(this, "Product deleted.");
            loadProducts();
            clearFields();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void loadProducts() {
        try {
            model.setRowCount(0);
            List<Product> list = dao.getAllProducts();
            for (Product p : list) {
                model.addRow(new Object[]{p.getId(), p.getName(), p.getQuantity(), p.getPrice()});
            }
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        quantityField.setText("");
        priceField.setText("");
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        ex.printStackTrace();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InventoryApp());
    }
}
