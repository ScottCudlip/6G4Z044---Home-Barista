//child of Users class

class Customer extends User
{
    public Customer(String username, String password, String firstName, String lastName)
    {
        super(username, password, firstName, lastName);
    }

    //when a user loging is type "customer", they are not an admin
    // will see different menu options
    @Override
    public boolean isAdmin()
    {
        return false;
    }
}