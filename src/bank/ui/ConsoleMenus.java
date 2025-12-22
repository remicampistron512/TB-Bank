package bank.ui;

import bank.business.BankingService;
import bank.daos.DaoException;
import bank.models.Customer;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class ConsoleMenus {
  private final Scanner in = new Scanner(System.in);
  private static final String CHOICE_TEXT = "Choose: ";
  private final BankingService service;

  public ConsoleMenus(BankingService service) {
    this.service = service;
  }

  public void run() {
    mainMenu();   // blocks until user exits
    System.out.println("Goodbye.");
  }

  private void mainMenu() {
    while (true) {
      System.out.println("\n=== MAIN MENU ===");
      System.out.println("1) Customers");
      System.out.println("2) Accounts");
      System.out.println("0) Exit");

      int choice = readInt(CHOICE_TEXT, 0, 2);
      switch (choice) {
        case 1 -> customersMenu();
        case 2 -> accountsMenu();
        case 0 -> { return; } // exit application
      }
    }
  }

  private int readInt(String prompt, int min, int max) {
    while (true) {
      System.out.print(prompt);
      String s = in.nextLine().trim();
      try {
        int v = Integer.parseInt(s);
        if (v < min || v > max) {
          System.out.printf("Enter %d..%d%n", min, max);
          continue;
        }
        return v;
      } catch (NumberFormatException e) {
        System.out.println("Invalid number.");
      }
    }
  }

  private void accountsMenu() {
    while (true) {
      System.out.println("\n--- Accounts ---");
      System.out.println("1) Open account");
      System.out.println("2) Deposit");
      System.out.println("3) Transfer");
      System.out.println("0) Back");

      int choice = readInt(CHOICE_TEXT, 0, 3);
      switch (choice) {
        case 1 -> openAccountMenu();
        case 2 -> deposit();
        case 3 -> transfer();
        case 0 -> { return; }
      }
    }
  }

  private void transfer() {
  }

  private void deposit() {

  }

  private void openAccountMenu() {
      while (true) {
          System.out.println("\n--- Open an account for which user ? ---");

          listCustomers();

          List<Customer> customersList = service.listCustomers();
          int choice = readInt(CHOICE_TEXT, 0, customersList.size());

          if(choice == 0){
              return;
          } else {
              String accountName = generateAccountName();
              openAccount(customersList.get(choice-1),accountName);
          }

      }
  }



    // Génère: FR-XXXX-XXXX (X = chiffre)
    private String generateAccountName() {
        int part1 = ThreadLocalRandom.current().nextInt(0, 10000); // 0..9999
        int part2 = ThreadLocalRandom.current().nextInt(0, 10000); // 0..9999
        return String.format("FR-%04d-%04d", part1, part2);
    }

    private void openAccount(Customer customer,String accountName) {
        try {
            service.createAccountForCustomer(customer,accountName);
            System.out.print ("An account (" + accountName + ") for "+ customer.getFirstName() + " " + customer.getLastName() + "has been" +
                    "created");
        }  catch (DaoException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private void customersMenu() {
    while (true) {
      System.out.println("\n--- Customers ---");
      System.out.println("1) List customers");
      System.out.println("2) Create customer");
      System.out.println("0) Back");

      int choice = readInt(CHOICE_TEXT, 0, 2);
      switch (choice) {
        case 1 -> listCustomers();
        case 2 -> createCustomerMenu();
        case 0 -> { return; } // back to main menu
      }
    }
  }

  private void createCustomerMenu() {
    while (true) {
      System.out.println("\n--- Customer creation ---");
      System.out.println("0) Back");

      String[] fullName = getFullName();
      if(fullName.length != 0 ) {
          if (!fullName[0].equals("0")) {
              createCustomer(fullName[0], fullName[1]);
          }
          return;
      }
    }
  }

  private String[] getFullName() {


    try {
      System.out.println("Enter full name : ");
      String fullName = in.nextLine().trim();
      String trimmed = fullName.trim();
      return (trimmed.split("\\s+", 2));
    } catch (Exception  e){
      System.out.print("Customer Full name : ");
      return new String[0];
    }
  }

  private void listCustomers() {

    List<Customer> customersList = service.listCustomers();

    if (customersList.isEmpty()) {
      System.out.println("No customers found.");
      return;
    }

    System.out.println("============== Customers ==============");
    System.out.printf("%-4s %-20s %-20s%n", "No.", "First name", "Last name");
    System.out.println("---------------------------------------");

    int i = 1;
    for (Customer customer : customersList) {
      System.out.printf("%-4d %-20s %-20s%n", i++, customer.getFirstName(), customer.getLastName());
    }
    System.out.println("=======================================");

  }

  private void createCustomer(String firstName,String lastName) {
    try {
      service.createCustomer(firstName,lastName);
      System.out.print ("l'utilisateur " + firstName + " " +  lastName +" a été créé");
    }  catch (DaoException e) {
      System.out.println("Database error: " + e.getMessage());
    }
  }

}
