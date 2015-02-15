package gtg.virus.gtpr.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by DavidLuvelleJoseph on 2/15/2015.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(resourceId() , container , false);

        ButterKnife.inject(this , v);

        setHasOptionsMenu(hasOptions());

        provideOnCreateView(inflater , container , savedInstanceState);

        return v;
    }

    protected abstract boolean hasOptions();

    protected abstract void provideOnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract int resourceId();





}
