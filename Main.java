//Main class for Home Barista

import java.util.ArrayList;

public class Main 
{
    private static final InputHelper input = new InputHelper();
    private static final FileHandler fileHandler = new FileHandler();

    //stores who is currently logged into the app
    private static User currentUser;

    public static void main(String[] args) 
    {
        System.out.print(Format.SCREEN_RESET);
        System.out.println("""
                ================================================
                ========        HOME BARISTA APP        ========
                ================================================
                """);

        while (currentUser == null)
        {
            System.out.println(Format.TXT_YELLOW + "\nWelcome!" + Format.ANSI_RESET + " Please login...");
            String username = input.readString("\nusername");
            String password = input.readString("\npassword");

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
                //displays error message if logins are wrong.
                //CHANGE
                //added tips for testing user profiles
                System.out.print(Format.SCREEN_RESET);
                System.out.println(Format.TXT_RED + "Invalid credentials. Please try again." + Format.ANSI_RESET);
                System.out.println("for testing 'admin' account     :use:    admin/admin123");
                System.out.println("for testing 'customer' account  :use:    customer/cust123");
            }//else
        }//while loop

        System.out.print(Format.SCREEN_RESET);
        System.out.println("Welcome, " + Format.TXT_BLUE + currentUser.getFullName() + Format.ANSI_RESET + "!");

