//subtitles are 78 characters long

import java.util.ArrayList;

public class Test 
{
    private static int testsRun = 0;
    private static int testsPassed = 0;



    public static void runDiagnostics()
    {
        testsRun = 0;
        testsPassed = 0;

        System.out.print(Format.SCREEN_RESET);
        System.out.println
        (Format.BG_CYAN + Format.TXT_BLACK +  """
        ==============================================================================
        ==================             APP DIAGNOSTICS              ==================
        ==============================================================================
        """ + Format.ANSI_RESET);

        testDrinkObjects();
        testMenuUtils();
        testMenuEditing();
        testFileConnections();
        testUserPermissions();
        testProfileEditing();
        testInterfaces();

        System.out.println("\n==============================================================================");
        System.out.println("TESTS COMPLETED:" + testsRun);
        if (testsPassed == testsRun)
        {
            System.out.println("RESULT: " + Format.TXT_GREEN + "ALL TESTS PASSED: 100%" + Format.ANSI_RESET);
        }
        else
        {
            System.out.println("RESULT: " + Format.TXT_RED + "FAILURES FOUND (" + testsPassed + "/" + testsRun + " PASSED)" + Format.ANSI_RESET);
        }
        System.out.println("==============================================================================");
    }



    private static void testDrinkObjects()
    {
        System.out.println(Format.TXT_CYAN + "\n===================   TESTING DRINK MODELS (INHERITANCE)   ===================\n" + Format.ANSI_RESET);

        //TEST: Chekcs the creation of a HotDirnk object
        try 
        {
            HotDrink coffee = new HotDrink("Latte");
            coffee.addExtras("Oat Milk");

            String result = coffee.toString();
            String expectedResult = "Latte (Hot) with Oat Milk";

            check("\tHot Drink Formatting", result.equals(expectedResult));
        }
        catch (Exception e)
        {
            check("\tHot Drink Formatting", false);
        }

        //TEST: Checks the creation of a ColdDrink object
        try
        {
            ColdDrink iced = new ColdDrink ("Iced Latte");
            iced.addExtras("Whipped Cream");
            iced.addExtras("1x Caramel Syrup");

            String result = iced.toString();
            String expectedResult = "Iced Latte (Cold) with Whipped Cream, 1x Caramel Syrup";

            check("\tCold drink with multiple extras", result.equals(expectedResult));
        }
        catch (Exception e)
        {
            check("\tCold drink with multiple extras", false);
        }
    }//end of testDrinObjects()



    //TESTING UTILS
    private static void testMenuUtils()
    {
        System.out.println(Format.TXT_CYAN + "\n=========================   TESTING MENU UTILITIES   =========================\n" + Format.ANSI_RESET);

        //TEST: Checks if "syrups" are recognised and asked for number of pumps
        boolean result1 = MenuUtils.isCounted("Caramel Syrup");
        check("\tSyrups asked for number value", result1 == true);

        //TEST: Checks if a milk change is asked for a number input (it shouldn't)
        boolean result2 = MenuUtils.isCounted("Oat Milk");
        check("\tMilk changes are NOT asked for number value", result2 == false);

        //TEST: Prompt grammar check for specific item "syrups"
        String prompt = MenuUtils.getQuantityPrompt("Caramel Syrup");
        check("\tSyrup prompt makes sense grammatically", prompt.contains("pumps"));

        //TEST: Prompt grammar check for specific item "extra coffee shot"
        String prompt2 = MenuUtils.getQuantityPrompt("Extrta Coffee Shot");
        check("\tShot prompt makes sense grammatically", prompt2.contains("shots"));
    }//End of testMenuUtils()



