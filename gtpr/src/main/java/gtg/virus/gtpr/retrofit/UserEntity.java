package gtg.virus.gtpr.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import gtg.virus.gtpr.entities.User;

/**
 * Created by DavidLuvelleJoseph on 2/17/2015.
 */
public class UserEntity {

    @SerializedName("users")
    private List<User> list;

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }

    public UserEntity() {
    }
}
