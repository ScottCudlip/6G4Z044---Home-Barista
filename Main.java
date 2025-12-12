//Main class for Home Barista

import java.util.ArrayList;

public class Main 
{
    //create once as 'final' so they can be used anywhere in the Main class
    private static final InputHelper input = new InputHelper();
    private static final FileHandler fileHandler = new FileHandler();
    private static final MenuRenderer menuRenderer = new MenuRenderer(fileHandler);

    //stores who is currently logged into the app
    //Currently supports 2 users, (admin) & (customer)
    private static User currentUser;

    public static void main(String[] args)  //main 
    {
        System.out.print(Format.SCREEN_RESET);  //startup screen
        System.out.println(Format.BG_CYAN + Format.TXT_BLACK + """
                ================================================
                ========        HOME BARISTA APP        ========
                ================================================
                """ + Format.ANSI_RESET);

        while (currentUser == null) //while not logged in
        {
            System.out.println(Format.TXT_YELLOW + "\nWelcome!" + Format.ANSI_RESET + " Please login...");
            String username = input.readString(Format.TXT_CYAN + "\nUsername" + Format.ANSI_RESET);
            String password = input.readString(Format.TXT_CYAN + "\nPassword" + Format.ANSI_RESET);
            
            //CHANGE : I can come back and try adding create a new account functionality and have multiple accounts of the same type
            //Hardcoded logins
            if (username.equals("admin") && password.equals("admin123")) {
                //Admin object created (sees different options)
                currentUser = new Admin("admin", "admin123", "Scott", "Cudlip");
            }
            else if (username.equals("customer") && password.equals("cust123"))
            {
                //Customer object created (standard user functionality)
                currentUser = new Customer("customer", "cust123", "Steve", "Jobs");
            }
            else
            {
                //displays error message if logins are wrong.
                //added tips for lecturers to test user profiles
                System.out.print(Format.SCREEN_RESET);
                System.out.println(Format.TXT_RED + "Invalid credentials. Please try again." + Format.ANSI_RESET);
                System.out.println("\nfor testing 'admin' account..." + Format.TXT_CYAN + "\t\tadmin / admin123" + Format.ANSI_RESET);
                System.out.println("for testing 'customer' account..." + Format.TXT_CYAN + "\tcustomer / cust123" + Format.ANSI_RESET);
                System.out.println("----------------------------------------");
            }
        }//end of login while loop

        System.out.print(Format.SCREEN_RESET);
        System.out.println("Welcome, " + Format.TXT_BLUE + currentUser.getFullName() + Format.ANSI_RESET + "!");
        boolean running = true; //sets the condition of the app to running
        
        while (running) //main loop for running the app
        {
            System.out.print(Format.SCREEN_RESET);
            System.out.println(Format.TXT_CYAN + "=====   MAIN MENU   =====\n" + Format.ANSI_RESET);
            
            if (currentUser.isAdmin())
            {
                //admin's main menu
                System.out.println("""
                        1. View active orders
                        2. Edit menus
                        3. Run App Diagnostics"""); //Runs Test.java
                System.out.println(Format.TXT_PURPLE + "4. Exit" + Format.ANSI_RESET);

                int choice = input.readInt("\nSelect", 1, 4);

                switch (choice)
                {
                    case 1 ->   adminViewOrders();
                    case 2 ->   adminEditMenus();
                    case 3 ->   {Test.runDiagnostics(); 
                                input.readString("\nPress enter to return to menu...");}
                    case 4 -> running = false;
                }
            } else
            {
                //customer's main menu
                System.out.println("""
                        1. Create new order
                        2. See menu (View only)
                        3. See my orders (Full history)""");
                System.out.println(Format.TXT_PURPLE + "4. Exit" + Format.ANSI_RESET);

                int choice = input.readInt("\nSelect",1 , 4);

                switch (choice)
                {
                    case 1 -> customerCreateOrder();
                    case 2 -> customerViewMenu();
                    case 3 -> customerViewHistory();
                    case 4 -> running = false;
                }
            }
        }//end of while(running) loop

        System.out.print(Format.SCREEN_RESET);
        System.out.println(Format.TXT_BLUE + "App terminated." + Format.ANSI_RESET + " Goodbye!");
        
        //scanner.close();
    }//end of main()



