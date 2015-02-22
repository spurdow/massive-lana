package gtg.virus.gtpr;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;

import butterknife.InjectView;
import gtg.virus.gtpr.base.BaseFragment;

public class GTGAudioBooks extends BaseFragment{


    @InjectView(R.id.audiobook_list)
    protected ListView mListView;



    @Override
    protected boolean hasOptions() {
        return true;
    }

    @Override
    protected void provideOnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    }

    @Override
    protected int resourceId() {
        return R.layout.audiobook_list;
    }


}
