package gtg.virus.gtpr.retrofit;


import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by DavidLuvelleJoseph on 2/22/2015.
 */
public interface FBLoginService {
    @FormUrlEncoded
    @POST(Constants.FB_LOGIN)
    public void onFBLogin(@Field("email") String email , Callback<RemoteLogin> callback );
}
