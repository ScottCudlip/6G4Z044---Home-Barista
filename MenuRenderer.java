import java.util.ArrayList;

public class MenuRenderer
{
    //VS suggested making final
    private final FileHandler fileHandler;

    public MenuRenderer(FileHandler fh)
    {
        this.fileHandler = fh;
    }

    public ArrayList<String> displayFormatted(String menuType)
    {
        ArrayList<String> menuFileLines = fileHandler.loadMenu(menuType);
        ArrayList<String> selectableItems = new ArrayList<>();

        int counter = 1;

        System.out.print(Format.SCREEN_RESET);
        System.out.println(Format.BG_WHITE + Format.TXT_BLACK + "========== " + menuType.toUpperCase() + " MENU ==========\n" + Format.ANSI_RESET);

        for (String line : menuFileLines)
        {
            String trimmed = line.trim();

            if (trimmed.isEmpty())
            {
                System.out.println("");
            }
            else if (trimmed.startsWith("#"))
            {
                String subTitle = trimmed.replace("#", "").trim();
                System.out.println("===== " + subTitle.toUpperCase() + " =====");
            }
            else
            {
                System.out.println(counter + ". " + trimmed);
                selectableItems.add(trimmed);
                counter++;
            }
        }
        return selectableItems;
    }

}
