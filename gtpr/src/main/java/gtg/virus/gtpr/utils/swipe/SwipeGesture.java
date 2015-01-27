package gtg.virus.gtpr.utils.swipe;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by DavidLuvelleJoseph on 1/28/2015.
 */
public class SwipeGesture  extends GestureDetector.SimpleOnGestureListener{

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;


    private SwipeListener mSwipeListener = null;

    public interface SwipeListener{
        void onSwipeLeft(SwipeGesture swipeGesture);
        void onSwipeRight(SwipeGesture swipeGesture);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result = false;
        try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {

                        if(mSwipeListener != null){
                            mSwipeListener.onSwipeRight(SwipeGesture.this);
                        }
                    } else {

                        if(mSwipeListener != null){
                            mSwipeListener.onSwipeLeft(SwipeGesture.this);
                        }
                    }
                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }


    public void setSwipeListener(SwipeListener mSwipeListener) {
        this.mSwipeListener = mSwipeListener;
    }
}
