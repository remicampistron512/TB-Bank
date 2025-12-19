package bank.daos;

import bank.models.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {

    public Customer createCustomer(String firstName, String lastName) {
        String sqlUser = "INSERT INTO users(first_name, last_name) VALUES (?, ?)";
        String sqlCustomer = "INSERT INTO customers(user_id) VALUES (?)";

        try (Connection cn = ConnectionFactory.getConnection()) {
            cn.setAutoCommit(false);

            long userId;
            try (PreparedStatement ps = cn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) throw new DaoException("User inserted but no generated key returned.");
                    userId = keys.getLong(1);
                }
            }

            try (PreparedStatement ps = cn.prepareStatement(sqlCustomer)) {
                ps.setLong(1, userId);
                ps.executeUpdate();
            }

            cn.commit();
            cn.setAutoCommit(true);

            return new Customer(userId, firstName, lastName);

        } catch (SQLException e) {
            throw new DaoException("Failed to create customer.", e);
        }
    }


    public List<Customer> findAll() {
        String sql = """
        SELECT u.id, u.first_name, u.last_name
        FROM users u
        JOIN customers c ON c.user_id = u.id
        ORDER BY u.last_name, u.first_name
        """;

        List<Customer> customers = new ArrayList<>();

        try (Connection cn = ConnectionFactory.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                long id = rs.getLong("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");

                // Adjust this line if your Customer constructor is different
                customers.add(new Customer(id, firstName, lastName));
            }

            return customers;

        } catch (SQLException e) {
            throw new DaoException("Failed to list customers.", e);
        }
    }


    public Customer findById(long customerId) {
        String sql = """
            SELECT u.id, u.first_name, u.last_name
            FROM users u
            JOIN customers c ON c.user_id = u.id
            WHERE u.id = ?
            """;

        try (Connection cn = ConnectionFactory.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setLong(1, customerId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new DaoException("Customer not found: id=" + customerId);
                return new Customer(
                        rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name")
                );
            }

        } catch (SQLException e) {
            throw new DaoException("Failed to find customer.", e);
        }
    }
}
