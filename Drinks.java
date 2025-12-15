//parent for HotDrink and ColdDrink classes

import java.util.ArrayList;

public abstract class Drinks implements Storable
{
    protected String name;
    protected ArrayList<String> extras;

    public Drinks(String name)
    {
        this.name = name;
        this.extras = new ArrayList<>();
    }

    public void addExtras(String custom)
    {
        extras.add(custom);
    }

    public String getName()
    {
        return name;
    }

    public abstract String getType();

    @Override
    public String toString()
    {
        if (extras.isEmpty()) 
        {
            return name + " (" + getType() + ")";
        } else 
            {
                return name + " (" + getType() + ") with " + String.join(", ", extras);
            }
    }

    @Override   //implementing Storable interface
    public String toFileFormat()
    {
        return this.toString();
    }
}
