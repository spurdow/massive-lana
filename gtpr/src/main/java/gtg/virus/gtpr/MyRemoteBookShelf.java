package gtg.virus.gtpr;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import butterknife.InjectView;
import gtg.virus.gtpr.aaentity.AARemoteBook;
import gtg.virus.gtpr.adapters.RemoteShelfAdapter;
import gtg.virus.gtpr.base.BaseFragment;
import gtg.virus.gtpr.entities.User;
import gtg.virus.gtpr.retrofit.Constants;
import gtg.virus.gtpr.retrofit.EbookQueryService;
import gtg.virus.gtpr.retrofit.RemoteQueryEbook;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by DavidLuvelleJoseph on 2/16/2015.
 */
public class MyRemoteBookShelf extends BaseFragment implements RemoteShelfAdapter.OnViewClick {

    @InjectView(R.id.shelf_list_view)
    protected ListView mListView;

    private RemoteShelfAdapter mShelfAdapter;

    @Override
    protected boolean hasOptions() {
        return false;
    }

    @Override
    protected void provideOnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mShelfAdapter = new RemoteShelfAdapter(getActivity());
        mListView.setAdapter(mShelfAdapter);
        mShelfAdapter.setmClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Constants.SERVER)
                .build();

        User user = User.getUser();

        final ProgressDialog dialog  = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.please_wait));
        dialog.show();
        EbookQueryService service = adapter.create(EbookQueryService.class);
        service.queryEbooks(user.remote_id , new Callback<RemoteQueryEbook>() {
            @Override
            public void success(RemoteQueryEbook remotePBooks, Response response) {
                dialog.dismiss();

                if(remotePBooks.getStatus().equals(getString(R.string.success))){
                    List<AARemoteBook> ebooks = remotePBooks.getEntity().getEbook();
                    for(int i = 0 ; i < ebooks.size() ; i++){
                        //RemotePBook pbook = new RemotePBook();

                        mShelfAdapter.addBook(ebooks.get(i));
                    }

                }else{
                    AlertDialog alert = new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.retry))
                            .setMessage(getString(R.string.provide_correct_credentials))
                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                }
                            }).create();

                    alert.show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                dialog.dismiss();
            }
        });

    }

    @Override
    protected int resourceId() {
        return R.layout.shelf_fragment;
    }



    @Override
    public void bookClick(AARemoteBook book, int position) {

    }
}
