import java.util.ArrayList;

public class Test 
{
    private static int testsRun = 0;
    private static int testsPassed = 0;

    public static void runDiagnostics()
    {
        testsRun = 0;
        testsPassed = 0;

        System.out.println
        (Format.BG_CYAN + Format.TXT_BLACK +  """
        ================================================
        ========        APP DIAGNOSTICS         ========
        ================================================
        """ + Format.ANSI_RESET);

        testDrinkObjects();
        testMenuUtils();
        testFileConnections();
        testUserPermissions();

        System.out.println("\n================================================");
        System.out.println("TESTS COMPLETED:" + testsRun);
        if (testsPassed == testsRun)
        {
            System.out.println("RESULT: " + Format.TXT_GREEN + "ALL TESTS PASSED:" + Format.ANSI_RESET);
        }
        else
        {
            System.out.println("RESULT: " + Format.TXT_RED + "FAILURES FOUND " + Format.ANSI_RESET + "(" + testsPassed + "/" + testsRun + " PASSED)");
        }
        System.out.println("================================================\n");
    }

    private static void testDrinkObjects()
    {
        System.out.println("\n=== TESTING DRINK MODELS (INHERITANCE) ===");

        //TEST 1: Chekcs the creation of a HotDirnk object
        try 
        {
            HotDrink coffee = new HotDrink("Latte");
            coffee.addExtras("Oat Milk");

            String result = coffee.toString();
            String expectedResult = "Latte (Hot) with Oat Milk";

            check("Hot Drink Formatting", result.equals(expectedResult));
        }
        catch (Exception e)
        {
            check("Hot Drink Formatting", false);
        }

        //TEST 2: Checks the creation of a ColdDrink object
        try
        {
            ColdDrink iced = new ColdDrink ("Iced Latte");
            iced.addExtras("Whipped Cream");
            iced.addExtras("1x Caramel syrup");

            String result = iced.toString();
            String expectedResult = "Iced Latte (Cold) with Whipped Cream, 1x Caramel Syrup";

            check("Cold drink with multiple extras", result.equals(expectedResult));
        }
        catch (Exception e)
        {
            check("Cold drink with multiple extras", false);
        }
    }//end of testDrinObjects()

    //TESTING UTILS
    private static void testMenuUtils()
    {
        System.out.println("\n=== TESTING MENU UTILITIES ===");

        //TEST 3: Checks if "syrups" are recognised and asked for number of pumps
        boolean result1 = MenuUtils.isCounted("Caramel Syrup");
        check("Logic: Syrup is counted", result1 == true);

        //TEST 4: Checks if a milk change is asked for a number input (it shouldn't)
        boolean result2 = MenuUtils.isCounted("Oat Milk");
        check("Logic: Milk changes are NOT counted", result2 == false);

        //TEST 5: Prompt grammar check for specific item "syrups"
        String prompt = MenuUtils.getQuantityPrompt("Caramel Syrup");
        check("Grammar: Syrup prompt makes sense", prompt.contains("pumps"));

        //TEST 5: Prompt grammar check for specific item "extra coffee shot"
        String prompt2 = MenuUtils.getQuantityPrompt("Extrta Coffee Shot");
        check("Grammar: Shot prompt makes sense", prompt2.contains("shots"));
    }//End of testMenuUtils()

    private static void testFileConnections()
    {
        System.out.println("\n=== TESTING FILE MANAGEMENT ===");

        FileHandler fh = new FileHandler();

        //TEST 6: Can the hot menu file be found and read
        ArrayList<String> menu = fh.loadMenu("hot");
        check("File: Read hot menu", !menu.isEmpty());

        //TEST 7: Can the orders file be read
        try
        {
            ArrayList<String> orders = fh.readAllOrders();
            check("File: Connection to orders file", orders != null);
        }
        catch (Exception e)
        {
            check("File: Connection to orders file", false);
        }
    }//end of testFileConnections()

    private static void testUserPermissions()
    {
        System.out.println("\n=== TESTING USER PERMISSIONS");

        User admin = new Admin("a", "b", "Admin", "User");
        User customer = new Customer("c", "d", "Customer", "User");

        //TEST 8: Admin permission check
        check("Permission: Admin is true", admin.isAdmin() == true);

        //TEST 9: Customer permission check
        check("Permission: Customer is not Admin", customer.isAdmin() == false);
    }//end of testingUSerPermissions()

    private static void check(String testName, boolean condition)
    {
        testsRun++; //every time a check is run, the counter increases
        if (condition) //if the test is successful
        {
            System.out.print(Format.TXT_GREEN + "PASS" + Format.ANSI_RESET);
            testsPassed++;
        }
        else
        {
            System.out.print(Format.TXT_RED + "FAIL" + Format.ANSI_RESET);
        }
        System.out.println("\tTest " + testsRun + ": " + testName + "... ");
    }//end of check()
}//end of Test.java
