//Main class for Home Barista

import java.util.ArrayList;

public class Main 
{
    private static final  InputHelper input = new InputHelper();
    private static final FileHandler fileHandler = new FileHandler();

    //stores who is currently logged into the app
    private static User currentUser;

    public static void main(String[] args) 
    {
        System.out.println("=== WELCOME TO HOME BARISTA ===");

        while (currentUser == null)
        {
            String username = input.readString("username");
            String password = input.readString("password");

            if (username.equals("admin") && password.equals("admin123"))
            {
                currentUser = new Admin("admin", "admin123", "Scott", "Cudlip");
            }//if
            else if (username.equals("customer") && password.equals("cust123"))
            {
                currentUser = new Customer("customer", "cust123", "Steve", "Jobs");
            }//else if
            else
            {
                System.out.println("Invalid credentials. Please try again.");
                System.out.println("for testing 'admin'    : admin/admin123");
                System.out.println("for testing 'customer' : customer/cust123");
            }//else
        }//while loop

        System.out.println("Welcome, " + currentUser.getFullName() + "!");

        //main app loop structure
        boolean running = true;
        while (running)
        {
            System.out.println("\n=== MAIN MENU ===");
            
            if (currentUser.isAdmin())
            {
                //admin menu
                System.out.println("1. View active orders");
                System.out.println("2. Edit menus");
                System.out.println("3. Exit");

                int choice = input.readInt("Select", 1, 3);

                switch (choice)
                {
                    case 1 -> adminViewOrders();
                    case 2 -> adminEditMenus();
                    case 3 -> running = false;
                }//switch
            } else
            {
                //customer menu
                System.out.println("1. Create new order");
                System.out.println("2. View menu only");
                System.out.println("3. Exit");

                int choice = input.readInt("Select",1 , 3);

                switch (choice)
                {
                    case 1 -> customerCreateOrder();
                    case 2 -> customerViewMenu();
                    case 3 -> running = false;
                }//switch
            }//else
        }//while loop
        System.out.println("App terminated. Goodbye!");
        //input.close();
    }//main method

    //admin view active orders screen
    private static void adminViewOrders()
    {
        System.out.println("\n=== ACTIVE ORDERS ===");

        ArrayList<String> orders = fileHandler.readAllOrders();

        boolean isWaiting = false;
        if (orders.isEmpty())
        {
            System.out.println("No orders at the moment.");
        } else
        {
            for (String order : orders)
            {
                System.out.println(order);
                if (order.contains("Waiting")) isWaiting = true;
            }
        }//else

        //option to complete the next order in the queue
        if (isWaiting)
        {
            System.out.println("\n----------------------------");
            String ans = input.readString("Mark current order as completed? (y/n)");

            if (ans.equalsIgnoreCase("y"))
            {
                fileHandler.completeNextOrder();
                adminViewOrders(); //refresh view
            }
        } else
        {
            input.readString("\nPress enter to go back...");
        }
    }//adminViewOrders

    //admin edit menus screen
    private static void adminEditMenus()
    {
        System.out.println("\n=== EDIT MENUS ===");
        System.out.println("1. Edit Hot Drinks");
        System.out.println("2. Edit Cold Drinks");
        System.out.println("3. Edit Extras");

        int typeChoice = input.readInt("Select a menu", 1, 3);
        String menuType = (typeChoice == 1) ? "hot" : (typeChoice == 2) ? "cold" : "extras";

        //load the selected menu
        ArrayList<String> currentMenu = fileHandler.loadMenu(menuType);

        System.out.println("\n=== CURRENT " + menuType.toUpperCase() + " MENU ===");
        for (int i = 0; i < currentMenu.size(); i++)
        {
            System.out.println((i + 1) + ". " + currentMenu.get(i));
        }

        System.out.println("\nOptions:");
        System.out.println("1. Add new item");
        System.out.println("2. Remove an item");
        System.out.println("3. Cancel");

        int option = input.readInt("Select an option", 1, 3);

        if (option == 1)
        {
            //add new item
            String newItem = input.readString("Enter the name of the new item");
            currentMenu.add(newItem);
            fileHandler.saveMenu(menuType, currentMenu);
            System.out.println("Item added successfully.");
        } else if (option == 2)
        {
            {
                //remove an item
                int removeIndex = input.readInt("Enter the item number to remove it", 1, currentMenu.size());
                String removedItem = currentMenu.get(removeIndex - 1);
                fileHandler.saveMenu(menuType, currentMenu);
                System.out.println("Removed item successfully: " + removedItem);
            }
        }//else if
    }//adminEditMenus

