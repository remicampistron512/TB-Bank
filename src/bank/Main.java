import bank.business.BankingService;
import bank.daos.DaoException;
import bank.models.Customer;
import java.util.List;
import java.util.Scanner;
import bank.ui.ConsoleMenus;

void main() {

  var service = new BankingService(/* customerDao, accountDao, ... */);
  var menus = new ConsoleMenus(service);
  menus.run();
}
