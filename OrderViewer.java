//parent class to display orders, configurable by child classes

import java.util.ArrayList;
import java.util.Collections;

public abstract class OrderViewer 
{
    protected FileHandler fileHandler;
    protected boolean arrangeNewestFirst;

    public OrderViewer(boolean arrangeNewestFirst)
    {
        this.fileHandler = new FileHandler();
        this.arrangeNewestFirst = arrangeNewestFirst;
    }

    public void printList(User currentUser)
    {
        ArrayList<String> allOrders = fileHandler.readAllOrders();

        ArrayList<String> filteredList = filter(allOrders, currentUser);

        if (arrangeNewestFirst)
        {
            Collections.reverse(filteredList);
        }
        displayFormatted(filteredList);
    }

    protected abstract ArrayList<String> filter(ArrayList<String> allOrders, User user);

    private void displayFormatted(ArrayList<String> orders)
    {
        if (orders.isEmpty())
        {
            System.out.println(Format.TXT_YELLOW + "No orders found." + Format.ANSI_RESET);
        }
        else
        {
            System.out.println("------------------------------------------------------------");
            for (String order : orders)
            {
                //CHANGE
                //not recognising "|" for some reason
                //FIXED - special character in java, added \\ to fix issue
                String[] parts = order.split("\\|");
                if (parts.length >= 5)
                {
                    System.out.println("\nOrder No:   " + Format.BG_BLUE +parts[0] + Format.ANSI_RESET);
                    
                    String statusColour = parts[1].contains("Waiting") ? Format.TXT_YELLOW : Format.TXT_GREEN;
                    System.out.println("Status:     " + statusColour + parts[1] + Format.ANSI_RESET);
                    
                    System.out.println("User:       " + parts[3]);
                    
                    String fullItemString = parts[4];   //reads full item order string

                    if (fullItemString.contains(" with ")) //splits the display after the drink name
                    {
                        String[] mainParts = fullItemString.split(" with");
                        String drinkName = mainParts[0];    //Latte
                        String allExtras = mainParts[1];    //Oat Milk, syrups, extras shots

                        System.out.println("Item:       " + drinkName);

                        String[] separatedExtras = allExtras.split(",");
                        for (String extra : separatedExtras)
                        {
                            System.out.println("            | " + extra);   //formatted to line up with drink name in the display
                        }
                    }
                    else
                    {
                        System.out.println("Item:       " + fullItemString);
                    }

                    if (parts.length > 5 && !parts[5].equals("null") && !parts[5].isEmpty())
                    {
                        System.out.println("Note:       " + parts[5]);
                    }
                    System.out.println("\n------------------------------------------------------------");
                }
            }
        }
    }

}
