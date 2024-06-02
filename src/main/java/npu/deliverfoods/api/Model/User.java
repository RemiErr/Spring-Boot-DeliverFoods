package npu.deliverfoods.api.Model;

public class User {

    // Database Columns
    private Long user_id;
    private String user_name;
    private String email;
    private String phone;
    private String address;
    private String password;

    public User() {
    }

    public User(String name, String password) {
        this.user_name = name;
        this.password = password;
    }

    public User(Long uid, String name, String email, String phone, String address, String password) {
        this.user_id = uid;
        this.user_name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
    }

    // Getter & Setter
    public Long getId() {
        return this.user_id;
    }

    public void setId(Long id) {
        this.user_id = id;
    }

    public String getName() {
        return this.user_name;
    }

    public void setName(String name) {
        this.user_name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean checkPassword(String input) {
        return (this.password == input);
    }

}
