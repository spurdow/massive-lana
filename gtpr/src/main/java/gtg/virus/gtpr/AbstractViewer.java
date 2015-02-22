package gtg.virus.gtpr;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import gtg.virus.gtpr.aaentity.AABook;
import gtg.virus.gtpr.aaentity.AADoodle;
import gtg.virus.gtpr.async.AddNewDoodle;
import gtg.virus.gtpr.entities.PBook;
import gtg.virus.gtpr.utils.color.ColorPickerDialog;
import gtg.virus.gtpr.utils.etc.DrawnView;

public abstract class AbstractViewer extends ActionBarActivity implements ColorPickerDialog.OnColorChangedListener {

    public static final String TAG = AbstractViewer.class.getSimpleName();

    public static final String INDEX_KEY = TAG + ".index_key";

    protected PBook mBook;

    protected TextView mPageNo;

    protected int mPageCount = 0;

    protected ColorPickerDialog mColorDialog=null;

    protected ViewerState mState = ViewerState.Normal;

    public static final String EDIT_COLOR_KEY = "edit_color_key";

    public static final String EDIT_BACKGROUND_COLOR_KEY = "background_color_key";

    public static final String EDIT_TEXT_COLOR_KEY = "text_color_key";

    protected int index_key = -1;

    protected ViewGroup vg;

    protected int current_page;

    protected AABook current_book;

    protected List<AADoodle> current_doodle;

    protected int maxPage = 0;

    protected int current_color = Color.BLACK;

    @Override
    public void colorChanged(String key, int color) {
        if(EDIT_COLOR_KEY.equals(key)) {
            mPaint.setColor(color);
            current_color = color;
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
        vg = (ViewGroup)  findViewById(R.id.parent_view_group);
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

    final List<Float> xPoints = new ArrayList<Float>();
    final List<Float> yPoints = new ArrayList<Float>();
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

                xPoints.add( x);
                yPoints.add( y);
            }
            public void touch_move(float x, float y) {
                float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);
                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                    mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                    mX = x;
                    mY = y;

                    xPoints.add( x);
                    yPoints.add( y);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()){

            case android.R.id.home: finish(); break;

            case R.id.opt_menu_edit:



                if(mState == ViewerState.Drawing){
                    AlertDialog alertDialog = new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.save_doodle))
                            .setMessage(getString(R.string.do_you_want_to_save_draw))
                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    new AddNewDoodle(AbstractViewer.this, xPoints, yPoints, current_book, current_page, current_color) {
                                        @Override
                                        protected void onPostExecute(Boolean aBoolean) {
                                            super.onPostExecute(aBoolean);
                                            if (!aBoolean.booleanValue()) {
                                                Toast.makeText(AbstractViewer.this, "Problem with saving", Toast.LENGTH_SHORT).show();
                                            }

                                            mDrawnView = new DrawnView(AbstractViewer.this, current_color, xPoints, yPoints);
                                            //LinearLayoutParams params = new Linear
                                            vg.addView(mDrawnView);


                                        }
                                    }
                                            .execute();

                                    if (mDraw != null)
                                        vg.removeView(mDraw);
                                    started = false;
                                    sx = Integer.MAX_VALUE;
                                    sy = Integer.MAX_VALUE;
                                    r = new Rect();
                                    canvasState = CanvasState.None;
                                    mState = ViewerState.Normal;
                                    dialog.dismiss();


                                }
                            })
                            .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //erase doodle
                                    if (mDraw != null)
                                        vg.removeView(mDraw);
                                    started = false;
                                    sx = Integer.MAX_VALUE;
                                    sy = Integer.MAX_VALUE;
                                    r = new Rect();
                                    canvasState = CanvasState.None;
                                    mState = ViewerState.Normal;
                                    dialog.dismiss();
                                }
                            })
                            .create();

                    alertDialog.show();
                }
                else if(mState == ViewerState.Normal){
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
                }else {
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

    protected DrawnView mDrawnView = null;
    public void setCurrentPage(int i ){

        if(current_book != null){
            if(current_book.status == 0 && current_book.status != 1){
                current_book.status = 1;
                current_book.save();
            }else if(current_book.status == 1  ){
                current_book.status = 2;
                current_book.save();
            }else if(current_book.status == 2 ){
                current_book.status = 3;
                current_book.save();
            }else if(current_book.status == 3 && maxPage == i){
                current_book.status = 4;
                current_book.save();
            }
        }
        try {
            current_page = i;
            current_doodle = AADoodle.list( current_page , current_book);

            if(mDrawnView != null){
                vg.removeView(mDrawnView);

            }


            if (current_doodle != null && !current_doodle.isEmpty()) {
                List<Float> x = new ArrayList<Float>();
                List<Float> y = new ArrayList<Float>();
                for (int index = 0; index < current_doodle.size(); index++) {
                    x.add(current_doodle.get(index).sx);
                    y.add(current_doodle.get(index).sy);

                }

                mDrawnView = new DrawnView(AbstractViewer.this, current_color, x, y);

                //LinearLayoutParams params = new Linear
                vg.addView(mDrawnView);


            }
        }catch(NullPointerException ex){
            ex.printStackTrace();
        }
    }
}
