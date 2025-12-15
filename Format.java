//Formatting class to introduce colours to the programme for better UX
public class Format 
{
    //ANSI code to reset colour formats
    static final String ANSI_RESET =            "\u001B[0m";

    //ANSI codes for coloured text
    public static final String TXT_BLACK =      "\u001B[30m";
    public static final String TXT_RED =        "\u001B[31m";
    public static final String TXT_GREEN =      "\u001B[32m";
    public static final String TXT_YELLOW =     "\u001B[33m";
    public static final String TXT_BLUE =       "\u001B[34m";
    public static final String TXT_PURPLE =     "\u001B[35m";
    public static final String TXT_CYAN =       "\u001B[36m";
    public static final String TXT_WHITE =      "\u001B[37m";

    //ANSI codes for coloured backgrounds
    public static final String BG_BLACK =       "\u001B[40m";
    public static final String BG_RED =         "\u001B[41m";
    public static final String BG_GREEN =       "\u001B[42m";
    public static final String BG_YELLOW =      "\u001B[43m";
    public static final String BG_BLUE =        "\u001B[44m";
    public static final String BG_PURPLE =      "\u001B[45m";
    public static final String BG_CYAN =        "\u001B[46m";
    public static final String BG_WHITE =       "\u001B[47m";

    //ANSI codes for screen manipulations
    public static final String CURSOR_RESET =   "\033[H";   //Moves the cursor to the top left corner of the terminal
    public static final String SCREEN_CLEAR =   "\033[J";   //Clears the terminal screen
    public static final String SCREEN_RESET =   CURSOR_RESET + SCREEN_CLEAR;    //Clears the terminal screen and resets the cursor position
    
}//end of Format class
