package siopa.ashu.androidsiopa.Model;

/**
 * Created by DELL on 2/21/2018.
 */

public class User {
    private String name;
    private String password;
    private String Phone;
    private String IsStaff;
    private String secureCode;


    public User() {
    }

    public User(String Pname, String Ppassword, String secureCode) {
        name = Pname;
        password = Ppassword;
        IsStaff = "false";
        this.secureCode = secureCode;
    }

    public String getSecureCode() {
        return secureCode;
    }

    public void setSecureCode(String secureCode) {
        this.secureCode = secureCode;
    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Pphone) {
        Phone = Pphone;
    }

    public String setname(String Pname){
        name = Pname;
        return name;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

}
