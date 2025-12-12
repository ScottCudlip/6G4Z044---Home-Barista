//child of OrderViewer

import java.util.ArrayList;

public class AdminOrderQueue extends OrderViewer
{
    public AdminOrderQueue()
    {
        super(false);   //arrangeNewestFirst is false
    }

    @Override
    protected ArrayList<String> filter(ArrayList<String> allOrders, User user)
    {
        ArrayList<String> waitingOrders = new ArrayList<>();

        for (String order : allOrders)
        {
            if (order.contains("Waiting"))
            {
                waitingOrders.add(order);
            }
        }
        return waitingOrders;
    }
}