    //admin view active orders screen
    private static void adminViewOrders()
    {
        System.out.print(Format.SCREEN_RESET);
        System.out.println(Format.TXT_CYAN + "=====   ACTIVE ORDERS   =====" + Format.ANSI_RESET);

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
        System.out.println(Format.TXT_CYAN + "=====   EDIT MENUS   =====" + Format.ANSI_RESET);
        System.out.println("1. Edit Hot Drinks");
        System.out.println("2. Edit Cold Drinks");
        System.out.println("3. Edit Extras");

        int typeChoice = input.readInt("\nSelect a menu", 1, 3);
        String menuType = (typeChoice == 1) ? "hot" : (typeChoice == 2) ? "cold" : "extras";

        //load the selected menu
        ArrayList<String> currentMenu = fileHandler.loadMenu(menuType);

        System.out.print(Format.SCREEN_RESET);

        System.out.println(Format.TXT_CYAN + "=====   CURRENT " + menuType.toUpperCase() + " MENU   =====" + Format.ANSI_RESET);
        for (int i = 0; i < currentMenu.size(); i++)
        {
            System.out.println((i + 1) + ". " + currentMenu.get(i));
        }

        System.out.println(Format.TXT_CYAN + "\n=====   Options:   =====" + Format.ANSI_RESET);
        System.out.println("1. Add new item");
        System.out.println("2. Remove an item");
        System.out.println("3. " + Format.TXT_PURPLE + "Cancel" + Format.ANSI_RESET);

        int option = input.readInt("\nSelect an option", 1, 3);

        if (option == 1)
        {
            //add new item
            System.out.print(Format.SCREEN_RESET);
            String newItem = input.readString("Enter the name of the new item");
            currentMenu.add(newItem);
            fileHandler.saveMenu(menuType, currentMenu);
            System.out.println(Format.TXT_GREEN + "Item added successfully." + Format.ANSI_RESET);

            //lets the user see the success message from eiditing the menu before leaving the screen
            input.readString("\nPress enter to continue...");
        } else if (option == 2)
        {
            {
                System.out.print(Format.SCREEN_RESET);  //option 2: to remove an item
                int removeIndex = input.readInt("Enter the item number to remove it", 1, currentMenu.size());
                String removedItem = currentMenu.get(removeIndex - 1);

                currentMenu.remove(removeIndex - 1);

                fileHandler.saveMenu(menuType, currentMenu);
                System.out.println(Format.TXT_GREEN + "Removed item successfully: " + Format.ANSI_RESET + removedItem);
            }
        }//else if
    }//end of adminEditMenus()



    private static void customerCreateOrder()   //method to create a new order as a customer
    {
        System.out.print(Format.SCREEN_RESET);
        System.out.println("=== CREATE NEW ORDER ===");

        //Hot/Cold drinks menu selection
        System.out.println("1. Hot Drinks");
        System.out.println("2. Cold Drinks");
        int typeChoice = input.readInt("Choose Type", 1, 2);
        String menuType = (typeChoice == 1) ? "hot" : "cold";

        //drink selection within the menu
        ArrayList<String> validDrinks = menuRenderer.displayFormatted(menuType);

        int drinkIndex = input.readInt("\nSelect a drink", 1, validDrinks.size());
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
                ArrayList<String> validExtras = menuRenderer.displayFormatted("extras");

                int extrasIndex = input.readInt("\nSelect one modification/extra for your drink order", 1, validExtras.size());
                String selectedExtra = validExtras.get(extrasIndex - 1);

                if (MenuUtils.isCounted(selectedExtra))
                {
                    String prompt = MenuUtils.getQuantityPrompt(selectedExtra);
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
        String orderNote = input.readString("\nAdd a note (or press enter to skip)");
        String orderId = fileHandler.generateOrderId();
        
        //
        String orderString = orderId + "|Waiting|" + currentUser.getFullName() + "|" + myDrink.toString() + "|" + orderNote;
        
        fileHandler.saveOrder(orderString);
        System.out.println(Format.TXT_GREEN + "Order placed successfully!" + Format.ANSI_RESET + " Your order number: " + orderId);
    }//end of customerCreateOrder()



    private static void customerViewMenu()
    {
        System.out.print(Format.SCREEN_RESET);
        System.out.println("1. View Hot Drinks");
        System.out.println("2. View Cold Drinks");
        int type = input.readInt("\nSelect menu", 1, 2);
        
        menuRenderer.displayFormatted(type == 1 ? "hot" : "cold");
    }//end of customerViewMenu()



    private static void customerViewHistory()
    {
        System.out.print(Format.SCREEN_RESET);
        System.out.println(Format.TXT_CYAN + "=====   MY ORDER HISTORY   =====" + Format.ANSI_RESET);

        CustomerOrderHistory view = new CustomerOrderHistory();
        view.printList(currentUser);

        input.readString("\nPress enter to go back...");
    }//end of customerViewHistory()

}//Main class

