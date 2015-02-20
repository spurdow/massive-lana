package gtg.virus.gtpr.fragment_pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.InjectView;
import gtg.virus.gtpr.R;

public class EpubCoverImageFragment extends AbstractFragmentViewer {

    public final static String TAG = EpubCoverImageFragment.class.getSimpleName();

    @InjectView(R.id.epub_fragment_cover_imageview)
    protected ImageView mImageView;

    public static EpubCoverImageFragment newInstance(final String data , final String path){
        EpubCoverImageFragment mFrag = new EpubCoverImageFragment();

        Bundle args = new Bundle();
        args.putString(DATA_FILTER , data);
        args.putString(PATH_FILTER , path);

        mFrag.setArguments(args);

        return mFrag;
    }

    @Override
    public int getResId() {
        return R.layout.epub_fragment_cover_layout;
    }

    @Override
    public void initializeView(View v, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //mImageView = (ImageView) v.findViewById(R.id.epub_fragment_cover_imageview);

        setHasOptionsMenu(true);
    }

    @Override
    public void overrideActions(Bundle savedInstanceState) {
        mImageView.setScrollContainer(true);
    }

    @Override
    public void checkBookMark(boolean conditional) {

    }

    @Override
    public boolean useButterKnife() {
        return true;
    }


}
