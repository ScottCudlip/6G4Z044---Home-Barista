//child of User class

//Admin inherits everything from User parent and assigns the user as an admin
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
        System.out.println("Admin " + Format.TXT_CYAN + getFullName() + Format.ANSI_RESET + " completed order: " + orderID);
    }
}//end of Admin()