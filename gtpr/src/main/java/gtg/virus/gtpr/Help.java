package gtg.virus.gtpr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import gtg.virus.gtpr.base.BaseFragment;


public class Help extends BaseFragment {


    @Override
    protected boolean hasOptions() {
        return false;
    }

    @Override
    protected void provideOnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    }

    @Override
    protected int resourceId() {
        return R.layout.coming_soon;
    }
}
