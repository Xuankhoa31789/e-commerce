//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import com.xuka.model.Customer;
import com.xuka.model.User;
import com.xuka.op.CustomerOperation;
import com.xuka.op.UserOperation;

import java.util.List;

public class Main {
    public static <DeleteCustomer> void main(String[] args) {
        CustomerOperation customerOp = CustomerOperation.getInstance();
        UserOperation userOp = UserOperation.getInstance();
//        System.out.println(customerOp.validateMobile("09131123456"));
//        User customerUser = userOp.login("xuka200309", "admin123456");
//        Customer customer = new Customer(customerUser.getUserId(),
//                customerUser.getUserName(),
//                customerUser.getUserPassword(),
//                customerUser.getRegisterTime(),
//                customerUser.getRole(),
//                "example@gmail.com",
//                "0312345678");
//        System.out.println(customer.toString());

//        if (customerOp.registerCustomer("linh2003", "admin1234", "abc@gmail.com", "0302928272")) {
//            System.out.println("successfully.");
//        } else {
//            System.out.println("Failed");
//        }
//        User userObj = userOp.login("linh2003", "admin1234");
//        if (userObj != null) {
//            System.out.println("Login successful: " + userObj.toString());
//        } else {
//            System.out.println("Login failed");
//        }
//    System.out.println(customerOp.getCustomerList(2));
//        CustomerOperation deleteCustomer = new CustomerOperation.getCustomer("customerId", "customerName", "customerPassword", "customerRegisterTime", "customerRole", "customerEmail", "customerMobile");
      customerOp.deleteAllCustomers();
    }
}