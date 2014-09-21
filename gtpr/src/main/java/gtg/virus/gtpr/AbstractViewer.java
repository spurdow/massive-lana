package gtg.virus.gtpr;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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

    int sx = 0;
    int sy = 0;
    Rect r = new Rect();
    private boolean editmode = false;

    private DrawView mDraw;

    private class DrawView extends View implements View.OnTouchListener{

        private final String TAG = DrawView.class.getSimpleName();
        Rect r = new Rect();

        Paint p = new Paint(){{
            setAntiAlias(true);
            setDither(true);
            setColor(Color.BLACK);
            setStyle(Paint.Style.STROKE);
            setStrokeJoin(Paint.Join.ROUND);
            setStrokeCap(Paint.Cap.ROUND);
            setStrokeWidth(5);
        }};

        Path path = new Path();

        public DrawView(Context context) {
            super(context);
        }

        public void onDraw(Canvas canvas){
            super.onDraw(canvas);


            canvas.drawPoint(sx , sy , p);

            path.lineTo(sx , sy);

            canvas.drawPath(path , p);





            Log.w(TAG, "Drawing...");
        }



        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                sx = (int) event.getX();
                sy = (int) event.getY();
                r.set(sx,sy,sx,sy);

            }else if(event.getAction()==MotionEvent.ACTION_UP){
                r.set(sx,sy,(int)event.getX(),(int)event.getY());
 

            }else if(event.getAction()==MotionEvent.ACTION_MOVE){
                r.set(sx,sy,(int)event.getX(),(int)event.getY());
                sx = (int) event.getX();
                sy = (int) event.getY();

            }

            invalidate();
            Log.w(TAG , "Painting..");
            return true;
        }


    }

    int ctr = 0;
    private float[] points = new float[10000];
    //private List<Point> points = (List<Point>) new ArrayList<Point>();
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()){

            case android.R.id.home: finish(); break;

            case R.id.opt_menu_edit:
                ViewGroup vg = (ViewGroup)  findViewById(R.id.parent_view_group);
                if(!editmode){
                    // create doodle

                    mDraw  = new DrawView(this);
                    mDraw.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                sx = (int) event.getX();
                                sy = (int) event.getY();

                                points[ctr] = event.getX();
                                ctr = (ctr + 1 ) % points.length -1;
                                points[ctr] = event.getY();
                                ctr = (ctr + 1 ) % points.length -1;

                                r.set(sx,sy,sx,sy);

                            }else if(event.getAction()==MotionEvent.ACTION_UP){
                                r.set(sx,sy,(int)event.getX(),(int)event.getY());
                                sx = -1000;
                                sy = -1000;

                            }else if(event.getAction()==MotionEvent.ACTION_MOVE){
                                r.set(sx,sy,(int)event.getX(),(int)event.getY());
                                sx = (int) event.getX();
                                sy = (int) event.getY();

                                points[ctr] = event.getX();
                                ctr = (ctr + 1 ) % points.length -1;
                                points[ctr] = event.getY();
                                ctr = (ctr + 1 ) % points.length -1;

                            }

                            mDraw.invalidate();
                            Log.w("TEST" , "Painting..");
                            return true;
                        }
                    });
                    mDraw.setFocusable(true);
                    vg.addView(mDraw);


                    editmode = true;
                }else{
                    //erase doodle
                    if(mDraw != null)
                        vg.removeView(mDraw);

                    sx = 0;
                    sy = 0;
                    r = new Rect();

                    editmode = false;
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
