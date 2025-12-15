//Main class for Home Barista programme

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
                System.out.println
                    ("""
                    1. View active orders
                    2. Edit menus
                    3. Edit profile
                    4. Run app diagnostics
                    """);
                System.out.println(Format.TXT_RED + "5. Exit" + Format.ANSI_RESET);

                int choice = input.readInt("\nSelect", 1, 5);

                switch (choice)
                {
                    case 1 ->   adminViewOrders();
                    case 2 ->   adminEditMenus();
                    case 3 ->   editUserProfile();
                    case 4 ->   {Test.runDiagnostics(); //runs Test.java
                                input.readString(Format.TXT_CYAN + "\nPress enter to return to menu..." + Format.ANSI_RESET);}
                    case 5 ->   running = false;
                }
            } else
            {
                //customer's main menu
                System.out.println
                    ("""
                    1. Create new order
                    2. See menu (View only)
                    3. See my orders (Full history)
                    4. Edit profile
                    """);
                System.out.println(Format.TXT_RED + "5. Exit" + Format.ANSI_RESET);

                int choice = input.readInt("\nSelect",1 , 5);

                switch (choice)
                {
                    case 1 -> customerCreateOrder();
                    case 2 -> customerViewMenu();
                    case 3 -> customerViewHistory();
                    case 4 -> editUserProfile();
                    case 5 -> running = false;
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
            input.readString(Format.TXT_CYAN + "\nNo orders right now. Press enter to go back..." + Format.ANSI_RESET);
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

        System.out.println(Format.TXT_CYAN + "\n\n=====   Options:   =====\n" + Format.ANSI_RESET);
        System.out.println("1. Add new item");
        System.out.println("2. Modify an item (Rename / Delete)");
        System.out.println("3. " + Format.TXT_RED + "Cancel" + Format.ANSI_RESET);

        int option = input.readInt("\nSelect an option", 1, 3);

        if (option == 1)    //add new item
        {
            String newItem = input.readString("\nEnter the name of the new item.");

            int insertItemPosition = input.readInt("\nEnter line number to insert at (1-" + (currentMenu.size() + 1) + ")", 1, currentMenu.size() + 1);
            
            currentMenu.add(insertItemPosition - 1, newItem);
            
            fileHandler.saveMenu(menuType, currentMenu);
            System.out.print(Format.SCREEN_RESET);
            System.out.println(Format.TXT_GREEN + "Item added at line " + insertItemPosition + "." + Format.ANSI_RESET);            
            input.readString("Press enter to continue...");
        } 
        else if (option == 2) //modify menu item
        {
            int targetItemIndex = input.readInt("\nEnter the line number of the item to modify", 1, currentMenu.size());
            String targetItem = currentMenu.get(targetItemIndex - 1);

            System.out.println("\nSelected item: " + Format.TXT_CYAN + targetItem + Format.ANSI_RESET);
            System.out.println("1. Rename item");
            System.out.println("2. Delete item");
            System.out.println("3. Cancel");

            int editItemOption = input.readInt("Action", 1, 3);

            if (editItemOption == 1)    //rename selected item
            {
                String updatedItemName = input.readString("\nEnter the new name for this item:");
                currentMenu.set(targetItemIndex - 1, updatedItemName);  //.set should override this item in the arraylist

                fileHandler.saveMenu(menuType, currentMenu);
                System.out.print(Format.SCREEN_RESET);
                System.out.println(Format.TXT_GREEN + "\nItem renamed successfully, new name: " + Format.ANSI_RESET + updatedItemName);
                input.readString("Press enter to coninue...");
            }
            else if (editItemOption == 2)   //delete selected item
            {
                currentMenu.remove(targetItemIndex - 1);
                fileHandler.saveMenu(menuType, currentMenu);
                System.out.print(Format.SCREEN_RESET);
                System.out.println(Format.TXT_GREEN + "\nItem successfully deleted." + Format.ANSI_RESET);
                input.readString("Press enter to continue...");
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
        String orderNote = input.readString(Format.TXT_CYAN + "\nAdd a note (or press enter to skip)" + Format.ANSI_RESET);
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

        input.readString(Format.TXT_CYAN + "\nPress enter to go back..." + Format.ANSI_RESET);
    }//end of customerViewHistory()

    //method to allow users to change their saved profile information
    private static void editUserProfile()
    {
        boolean editingProfile = true;
        while (editingProfile)
        {
            System.out.print(Format.SCREEN_RESET);
            System.out.println(Format.TXT_CYAN + "=====   EDIT PROFILE   =====" + Format.ANSI_RESET);
            System.out.println(Format.TXT_CYAN + "Current Name: " + Format.ANSI_RESET + currentUser.getFullName());
            
            System.out.println("\nWhat would you like to change?");
            System.out.println
                ("""
                1. First Name
                2. Last Name
                """);
            System.out.println("3. " + Format.TXT_RED + "Return to Main Menu" + Format.ANSI_RESET);

            int choice = input.readInt("\nSelect", 1, 3);

            switch (choice)
            {
                case 1 ->
                {
                    String newFirstName = input.readString("Enter your new First Name:");
                    currentUser.setFirstName(newFirstName);
                    System.out.println(Format.TXT_GREEN + "Success! " + Format.ANSI_RESET + "First Name updated");
                    input.readString(Format.TXT_CYAN + "\nPress enter to continue..." + Format.ANSI_RESET);
                }
                case 2 ->
                {
                    String newLastName = input.readString("Enter your new Last Name:");
                    currentUser.setLastName(newLastName);
                    System.out.println(Format.TXT_GREEN + "Success! " + Format.ANSI_RESET + "Last Name updated");
                    input.readString(Format.TXT_CYAN + "\nPress enter to continue..." + Format.ANSI_RESET);
                }
                case 3 -> editingProfile = false;
            }//end of switch
        }//end of while loop (editingProfile)
    }//end of editUserProfile()
}//Main class

