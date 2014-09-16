package gtg.virus.gtpr;


import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import gtg.virus.gtpr.entities.PBook;

public abstract class AbstractViewer extends ActionBarActivity{

    protected PBook mBook;

    protected TextView mPageNo;

    protected int mPageCount = 0;


    protected OnActionBarItemClick mAbsClickListener ;

    public interface OnActionBarItemClick{
        void onItemClick(MenuItem item);
        void onItemClick(int resId);

        void onSearch();
        void onSetAlarm();
        void onViewDetails();
    }
    /**
     *
     * @return the layout from R.
     */
    protected abstract int getContentViewResId();

    protected abstract void initializeResources(Bundle saveInstanceState);

    protected abstract void changeViewItem(MenuItem item);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewResId());

        initializeResources(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        getMenuInflater().inflate(R.menu.pdf_viewer_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()){

            case android.R.id.home: finish(); break;

            case R.id.opt_menu_search:
                if(mAbsClickListener != null){
                    mAbsClickListener.onSearch();
                }
                break;

            case R.id.opt_menu_set_alarm:
                changeViewItem(item);
                if(mAbsClickListener != null){
                    mAbsClickListener.onSetAlarm();
                }
                break;

            case R.id.opt_menu_view:
                if(mAbsClickListener != null){
                    mAbsClickListener.onViewDetails();
                }
                break;
        }

        if(mAbsClickListener != null){
            mAbsClickListener.onItemClick(item.getItemId());
            mAbsClickListener.onItemClick(item);
        }

        return super.onOptionsItemSelected(item);
    }


    public void setAbsClickListener(OnActionBarItemClick mAbsClickListener) {
        this.mAbsClickListener = mAbsClickListener;
    }
}