    //customer create new order screen
    private static void customerCreateOrder()
    {
        System.out.println("\n=== CREATE NEW ORDER ===");

        //Hot/Cold drinks menu selection
        System.out.println("1. Hot Drinks");
        System.out.println("2. Cold Drinks");
        int typeChoice = input.readInt("Choose Type", 1, 2);
        String menuType = (typeChoice == 1) ? "hot" : "cold";

        //drink selection within the menu
        ArrayList<String> validDrinks = displayMenuFormatted(menuType);

        int drinkIndex = input.readInt("Select a drink", 1, validDrinks.size());
        String selectedDrinkName = validDrinks.get(drinkIndex - 1);

        Drinks myDrink;
        if (typeChoice == 1) myDrink = new HotDrink(selectedDrinkName);
        else myDrink = new ColdDrink(selectedDrinkName);

        boolean addingExtras = true;
        while (addingExtras)
        {
            String extrasChoice = input.readString("Would you like to modify your drink or add some extras? (y/n)");
            if (extrasChoice.equalsIgnoreCase("y"))
            {
                ArrayList<String> validExtras = displayMenuFormatted("extras");

                int extrasIndex = input.readInt("Select one modification/extra for your drink order", 1, validExtras.size());
                String selectedExtra = validExtras.get(extrasIndex - 1);

                if (isCounted(selectedExtra))
                {
                    int extraQuantity = input.readInt("How many " + selectedExtra + "s would you like?", 1, 20);
                    myDrink.addExtras(extraQuantity + "x " + selectedExtra);
                }
                System.out.println("Added " + selectedExtra + " to your order.");
            }
            else
            {
                addingExtras = false;
            }
        }
        
        //add notes to a drink order
        String orderNote = input.readString("Add a note (or press enter to skip)");
        String orderId = fileHandler.generateOrderId();
        String orderString = orderId + ", waiting," + currentUser.getFullName() + "," + myDrink.toString() + "," + orderNote;
        
        fileHandler.saveOrder(orderString);
        System.out.println("Order placed successfully! Your order number: " + orderId);
    }//end of customerCreateOrder

    private static void customerViewMenu()
    {
        System.out.println("1. View Hot Drinks");
        System.out.println("2. View Cold Drinks");
        int type = input.readInt("Select menu", 1, 2);
        if (type == 1 ) printMenu("hot");
        else printMenu("cold");
    }

    private static void printMenu(String type)
    {
        ArrayList<String> menu = fileHandler.loadMenu(type);
        for(String m : menu) System.out.println("- " + m);
    }

    private static ArrayList<String> displayMenuFormatted(String menuType)
    {
        ArrayList<String> menuFileLines = fileHandler.loadMenu(menuType);
        ArrayList<String> menuItems = new ArrayList<>();

        int counter = 1;

        System.out.println("\n=== " + menuType.toUpperCase() + " MENU ===");

        for (String menuLine : menuFileLines)
        {
            String menuLineTrimmed = menuLine.trim();

            if (menuLineTrimmed.isEmpty())
            {
                //reading empty lines in menu files and displaying them in the terminal correctly without being numbered as items.
                System.out.println("");
            }
            else if (menuLineTrimmed.startsWith("#"))
            {
                //identifying menu subtitles and formatting them.
                String menuSubtitle = menuLineTrimmed.replace("#", "").trim();
                System.out.println(Format.BG_WHITE + Format.TXT_WHITE + "=== " + menuSubtitle.toUpperCase() + " ===" + Format.ANSI_RESET);
            }
            else
            {
                //finally, displaying the actual menu items and numbering them correctly.
                System.out.println(counter + ". " + menuLineTrimmed);
                menuItems.add(menuLineTrimmed);
                counter++;
            }
        }
        //indexes the actual menu items correctly in the array after removing the subtitles and empty lines.
        return menuItems;
    }//end of displayMenuFormatted

    //method decides is a number is needed after selecting specific extras
    private static boolean isCounted(String itemName)
    {
        String lower = itemName.toLowerCase();
        return //couldn't make work on a single line in brackets
            lower.contains("syrup") || 
            lower.contains("shot") || 
            lower.contains("sugar") ||
            lower.contains("sweetener") ||
            lower.contains("marshmallow");
    }//isCounted

}//Main class

