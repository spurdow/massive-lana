package gtg.virus.gtpr.fragment_pages;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AbstractFragmentViewer extends Fragment {

    public final static String DATA_FILTER = "_data_filter";
    public final static String PATH_FILTER = "_path_filter";

    protected String data = null ;

    protected String baseUrl = null;

    public abstract int getResId();

    public abstract void initializeView(View v , LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void overrideActions(Bundle savedInstanceState);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(getResId() , container , false);
        ///////////////////////////////////////////////////////////////////////////////////////
        initializeView(v , inflater , container , savedInstanceState);
        ///////////////////////////////////////////////////////////////////////////////////////
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle extras = getArguments();
        if(extras != null && extras.containsKey(DATA_FILTER)){
            data = extras.getString(DATA_FILTER);
            baseUrl = extras.getString(PATH_FILTER);
        }

        ////////////////////////////////////////////////////////////////////////////////////////
        overrideActions(savedInstanceState);
        ///////////////////////////////////////////////////////////////////////////////////////
    }
}
