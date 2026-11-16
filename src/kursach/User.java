package kursach;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class User implements Serializable {

    private String name;
    private List<Long> openKey;
    private List<Long> secretKey;
    private transient String password;
    private boolean isAuthorised = false;
    private static final long serialVersionUID = 1L;


    public User(String name, List<Long> openKey, List<Long> secretKey, String password) {
        setName(name);
        setOpenKey(openKey);
        setSecretKey(secretKey);
        setPassword(password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Invalid name!");
        this.name = name;
    }

    public List<Long> getOpenKey() {
        return openKey;
    }

    public void setOpenKey(List<Long> openKey) {
        if (openKey == null) throw new IllegalArgumentException("Invalid openKey");
        this.openKey = openKey;
    }

    public List<Long> getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(List<Long> secretKey) {
        if (secretKey == null) throw new IllegalArgumentException("Invalid secretKey");
        this.secretKey = secretKey;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.isBlank()) throw new IllegalArgumentException("Invalid password");
        this.password = password;
    }

    public boolean isAuthorised() {
        return isAuthorised;
    }

    public void setAuthorised(boolean authorised) {
        isAuthorised = authorised;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", openKey=" + openKey +
                ", secretKey=" + (isAuthorised ? secretKey : "?????????????");
    }
}
