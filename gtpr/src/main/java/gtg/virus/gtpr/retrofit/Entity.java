package gtg.virus.gtpr.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DavidLuvelleJoseph on 2/10/2015.
 */
public class Entity<E> {


    @SerializedName("")
    private List<E> list;

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }

    public Entity() {

    }

}
