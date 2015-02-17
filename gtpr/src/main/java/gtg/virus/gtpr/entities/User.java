package gtg.virus.gtpr.entities;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.SerializedName;

@Table(name = "user" , id = "_id")
public class User extends Model {

    @Column()
    @SerializedName("id")
    public long remote_id;
    @Column()
    public String first_name;
    @Column()
    public String middle_name;
    @Column()
    public String last_name;
    @Column()
    public String picture;
    @Column()
    public String username;
    @Column()
    public String password;
    @Column()
    public String email;
    @Column()
    public int type;
    @Column()
    public String key;
    @Column()
    public int status;
    @Column()
    public String image;

    public User() {
    }


    public String getFullName(){ return first_name + " " + last_name;}

    public static User getUser(){
        return new Select().from(User.class).executeSingle();
    }
}
