package gtg.virus.gtpr;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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

import gtg.virus.gtpr.entities.PBook;
import gtg.virus.gtpr.utils.color.ColorPickerDialog;

public abstract class AbstractViewer extends ActionBarActivity implements ColorPickerDialog.OnColorChangedListener {

    protected PBook mBook;

    protected TextView mPageNo;

    protected int mPageCount = 0;

    protected ColorPickerDialog mColorDialog=null;

    protected ViewerState mState = ViewerState.Normal;

    @Override
    public void colorChanged(String key, int color) {
        mPaint.setColor(color);
    }

    public enum ViewerState{
        Normal,
        Drawing,
        SetAlarm,
        PdfDetails,
    }

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

        mColorDialog = new ColorPickerDialog(this , this , "colorKey" , mInitialColor , mDefaultColor);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        getMenuInflater().inflate(R.menu.pdf_viewer_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    int sx = -1;
    int sy = -1;
    Rect r = new Rect();

    Paint mPaint = new Paint(){{
        setAntiAlias(true);
        setDither(true);
        setColor(mInitialColor);
        setStyle(Paint.Style.STROKE);
        setStrokeJoin(Paint.Join.ROUND);
        setStrokeCap(Paint.Cap.ROUND);
        setStrokeWidth(5);
    }};

    private boolean editmode = false;

    private DrawView mDraw;

    private int mInitialColor = Color.BLACK;
    private int mDefaultColor = Color.WHITE;

    private class DrawView extends View {

        private final String TAG = DrawView.class.getSimpleName();



        Path path = new Path();

        public DrawView(Context context) {
            super(context);
        }

        public void onDraw(Canvas canvas){
            super.onDraw(canvas);



            canvas.drawPoint(sx , sy , mPaint);

            if(started){
                path.lineTo(sx , sy);

                Log.w(TAG , sx + " " + sy);

                canvas.drawPath(path , mPaint);
            }else{
                path.reset();
                canvas.drawPath(path , mPaint);
            }






            Log.w(TAG, "Drawing...");
        }





    }
    private boolean started = false;
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

                if(mState == ViewerState.Normal){
                    mColorDialog.show();
                    // create doodle

                    mDraw  = new DrawView(this);
                    mDraw.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                sx = (int) event.getX();
                                sy = (int) event.getY();



                            }else if(event.getAction()==MotionEvent.ACTION_UP){

                            }else if(event.getAction()==MotionEvent.ACTION_MOVE){

                                sx = (int) event.getX();
                                sy = (int) event.getY();

                            }

                            mDraw.invalidate();
                            started  = true;
                            Log.w("TEST" , "Painting..");
                            return true;
                        }
                    });
                    mDraw.setFocusable(true);
                    vg.addView(mDraw);


                    mState = ViewerState.Drawing;
                }else{
                    mColorDialog.dismiss();
                    //erase doodle
                    if(mDraw != null)
                        vg.removeView(mDraw);
                    started = false;
                    sx = -1;
                    sy = -1;
                    r = new Rect();

                    mState = ViewerState.Normal;
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
