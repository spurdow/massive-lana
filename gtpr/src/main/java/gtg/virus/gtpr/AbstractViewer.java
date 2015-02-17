package gtg.virus.gtpr;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import gtg.virus.gtpr.entities.PBook;
import gtg.virus.gtpr.utils.color.ColorPickerDialog;

public abstract class AbstractViewer extends ActionBarActivity implements ColorPickerDialog.OnColorChangedListener {

    protected PBook mBook;

    protected TextView mPageNo;

    protected int mPageCount = 0;

    protected ColorPickerDialog mColorDialog=null;

    protected ViewerState mState = ViewerState.Normal;

    public static final String EDIT_COLOR_KEY = "edit_color_key";

    public static final String EDIT_BACKGROUND_COLOR_KEY = "background_color_key";

    public static final String EDIT_TEXT_COLOR_KEY = "text_color_key";

    @Override
    public void colorChanged(String key, int color) {
        if(EDIT_COLOR_KEY.equals(key)) {
            mPaint.setColor(color);
        }
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
        ButterKnife.inject(this);
        initializeResources(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mColorDialog = new ColorPickerDialog(this , this , EDIT_COLOR_KEY , mInitialColor , mDefaultColor);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        getMenuInflater().inflate(R.menu.pdf_viewer_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    int sx = Integer.MAX_VALUE;
    int sy = Integer.MAX_VALUE;
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



    private DrawView mDraw;

    private int mInitialColor = Color.BLACK;
    private int mDefaultColor = Color.WHITE;

    public enum CanvasState{
        None,
        Down,
        Moving
    }

    private CanvasState canvasState = CanvasState.None;


        public class DrawView extends View {

            public int width;
            public  int height;
            private Bitmap  mBitmap;
            private Canvas  mCanvas;
            private Path    mPath;
            private Paint   mBitmapPaint;
            Context context;
            private Paint circlePaint;
            private Path circlePath;

            public DrawView(Context c) {
                super(c);
                context=c;
                mPath = new Path();
                mBitmapPaint = new Paint(Paint.DITHER_FLAG);
                circlePaint = new Paint();
                circlePath = new Path();
                circlePaint.setAntiAlias(true);
                circlePaint.setColor(Color.BLUE);
                circlePaint.setStyle(Paint.Style.STROKE);
                circlePaint.setStrokeJoin(Paint.Join.MITER);
                circlePaint.setStrokeWidth(4f);


            }

            @Override
            protected void onSizeChanged(int w, int h, int oldw, int oldh) {
                super.onSizeChanged(w, h, oldw, oldh);

                mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                mCanvas = new Canvas(mBitmap);

            }
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);

                canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);

                canvas.drawPath( mPath,  mPaint);

                canvas.drawPath( circlePath,  circlePaint);
            }

            private float mX, mY;
            private static final float TOUCH_TOLERANCE = 4;

            public void touch_start(float x, float y) {
                mPath.reset();
                mPath.moveTo(x, y);
                mX = x;
                mY = y;
            }
            public void touch_move(float x, float y) {
                float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);
                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                    mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                    mX = x;
                    mY = y;

                    circlePath.reset();
                    circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
                }
            }
            public void touch_up() {
                mPath.lineTo(mX, mY);
                circlePath.reset();
                // commit the path to our offscreen
                mCanvas.drawPath(mPath,  mPaint);
                // kill this so we don't double draw
                mPath.reset();
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
                            float x = event.getX();
                            float y = event.getY();

                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    mDraw.touch_start(x, y);
                                    mDraw.invalidate();
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    mDraw.touch_move(x, y);
                                    mDraw.invalidate();
                                    break;
                                case MotionEvent.ACTION_UP:
                                    mDraw.touch_up();
                                    mDraw.invalidate();
                                    break;
                            }
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
                    sx = Integer.MAX_VALUE;
                    sy = Integer.MAX_VALUE;
                    r = new Rect();
                    canvasState  = CanvasState.None;
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
