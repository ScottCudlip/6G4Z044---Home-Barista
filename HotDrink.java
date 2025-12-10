//child of drinks class

class HotDrink extends Drinks
{
    public HotDrink(String name)
    {
        super(name);
    }

    @Override
    public String getType()
    {
        return "Hot";
    }
}