package gtg.virus.gtpr.fragment_pages;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.ButterKnife;
import gtg.virus.gtpr.R;
import gtg.virus.gtpr.utils.etc.ActionItem;
import gtg.virus.gtpr.utils.etc.QuickAction;

public abstract class AbstractFragmentViewer extends Fragment {

    public final static String DATA_FILTER = "_data_filter";
    public final static String PATH_FILTER = "_path_filter";

    //action id
    public static final int ID_UP     = 1;
    public static final int ID_DOWN   = 2;
    public static final int ID_SEARCH = 3;
    public static final int ID_INFO   = 4;
    public static final int ID_ERASE  = 5;
    public static final int ID_OK     = 6;

    protected String data = null ;

    protected String baseUrl = null;

    public abstract int getResId();

    public abstract void initializeView(View v , LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void overrideActions(Bundle savedInstanceState);

    public abstract boolean useButterKnife();

    protected ArrayList<ActionItem> actionItemArrayList = new ArrayList<ActionItem>();

    protected QuickAction quickAction;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(getResId() , container , false);

        ActionItem nextItem 	= new ActionItem(ID_DOWN, "Next", getResources().getDrawable(R.drawable.menu_down_arrow));
        ActionItem prevItem 	= new ActionItem(ID_UP, "Prev", getResources().getDrawable(R.drawable.menu_up_arrow));
        ActionItem searchItem 	= new ActionItem(ID_SEARCH, "Find", getResources().getDrawable(R.drawable.menu_search));
        ActionItem infoItem 	= new ActionItem(ID_INFO, "Info", getResources().getDrawable(R.drawable.menu_info));
        ActionItem eraseItem 	= new ActionItem(ID_ERASE, "Clear", getResources().getDrawable(R.drawable.menu_eraser));
        ActionItem okItem 		= new ActionItem(ID_OK, "OK", getResources().getDrawable(R.drawable.menu_ok));

        //create QuickAction. Use QuickAction.VERTICAL or QuickAction.HORIZONTAL param to define layout
        //orientation
        quickAction = new QuickAction(getActivity(), QuickAction.HORIZONTAL);

        //add action items into QuickAction
        quickAction.addActionItem(nextItem);
        quickAction.addActionItem(prevItem);
        quickAction.addActionItem(searchItem);
        quickAction.addActionItem(infoItem);
        quickAction.addActionItem(eraseItem);
        quickAction.addActionItem(okItem);

        //Set listener for action item clicked
        quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {
                ActionItem actionItem = quickAction.getActionItem(pos);

                //here we can filter which action item was clicked with pos or actionId parameter
                if (actionId == ID_SEARCH) {
                    Toast.makeText(getActivity().getApplicationContext(), "Let's do some search action", Toast.LENGTH_SHORT).show();
                } else if (actionId == ID_INFO) {
                    Toast.makeText(getActivity().getApplicationContext(), "I have no info this time", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), actionItem.getTitle() + " selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //set listnener for on dismiss event, this listener will be called only if QuickAction dialog was dismissed
        //by clicking the area outside the dialog.
        quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
            @Override
            public void onDismiss() {
                Toast.makeText(getActivity().getApplicationContext(), "Dismissed", Toast.LENGTH_SHORT).show();
            }
        });

        if(useButterKnife()){
            ButterKnife.inject(this , v);
        }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(useButterKnife()) {
            ButterKnife.reset(this);
        }
    }
}
