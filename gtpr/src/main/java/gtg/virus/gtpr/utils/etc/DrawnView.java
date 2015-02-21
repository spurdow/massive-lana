package gtg.virus.gtpr.utils.etc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import java.util.List;

/**
 * Created by DavidLuvelleJoseph on 2/22/2015.
 */
public class DrawnView extends View {

    public int width;
    public  int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    Context context;
    private Paint circlePaint;
    private Path circlePath;

    Paint mPaint = new Paint(){{
        setAntiAlias(true);
        setDither(true);

        setStyle(Paint.Style.STROKE);
        setStrokeJoin(Paint.Join.ROUND);
        setStrokeCap(Paint.Cap.ROUND);
        setStrokeWidth(5);
    }};


    List<Float> x;
    List<Float> y;

    public DrawnView(Context context , int color , List<Float> x , List<Float> y) {
        super(context);
        this.x = x;
        this.y = y;
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        circlePaint = new Paint();
        circlePath = new Path();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(4f);
        mPaint.setColor(color);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawPath(mPath, mPaint);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();


        if(x.size() > 0) {
            mPath.moveTo(x.get(0), y.get(0));
            for (int i = 1; i < x.size(); i++) {
                mPath.lineTo(x.get(i), y.get(i));
            }

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);

        canvas.drawPath( mPath,  mPaint);

        canvas.drawPath( circlePath,  circlePaint);
    }

}
