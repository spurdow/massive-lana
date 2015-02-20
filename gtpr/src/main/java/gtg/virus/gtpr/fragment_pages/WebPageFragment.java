package gtg.virus.gtpr.fragment_pages;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bossturban.webviewmarker.TextSelectionSupport;

import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import gtg.virus.gtpr.R;
import gtg.virus.gtpr.aaentity.AABookmark;
import gtg.virus.gtpr.utils.Utilities;

public class WebPageFragment extends AbstractFragmentViewer {

    private static final String TAG = WebPageFragment.class.getSimpleName();

    @InjectView(R.id.epub_web_view)
    protected WebView mWebView;

    @InjectView(R.id.toggle_bookmark)
    protected ToggleButton mToggle;

    protected TextSelectionSupport mTextSelectionSupport;




    public static WebPageFragment newInstance(final String data , final String path , final int page , final String charEncoding){
        WebPageFragment mFrag = new WebPageFragment();

        Bundle args = new Bundle();
        args.putString(DATA_FILTER , data);
        args.putString(PATH_FILTER , path);
        args.putInt(PAGE_FILTER , page);
        args.putString(ENCODING_FILTER , charEncoding);

        mFrag.setArguments(args);

        return mFrag;
    }

    @Override
    public int getResId() {
        return R.layout.epub_fragment_layout;
    }

    @Override
    public void initializeView(View view, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mTextSelectionSupport = TextSelectionSupport.support(getActivity() , mWebView);
        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE , null);
        setHasOptionsMenu(true);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void overrideActions(Bundle savedInstanceState) {
        Log.w(TAG , "Path " + baseUrl );
        final String type = "text/html";

        mTextSelectionSupport.setSelectionListener(new TextSelectionSupport.SelectionListener() {
            @Override
            public void startSelection() {

            }

            @Override
            public void selectionChanged(String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void endSelection() {

            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
                mTextSelectionSupport.onScaleChanged(oldScale, newScale);
            }
        });
        mWebView.getSettings().setDisplayZoomControls(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.loadData( data, type, encoding);


    }

    @Override
    public void checkBookMark(boolean conditional) {
        mToggle.setChecked(conditional);
    }

    @OnCheckedChanged(R.id.toggle_bookmark)
    void onBookMarked(boolean conditional){
        Toast.makeText(getActivity() , "test" , Toast.LENGTH_SHORT).show();
        if(conditional){
            AABookmark aaBookmark =AABookmark.findBookMark(page , book);
            if(aaBookmark == null){
                aaBookmark = new AABookmark();
                aaBookmark.book = book;
                aaBookmark.bookmark_page = page;
                aaBookmark.sentence_sample = mWebView.getTitle();
                long id = aaBookmark.save();
                if(id > 0){
                    Utilities.makeText(getActivity() , getString(R.string.success));
                    checkBookMark(true);
                }else{
                    Utilities.makeText(getActivity(), getString(R.string.failed_to_bookmark ));
                    checkBookMark(false);
                }
            }
        }else{
            AABookmark aaBookmark =AABookmark.findBookMark(page , book);
            if(aaBookmark != null){
                aaBookmark.delete();

            }

            checkBookMark(false);
        }
        Log.w(TAG , "Bookmarked!");
    }

    @Override
    public boolean useButterKnife() {
        return true;
    }


}
