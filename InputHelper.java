import java.util.Scanner;

//class to help with inputs and keep the programme running

public class InputHelper
{
    private Scanner scanner;

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
                System.out.println("Error: Please enter a valid number.");
            }
        }
    }
}