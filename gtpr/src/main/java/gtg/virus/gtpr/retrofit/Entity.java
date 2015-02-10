package gtg.virus.gtpr.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import gtg.virus.gtpr.entities.User;

/**
 * Created by DavidLuvelleJoseph on 2/10/2015.
 */
public class Entity {

    @SerializedName("users")
    private List<User> listOfUsers;

    public List<User> getListOfUsers() {
        return listOfUsers;
    }

    public void setListOfUsers(List<User> listOfUsers) {
        this.listOfUsers = listOfUsers;
    }

    public Entity() {

    }
}
