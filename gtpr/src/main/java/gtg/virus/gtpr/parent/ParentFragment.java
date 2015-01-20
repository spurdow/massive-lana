package gtg.virus.gtpr.parent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;


public abstract class ParentFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(resId() , container , false);

        if(useButterKnife()){
            ButterKnife.inject(this , view);
        }

        overrideSetUpView( view , inflater , container , savedInstanceState);

        return view;
    }


    public abstract int resId();

    public abstract boolean useButterKnife();

    public abstract void overrideSetUpView(View createdView , LayoutInflater inflater , ViewGroup container, Bundle saveInstanceState);


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(useButterKnife()) {
            ButterKnife.reset(this);
        }
    }
}
