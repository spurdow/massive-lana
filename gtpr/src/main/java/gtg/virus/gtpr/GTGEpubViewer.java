package gtg.virus.gtpr;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gtg.virus.gtpr.adapters.PagerAdapter;
import gtg.virus.gtpr.entities.PBook;
import gtg.virus.gtpr.fragment_pages.WebPageFragment;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

import static gtg.virus.gtpr.utils.Utilities.PIN_EXTRA_PBOOK;

public class GTGEpubViewer extends AbstractViewer implements AbstractViewer.OnActionBarItemClick {

    private final static String TAG = GTGEpubViewer.class.getSimpleName();

    private ViewPager mPager;

    private PagerAdapter mAdapter;

    private List<Fragment> mFragments;

    /**
     * @return the layout from R.
     */
    @Override
    protected int getContentViewResId() {
        return R.layout.epub_main_layout;
    }

    @Override
    protected void initializeResources(Bundle saveInstanceState) {
        mPager = (ViewPager) findViewById(R.id.pager);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String gsonExtra =  extras.getString(PIN_EXTRA_PBOOK);

            mBook = new Gson().fromJson(gsonExtra, PBook.class);
        }

        EpubReader epubReader = new EpubReader();
        Book epubBook = null;
        try {
            epubBook = epubReader.readEpub(new FileInputStream(mBook.getPath()));

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        mFragments = new ArrayList<Fragment>();
        for(int i= 0 ; i < epubBook.getContents().size() ; i++){
            try {
                String data = new String(epubBook.getContents().get(i).getData());
                mFragments.add(WebPageFragment.newInstance(data , mBook.getPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        mAdapter = new PagerAdapter(getSupportFragmentManager() , mFragments);

        mPager.setPageTransformer(false , new PageTransformer());
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                Log.w(TAG , "onPageScrolled");
            }

            @Override
            public void onPageSelected(int i) {
                Log.w(TAG , "onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                Log.w(TAG , "onPageScrollStateChanged");
            }
        });
        mPager.setAdapter(mAdapter);

    }



    @Override
    public void onItemClick(MenuItem item) {

    }

    @Override
    public void onItemClick(int resId) {

    }

    @Override
    public void onSearch() {

    }

    @Override
    public void onSetAlarm() {

    }

    @Override
    public void onViewDetails() {

    }
}