        //main app loop structure
        boolean running = true;
        while (running)
        {
            System.out.print(Format.SCREEN_RESET);
            System.out.println("=====   MAIN MENU   =====");
            
            if (currentUser.isAdmin())
            {
                //admin's main menu
                System.out.println("""
                        1. View active orders
                        2. Edit menus""");
                System.out.println(Format.TXT_YELLOW + "3. Exit" + Format.ANSI_RESET);

                int choice = input.readInt("Select", 1, 3);

                switch (choice)
                {
                    case 1 -> adminViewOrders();
                    case 2 -> adminEditMenus();
                    case 3 -> running = false;
                }//switch
            } else
            {
                //customer's main menu
                System.out.println("""
                        1. Create new order
                        2. See menu (View only)
                        3. See my orders (Full history)""");
                System.out.println(Format.TXT_YELLOW + "4. Exit" + Format.ANSI_RESET);

                int choice = input.readInt("Select",1 , 4);

                switch (choice)
                {
                    case 1 -> customerCreateOrder();
                    case 2 -> customerViewMenu();
                    case 3 -> customerViewHistory();
                    case 4 -> running = false;
                }//switch
            }//else
        }//while loop
        System.out.print(Format.SCREEN_RESET);
        System.out.println(Format.TXT_BLUE + "App terminated." + Format.ANSI_RESET + " Goodbye!");
        //input.close();
    }//main method

    //admin view active orders screen
    private static void adminViewOrders()
    {
        System.out.print(Format.SCREEN_RESET);
        System.out.println("=====   ACTIVE ORDERS   =====");

        AdminOrderQueue view = new AdminOrderQueue();
        view.printList(currentUser);

        ArrayList<String> orders = fileHandler.readAllOrders();
        boolean isWaiting = false;
        for (String o : orders) if(o.contains("Waiting")) isWaiting = true;
        
        if (isWaiting)
        {
            System.out.println("\n------------------------------");
            String adminAnswer = input.readString("Mark the current order as completed? (y/n)");
            if (adminAnswer.equalsIgnoreCase("y"))
            {
                fileHandler.completeNextOrder();
                adminViewOrders();
            }
        }
        else
        {
            input.readString("\nNo orders right now. Press enter to go back...");
        }
    }//adminViewOrders

    //admin edit menus screen
    private static void adminEditMenus()
    {
        System.out.print(Format.SCREEN_RESET);
        System.out.println("=====   EDIT MENUS   =====");
        System.out.println("1. Edit Hot Drinks");
        System.out.println("2. Edit Cold Drinks");
        System.out.println("3. Edit Extras");

        int typeChoice = input.readInt("Select a menu", 1, 3);
        String menuType = (typeChoice == 1) ? "hot" : (typeChoice == 2) ? "cold" : "extras";

        //load the selected menu
        ArrayList<String> currentMenu = fileHandler.loadMenu(menuType);

        System.out.print(Format.SCREEN_RESET);

        System.out.println("=====   CURRENT " + menuType.toUpperCase() + " MENU   =====");
        for (int i = 0; i < currentMenu.size(); i++)
        {
            System.out.println((i + 1) + ". " + currentMenu.get(i));
        }

        System.out.println("=====   Options:   =====");
        System.out.println("1. Add new item");
        System.out.println("2. Remove an item");
        System.out.println("3. Cancel");

        int option = input.readInt("Select an option", 1, 3);

        if (option == 1)
        {
            //add new item
            System.out.print(Format.SCREEN_RESET);
            String newItem = input.readString("Enter the name of the new item");
            currentMenu.add(newItem);
            fileHandler.saveMenu(menuType, currentMenu);
            System.out.println(Format.TXT_GREEN + "Item added successfully." + Format.ANSI_RESET);

            //lets the user see the success message from eiditing the menu before leaving the screen
            input.readString("Press enter to continue...");
        } else if (option == 2)
        {
            {
                //remove an item
                System.out.print(Format.SCREEN_RESET);
                int removeIndex = input.readInt("Enter the item number to remove it", 1, currentMenu.size());
                String removedItem = currentMenu.get(removeIndex - 1);

                currentMenu.remove(removeIndex - 1);

                fileHandler.saveMenu(menuType, currentMenu);
                System.out.println(Format.TXT_GREEN + "Removed item successfully: " + Format.ANSI_RESET + removedItem);
            }
        }//else if
    }//adminEditMenus

    //customer create new order screen
    private static void customerCreateOrder()
    {
        System.out.print(Format.SCREEN_RESET);
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
                    String prompt = getQuantityPrompt(selectedExtra);
                    int extrasQuantity = input.readInt(prompt, 1 ,20);

                    myDrink.addExtras(extrasQuantity + "x " + selectedExtra);
                }
                else
                {
                    myDrink.addExtras(selectedExtra);
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
        
        //
        String orderString = orderId + "|Waiting|" + currentUser.getFullName() + "|" + myDrink.toString() + "|" + orderNote;
        
        fileHandler.saveOrder(orderString);
        System.out.println(Format.TXT_GREEN + "Order placed successfully!" + Format.ANSI_RESET + " Your order number: " + orderId);
    }//end of customerCreateOrder

    private static void customerViewMenu()
    {
        System.out.print(Format.SCREEN_RESET);
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

        System.out.print(Format.SCREEN_RESET);
        System.out.println(Format.BG_WHITE + Format.TXT_BLACK + "\n=== " + menuType.toUpperCase() + " MENU ===" + Format.ANSI_RESET);

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
                System.out.println("=== " + menuSubtitle.toUpperCase() + " ===");
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
    }//end of isCounted

    private static String getQuantityPrompt(String itemName)
    {
        String lower = itemName.toLowerCase();
        String extrasCategory = "default";

        if (lower.contains("syrup")) extrasCategory = "syrup";
        else if (lower.contains("shot")) extrasCategory = "shot";
        else if (lower.contains("sugar")) extrasCategory = "sugar";
        else if (lower.contains("sweetener")) extrasCategory = "sweetener";
        else if (lower.contains("marshmallow")) extrasCategory = "marshmallow";

        //Categories of items give different prompts for quantity input
        //used copilot to understand and get the solution to my switch suggesting "convert to switch expression"
        return switch (extrasCategory)
        {
            case "syrup"        -> "How many pumps of " + itemName + " ?";
            case "shot"         -> "How many extras shots?";
            case "sugar"        -> "How many teaspoons of sugar?";
            case "sweetener"    -> "How many sweeteners?";
            case "marshmallow"  -> "How many marshmallows?";
            default -> "Quantity of " + itemName + "?";
        };

    }//end of getQuantityPrompt

    private static void customerViewHistory()
    {
        System.out.print(Format.SCREEN_RESET);
        System.out.println("=====   MY ORDER HISTORY   =====");

        CustomerOrderHistory view = new CustomerOrderHistory();
        view.printList(currentUser);

        input.readString("\nPress enter to go back...");
    }

}//Main class

