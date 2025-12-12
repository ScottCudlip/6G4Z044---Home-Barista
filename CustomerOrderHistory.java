//child of OrdersViewer
import java.util.ArrayList;

public class CustomerOrderHistory extends OrderViewer
{
    public CustomerOrderHistory()
    {
        super(true);    //arrangeNewestFirst is true
    }

    @Override
    protected ArrayList<String> filter(ArrayList<String> allOrders, User user)
    {
        ArrayList<String> myOrders = new ArrayList<>();

        for (String order : allOrders)
        {
            if (order.contains(user.getFullName()))
                myOrders.add(order);
        }
        return myOrders;
    }
}
