package Class;

public class User {
    private Integer id = null;
    private String email;
    private String name;
    private String lastName;
    private String password;

    public User(
            Integer id,
            String email,
            String name,
            String lastName,
            String password
    ) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.password = password;
    }


    public Integer getId() {
        return id;
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
