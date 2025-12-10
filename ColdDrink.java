//child of Drinks class

class ColdDrink extends Drinks
{
    public ColdDrink(String name)
    {
        super(name);
    }

    @Override
    public String getType()
    {
        return "Cold";
    }
}