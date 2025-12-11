//child of User class

class Admin extends User
{
    public Admin(String username, String password, String firstName, String lastName)
    {
        super(username, password, firstName, lastName);
    }
    
    @Override
    public boolean isAdmin()
    {
        return true;
    }

    //admin-specific method to manage orders
    public void completeOrder(String orderID)
    {
        System.out.println("Admin " + getFullName() + " completed order: " + orderID);
    }
}
