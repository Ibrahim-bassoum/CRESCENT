package controller;

import dao.CustomerDao;
import model.Customer;

import java.util.List;
import java.util.Optional;

/**
 * Contrôleur gérant la logique métier des clients.
 */
public class CustomerController {

    private final CustomerDao customerDao = new CustomerDao();

    public void addCustomer(Customer customer) {
        validate(customer);
        customerDao.save(customer);
    }

    public void updateCustomer(Customer customer) {
        validate(customer);
        customerDao.update(customer);
    }

    public void deleteCustomer(int id) {
        customerDao.delete(id);
    }

    public Optional<Customer> getCustomer(int id) {
        return customerDao.findById(id);
    }

    public List<Customer> getAllCustomers() {
        return customerDao.findAll();
    }

    public List<Customer> searchCustomers(String name) {
        return customerDao.searchByName(name);
    }

    private void validate(Customer customer) {
        if (customer.getFullName() == null || customer.getFullName().isBlank()) {
            throw new IllegalArgumentException("Le nom du client est obligatoire.");
        }
        if (customer.getPhone() == null || customer.getPhone().isBlank()) {
            throw new IllegalArgumentException("Le téléphone est obligatoire.");
        }
        if (customer.getPhone().length() < 8) {
            throw new IllegalArgumentException("Le numéro de téléphone est invalide.");
        }
    }
}
