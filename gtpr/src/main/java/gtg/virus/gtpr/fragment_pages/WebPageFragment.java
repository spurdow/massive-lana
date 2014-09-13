package gtg.virus.gtpr.fragment_pages;


import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import gtg.virus.gtpr.R;

public class WebPageFragment extends Fragment {

    private WebView mWebView;

    public final static String DATA_FILTER = "_data_filter";
    public final static String PATH_FILTER = "_path_filter";

    private String data = null ;

    public static WebPageFragment newInstance(final String data , final String path){
        WebPageFragment mFrag = new WebPageFragment();

        Bundle args = new Bundle();
        args.putString(DATA_FILTER , data);
        args.putString(PATH_FILTER , path);

        mFrag.setArguments(args);

        return mFrag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View)inflater.inflate(R.layout.epub_fragment_layout, container, false);

        mWebView = (WebView) view.findViewById(R.id.epub_web_view);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final String baseUrl = Environment.getExternalStorageDirectory().getPath()  + "/";
        Bundle extras = getArguments();
        if(extras.containsKey(DATA_FILTER)){
            data = extras.getString(DATA_FILTER);
        }


        final String type = "text/html";
        final String encoding = "UTF-8";

        mWebView.loadDataWithBaseURL(baseUrl, data, type, encoding, null);

    }
}
