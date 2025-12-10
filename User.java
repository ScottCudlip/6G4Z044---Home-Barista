//parent for Admin and Customer classes

public abstract class User
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

    //determines what options the user has access to
    public abstract boolean isAdmin();
}
