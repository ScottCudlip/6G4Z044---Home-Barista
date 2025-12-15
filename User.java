//parent for Admin and Customer classes

public abstract class User implements Storable
{
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;

    public User(String username, String password, String firstName, String lastName)
    {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public boolean validLogin(String username, String password)
    {
        return this.username.equals(username) && this.password.equals(password);
    }

    public String getFullName()
    {
        return firstName + " " + lastName;
    }

    public String getUsername() //needed to fix data link break. if user changed their name, orders wouldn't be linked to them anymore
    {
        return username;
    }

    //setters for users to change their profile name
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    //implementing Storable interface
    @Override
    public String toFileFormat()
    {
        return username + "|" + password + "|" + firstName + "|" + lastName;
    }

    //determines what options the user has access to
    public abstract boolean isAdmin();
}
