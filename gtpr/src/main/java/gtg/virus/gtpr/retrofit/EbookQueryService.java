package gtg.virus.gtpr.retrofit;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by DavidLuvelleJoseph on 2/17/2015.
 */
public interface EbookQueryService {

    @FormUrlEncoded
    @POST(Constants.EBOOK_QUERY_SUBDOMAIN)
    public void queryEbooks(@Field("user_id") long user_id , Callback<RemoteQueryEbook> listOfEBooks);
}
