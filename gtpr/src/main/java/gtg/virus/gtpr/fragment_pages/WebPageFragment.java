package gtg.virus.gtpr.fragment_pages;


import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import gtg.virus.gtpr.R;

public class WebPageFragment extends Fragment {

    private static final String TAG = WebPageFragment.class.getSimpleName();
    private WebView mWebView;

    public final static String DATA_FILTER = "_data_filter";
    public final static String PATH_FILTER = "_path_filter";

    private String data = null ;

    private String baseUrl = null;

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

        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE , null);


        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Bundle extras = getArguments();
        if(extras.containsKey(DATA_FILTER)){
            data = extras.getString(DATA_FILTER);
            baseUrl = extras.getString(PATH_FILTER);
        }

        Log.w(TAG , "Path " + baseUrl );
        final String type = "text/html";
        final String encoding = "utf-8";
        mWebView.setScrollContainer(false  );
        mWebView.loadDataWithBaseURL(baseUrl ,  data, type, encoding , null );


    }
}
