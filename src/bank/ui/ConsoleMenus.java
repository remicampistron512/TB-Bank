package bank.ui;

import bank.business.BankingService;
import java.util.Scanner;

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
        case 1 -> openAccount();
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

  private void openAccount() {

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
        case 2 -> createCustomer();
        case 0 -> { return; } // back to main menu
      }
    }
  }

  private void listCustomers() {
  }

  private void createCustomer() {
    
  }

}
