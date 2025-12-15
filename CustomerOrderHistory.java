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
            String[] parts = order.split("\\|");

            //14/12/25 fixed data link, username is now index 2
            if (parts.length > 2 && parts[2].equals(user.getUsername()))
            {
                myOrders.add(order);
            }
        }
        return myOrders;
    }
}
