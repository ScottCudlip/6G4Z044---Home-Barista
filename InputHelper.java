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

    public int readInt(String prompt, int min, int max)     //custom scanner and prompt for menu choice inputs
    {
        int input;
        while (true)
        {
            System.out.println(prompt + " (" + min + " - " + max + "): ");
            try
            {
                String line = scanner.nextLine();
                input = Integer.parseInt(line.trim());  //reads the next line and converts to an integer if valid, otherwise it's a string

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
                //System.out.print(Format.SCREEN_RESET); //this was causing UX issues if the wrong input was entered it would remove the menu you needed to pick from again
                System.out.println(Format.TXT_RED + "Error: Please enter a valid number." + Format.ANSI_RESET);
            }
        }
    }

    public String readYesNo(String prompt)  //solution to readString allowing y/n prompts to be skipped and orders not created or saved
    {
        while (true)
        {
            String response = readString(prompt);   //getting input from previous string method

            //fix for y/n questions being skippable with any other input and preventing expected programme behaviour
            if (response.equalsIgnoreCase("y") || response.equalsIgnoreCase("n"))
            {
                return response.toLowerCase();
            }
            System.out.println(Format.TXT_RED + "Invalid input. " + Format.ANSI_RESET + "Please enter 'y' or 'n'.");
        }
    }
}