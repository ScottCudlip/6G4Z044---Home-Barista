public class MenuUtils 
{
     //method decides is a number is needed after selecting specific extras
    public static boolean isCounted(String itemName)
    {
        String lower = itemName.toLowerCase();
        return //couldn't make work on a single line in brackets
            lower.contains("syrup") || 
            lower.contains("shot") || 
            lower.contains("sugar") ||
            lower.contains("sweetener") ||
            lower.contains("marshmallow");
    }//end of isCounted

    public static String getQuantityPrompt(String itemName)
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
}
