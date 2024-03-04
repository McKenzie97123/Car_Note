package Class;

public class User {
    private String email;
    private String name;
    private String lastName;
    private String password;

    public User (String email,
                 String name,
                 String lastName,
                 String password
    ) {
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }
}
