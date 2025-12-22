package bank.daos;

import bank.models.Account;
import bank.models.Customer;
import bank.models.Operation;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

public class AccountDao {
    public Account createAccount(Customer customer, String accountNumber) {
        String sqlAccount = "INSERT INTO accounts(customer_id, account_number) VALUES (?, ?)";

        Connection cn = null;
        try {
            cn = ConnectionFactory.getConnection();
            cn.setAutoCommit(false);

            System.err.println("SQL: " + sqlAccount);
            System.err.println("params: [1]=" + customer.getUserId() + ", [2]=" + accountNumber);

            try (PreparedStatement ps = cn.prepareStatement(sqlAccount)) {
                ps.setLong(1, customer.getUserId());   // Vérifie si ce doit être customer.getId()
                ps.setString(2, accountNumber);
                ps.executeUpdate();
            }

            cn.commit();

            List<Operation> operations = List.of(); // mieux que null
            return new Account(
                    accountNumber,
                    customer,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    operations
            );

        } catch (SQLException e) {
            if (cn != null) {
                try { cn.rollback(); } catch (SQLException ignored) {}
            }
            throw new DaoException("Failed to create account.", e);
        } finally {
            if (cn != null) {
                try { cn.close(); } catch (SQLException ignored) {}
            }
        }
    }





}

