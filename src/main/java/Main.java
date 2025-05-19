//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import com.xuka.model.Customer;
import com.xuka.model.Product;
import com.xuka.model.User;
import com.xuka.op.CustomerOperation;
import com.xuka.op.OrderOperation;
import com.xuka.op.ProductOperation;
import com.xuka.op.UserOperation;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        CustomerOperation customerOp = CustomerOperation.getInstance();
        UserOperation userOp = UserOperation.getInstance();
        ProductOperation prodOp = ProductOperation.getInstance();
        OrderOperation orderOp = OrderOperation.getInstance();
        orderOp.generateTestOrderData();
    }
}