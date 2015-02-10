package gtg.virus.gtpr.retrofit;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by DavidLuvelleJoseph on 2/10/2015.
 */
public interface UserLoginService {

    @FormUrlEncoded
    @POST(Constants.USER_LOGIN_SUBDOMAIN)
    public void login(@Field("username") String username , @Field("password") String password , Callback<RemoteLogin> callback);
}