    private static void testMenuEditing()
    {
        System.out.println(Format.TXT_CYAN + "\n=========================    TESTING MENU EDITING    =========================\n" + Format.ANSI_RESET);

        ArrayList<String> testMenu = new ArrayList<>();
        testMenu.add("Espresso");     //index 0
        testMenu.add("Mocha");   //index 1

        //TEST: Inserting a new item to the menu
        testMenu.add(1, "Latte");

        check("\tMenu item insert: Menu size increased", testMenu.size() == 3);
        check("\tMenu item insert: Item added to the correct index", testMenu.get(1).equals("Latte"));
        check("\tMenu item insert: List items moved after an insertion", testMenu.get(2).equals("Mocha"));

        //TEST: Renaming the selected menu item
        testMenu.set(0, "Americano");
        check("\tMenu item rename: Item updated correctly", testMenu.get(0).equals("Americano"));

        //TEST: Deleting the selected item from the menu
        testMenu.remove(0);
        check("\tMenu item delete: Item removed correctly", !testMenu.contains("Americano"));
    }//end of testMenuEditing()



    private static void testFileConnections()
    {
        System.out.println(Format.TXT_CYAN + "\n========================   TESTING FILE MANAGEMENT    ========================\n" + Format.ANSI_RESET);

        FileHandler fh = new FileHandler();

        //TEST: Can the hot menu file be found and read
        ArrayList<String> menu = fh.loadMenu("hot");
        check("\tHot drinks menu can be read", !menu.isEmpty());

        //TEST: Can the orders file be read
        try
        {
            ArrayList<String> orders = fh.readAllOrders();
            check("\tOrders file can be read", orders != null);
        }
        catch (Exception e)
        {
            check("\tOrders file can be read", false);
        }
    }//end of testFileConnections()



    private static void testUserPermissions()
    {
        System.out.println(Format.TXT_CYAN + "\n========================   TESTING USER PERMISSIONS   ========================\n" + Format.ANSI_RESET);

        User admin = new Admin("a", "b", "Admin", "User");
        User customer = new Customer("c", "d", "Customer", "User");

        //TEST: Admin permission check
        check("\tPermission: Admin's 'isAdmin' is enabled", admin.isAdmin() == true);

        //TEST: Customer permission check
        check("\tPermission: Customer's 'isAdmin' is NOT enabled", customer.isAdmin() == false);
    }//end of testingUSerPermissions()



    private static void testProfileEditing()
    {
        System.out.println(Format.TXT_CYAN + "\n========================   TESTING PROFILE EDITING    ========================\n" + Format.ANSI_RESET);

        User testUser = new Customer("test", "pass", "Old", "Name");

        testUser.setFirstName("New");
        testUser.setLastName("Name");

        String result = testUser.getFullName();
        String expectedResult = "New Name";
    
        check("\tProfile updates correctly", result.equals(expectedResult));
    }//end of testProfileEditing()



    private static void testInterfaces()
    {
        System.out.println(Format.TXT_CYAN + "\n==========================    TESTING INTERFACES    ==========================\n" + Format.ANSI_RESET);

        User testUser = new Customer("jdoe", "1234", "john", "doe");
        Drinks testDrink = new HotDrink("Spanish Latte");

        //both objects should be 'storable' in the same list
        ArrayList<Storable> storableReadyItems = new ArrayList<>();
        storableReadyItems.add(testUser);
        storableReadyItems.add(testDrink);

        //TEST: User formatting to file with interface
        String userFileString = testUser.toFileFormat();
        check("\tInterface: User formats to string, separated by '|'",userFileString.contains("|"));
        check("\tInterface: User string contains matching data",userFileString.contains("1234"));

        //TEST: Drink formatting to file with interface
        String drinkFileString = testDrink.toFileFormat();
        check("\tInterface: Drink formats to string", drinkFileString.equals("Spanish Latte (Hot)"));

        boolean allFormatted = true;
        for (Storable item : storableReadyItems)
        {
            if (item.toFileFormat().isEmpty()) 
            allFormatted = false;
        }
        check("\tInterface: All storable items formatted correctly", allFormatted == true);
    }//end of testInterfaces



    private static void check(String testName, boolean condition)
    {
        testsRun++; //every time a check is run, the counter increases
        if (condition) //if test condition is successful
        {
            System.out.print(Format.TXT_GREEN + "PASS" + Format.ANSI_RESET);
            testsPassed++;
        }
        else    //if the test fails
        {
            System.out.print(Format.TXT_RED + "FAIL" + Format.ANSI_RESET);
        }
        System.out.println("\tTest " + testsRun + ": " + testName + ". ");
    }//end of check()
}//end of Test.java
