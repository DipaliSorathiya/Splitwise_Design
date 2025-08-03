package org.example.UtilityClasses;

import java.util.Objects;

public class User {
    private String id;
    private String name;
    private String email;

    public User(String id,String name,String email){
        this.id = id;
        this.name = name;
        this.email = email;
    }
    public  String getId() { return id;}
    public String getName() { return name;}
    public String getEmail() { return email;}

    // overrrise equlas()
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof User)) return false;
        User user = (User)o;
        return id.equals(user.id);

    }

    // override hashCode() to generate hash based on user ID
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
