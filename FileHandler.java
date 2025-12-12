import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandler
{
    //system files
    private final String FILE_ORDERS = "orders.txt";
    private final String FILE_ID = "id_counter.txt";

    //menu files
    private final String FILE_MENU_HOT = "menu_hot.txt";
    private final String FILE_MENU_COLD = "menu_cold.txt";
    private final String FILE_MENU_EXTRAS = "menu_extras.txt";

    public String generateOrderId()
    {
        int currentNum = 0;

        try (Scanner scanner = new Scanner(new File(FILE_ID)))
        {
            if (scanner.hasNextInt())
            {
                currentNum = scanner.nextInt();
            }
        } catch (FileNotFoundException e)
        {
            //start at 0 if the file doesn't exist yet
        }
        
        //Adds 1 to current number
        currentNum++;

        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_ID)))
        {
            // You asked why 'out.println' and not 'System.out':
            // 'out' is the name of your file writer variable. 
            // 'System.out' prints to the screen. 'out' prints to the text file.
            out.println(currentNum);
        } catch (IOException e)
        {
           System.out.println("Error saving ID: " + e.getMessage());
        }
        
        //returns the custom formatted order ID in the format "HB-"
        return "HB-" + currentNum;
    }

    public ArrayList<String> loadMenu(String type)
    {
        ArrayList<String> menu = new ArrayList<>();
        String filename;

        if (type.equalsIgnoreCase("hot")) filename = FILE_MENU_HOT;
        else if (type.equalsIgnoreCase("cold")) filename = FILE_MENU_COLD;
        else filename = FILE_MENU_EXTRAS;    

        try (Scanner scanner = new Scanner(new File(filename)))
        {
            while (scanner.hasNextLine())
            {
                menu.add(scanner.nextLine().trim());
            } 
        } catch (FileNotFoundException e)
        {
            createDefaultMenu(filename);
            
            //error message for dangerous recursion of method loadMenu so I'm manually forcing it to implement a default menu list if the method is called again
            if (type.equalsIgnoreCase("hot"))
            {
                menu.add("Espresso");
                menu.add("Cappuccino");
                menu.add("Latte");
            }
            else if (type.equalsIgnoreCase("cold"))
            {
                menu.add("Iced Americano");
                menu.add("Iced Latte");
                menu.add("Iced Chocolate");
            }
            else
            {
                menu.add("Extra Shot");
                menu.add("Soy Milk");
                menu.add("Vanilla Syrup");
            }
        }
        return menu;
    }

    private void createDefaultMenu(String filename)
    {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename)))
        {
            switch (filename)
            {
            case FILE_MENU_HOT -> {
                out.println("Espresso");
                out.println("Cappuccino");
                out.println("Latte");
                }
            case FILE_MENU_COLD -> {               
                out.println("Iced Americano");
                out.println("Iced Latte");
                out.println("Iced Chocolate");
                }
            case FILE_MENU_EXTRAS -> {
                out.println("Extra Coffee Shot");
                out.println("Soy Milk");
                out.println("Oat Milk");
                out.println("Coconut Milk");
                out.println("Vanilla Syrup");
                out.println("Caramel Syrup");
                }
            }
        } catch (IOException e)
        {
            System.out.println("Could not create default menu: " + e.getMessage());   
        }
    }

    public void saveMenu(String type, ArrayList<String> newMenu)
    {
        String filename;

        if (type.equalsIgnoreCase("hot")) filename = FILE_MENU_HOT;
        else if (type.equalsIgnoreCase("cold")) filename = FILE_MENU_COLD;
        else filename = FILE_MENU_EXTRAS;

        try (PrintWriter out = new PrintWriter(new FileWriter(filename)))
        {
            for (String item : newMenu)
            {
                out.println(item);
            }
        } catch (Exception e) {
            System.out.println("Error saving menu: " + e.getMessage());
        }
    }

    public void saveOrder(String orderDetails)
    {
        try (FileWriter fw = new FileWriter(FILE_ORDERS, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw))
        {
            out.println(orderDetails);
        } catch (IOException e)
        {
            System.out.println("Error saving order: " + e.getMessage());
        }
    }

    public ArrayList<String> readAllOrders()
    {
        ArrayList<String> orders = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(FILE_ORDERS)))
        {
            while (scanner.hasNextLine())
            {
                orders.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e)
        {
            System.out.println("No orders found.");
        }
        return orders;
    }

    public void completeNextOrder()
    {
        ArrayList<String> allOrders = readAllOrders();
        ArrayList<String> updatedOrders = new ArrayList<>();
        boolean found = false;

        for (String order : allOrders)
        {
            if (!found && order.contains("Waiting"))
            {
                updatedOrders.add(order.replace("Waiting", "Complete"));
                found = true;
            } else
            {
                updatedOrders.add(order);
            }
        }

        if (found)
        {
            try (PrintWriter out = new PrintWriter(new FileWriter(FILE_ORDERS)))
            {
                for (String line : updatedOrders)
                {
                    out.println(line);
                }
            } catch (IOException e)
            {
                System.out.println("Error updating orders: " + e.getMessage());
            }
        } else
        {
            System.out.println("No 'waiting' orders found to complete.");
        }
    }
}