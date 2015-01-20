package gtg.virus.gtpr.fragment_pages;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import gtg.virus.gtpr.R;

public class WebPageFragment extends AbstractFragmentViewer {

    private static final String TAG = WebPageFragment.class.getSimpleName();
    private WebView mWebView;

    public static WebPageFragment newInstance(final String data , final String path){
        WebPageFragment mFrag = new WebPageFragment();

        Bundle args = new Bundle();
        args.putString(DATA_FILTER , data);
        args.putString(PATH_FILTER , path);

        mFrag.setArguments(args);

        return mFrag;
    }

    @Override
    public int getResId() {
        return R.layout.epub_fragment_layout;
    }

    @Override
    public void initializeView(View view, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mWebView = (WebView) view.findViewById(R.id.epub_web_view);

        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE , null);

        setHasOptionsMenu(true);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void overrideActions(Bundle savedInstanceState) {
        Log.w(TAG , "Path " + baseUrl );
        final String type = "text/html";
        final String encoding = "utf-8";
        mWebView.getSettings().setDisplayZoomControls(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.loadData( data, type, encoding);

    }


}
