import bank.business.BankingService;
import bank.daos.DaoException;
import bank.models.Customer;
import java.util.List;
import java.util.Scanner;

void main() {
    BankingService bankingService = new BankingService();
    String choice = "init";
    Scanner sc = new Scanner(System.in);
  while(!choice.equalsIgnoreCase("q")){
    System.out.println("Choisissez une action");
    System.out.println("1. afficher les clients");
    System.out.println("2. Créez un client");

    System.out.println("q. quitter");


    System.out.print("Entrez votre choix: ");


      choice = sc.nextLine();
      switch (choice) {
        case "1" -> {
          showCustomers(bankingService);
          System.out.println("Choisissez un client");

          }

        case "2" -> {
          System.out.println("Créez un client");
          String userName = askForFullName(choice,sc);
          if(userName != null){
            String firstName = splitFullName(userName,1) ;
            String lastName = splitFullName(userName,2);
            if(firstName != null && lastName != null){
              createCustomer(bankingService,firstName,lastName);
            } else {
              System.out.println("Rentrez un nom valide");
            }
          } else {
            System.out.println("Rentrez un nom valide");
          }

          continue;

        }

        case "q" -> {
          System.out.print("Au revoir");
        }


      }

    }






}

private void createCustomer(BankingService bankingService, String firstName, String lastName) {
  try {
    bankingService.createCustomer(firstName,lastName);
    System.out.print ("l'utilisateur " + firstName + " " +  lastName +" a été créé");
    }  catch (DaoException e) {
    System.out.println("Database error: " + e.getMessage());
  }
}

private String splitFullName(String fullName, int partNb){


  try {
    if (fullName == null) return null;
    String trimmed = fullName.trim();
    String[] parts = trimmed.split("\\s+", 2);
    if (trimmed.isEmpty()) return null;

    if(partNb == 1){
      return parts[0];
    } else if (partNb == 2){
      return (parts.length == 2) ? parts[1] : null;
    } else {
      return null;
    }
    } catch (Exception  e){
      return null;
  }
}
private String askForFullName(String choice, Scanner sc) {
    while(!choice.equalsIgnoreCase("q")){
      System.out.println("Entrez le nom et prénom du client : ");
      choice = sc.nextLine();
      return choice;
    }
    return null;
}

public void showCustomers(BankingService bankingService) {
  List<Customer> customersList = bankingService.listCustomers();

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
