import java.util.Scanner;

//class to help with inputs and keep the programme running.
//loops back and asks for input again if an incompatable one is entered.
public class InputHelper
{
    //CHANGE
    //allocated new scanner in memory, fixed the error for now
    public Scanner scanner; // = new Scanner(System.in);
    //not closed anywhere!

    public InputHelper()
    {
        this.scanner = new Scanner(System.in);
    }

    //prevents empty inputs by mistake
    public String readString(String prompt)
    {
        System.out.println(prompt + ": ");
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt, int min, int max)
    {
        int input;
        while (true)
        {
            System.out.println(prompt + " (" + min + " - " + max + "): ");
            try
            {
                String line = scanner.nextLine();
                input = Integer.parseInt(line.trim());

                if (input >= min && input <= max)
                {
                    return input;
                } 
                else 
                {
                    System.out.println("Input must be between " + min + " and " + max + ". Please try again.");
                }
            }
            catch (NumberFormatException e)
            {
                System.out.print(Format.SCREEN_RESET);
                System.out.println(Format.TXT_RED + "Error: Please enter a valid number." + Format.ANSI_RESET);
            }
        }
    }
}