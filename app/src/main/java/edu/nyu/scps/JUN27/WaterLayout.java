package edu.nyu.scps.JUN27;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Random;

public class WaterLayout extends RelativeLayout {
    private ArrayList<FishView> fishViewList = new ArrayList<>();

    private final long ANIMATION_LENGTH = 1200L;
    // Make rotate and move durations total less than animation length to make sure we finish moving fish before starting next animation
    private final long ROTATE_DURATION = 350L;
    private final long MOVE_DURATION = 750L;

    private PointF touchPoint;

    // used when testing pushObject method
    //private int[] xDest = new int[] {20,400,400,20};
    //private int[] yDest = new int[] {20,20,400,400};
    //private int destCount = 0;

    public WaterLayout(Context context) {
        super(context);

        initializeView();
    }

    // used when constructing object from layout activity_main.xml file
    public WaterLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        initializeView();
    }

    private void initializeView() {

        final int FISH_COUNT = 10;

        int fishList[] = new int[]{
                R.drawable.fish1,
                R.drawable.fish2,
                R.drawable.fish3,
                R.drawable.fish4,
                R.drawable.fish5,
                R.drawable.fish1,
                R.drawable.fish2,
                R.drawable.fish3,
                R.drawable.fish4,
                R.drawable.fish5,
        };

        setBackgroundColor(Color.BLUE);

        touchPoint = null;
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //Put finger's x, y coordinates into touchPoint to make fish go there.
                        touchPoint = new PointF(event.getX(), event.getY());
                        return true;    //do nothing

                    default:
                        return false;
                }
            }
        });

        for (int i = 0; i < FISH_COUNT; ++i) {
            FishView tmpFish = new FishView(getContext(), fishList[i]);
            fishViewList.add(tmpFish);
            addView(tmpFish);
        }

        super.layout(0, 0, getWidth(), getHeight());

    }

    public void animateObjects() {

        PointF newPoint;

        if (touchPoint == null) {
            for (FishView tmpFish : fishViewList) {

                newPoint = getRandCoordinate();
                //PointF currentPoint = tmpFish.getCenterPoint();
                //double distance = Math.hypot(newPoint.x - currentPoint.x, newPoint.y - currentPoint.y);

                pushObject(tmpFish, newPoint.x, newPoint.y);
            }

        } else {
            newPoint = touchPoint;

            for (FishView tmpFish : fishViewList) {
                pushObject(tmpFish, newPoint.x, newPoint.y);
            }

            touchPoint = null;
        }
    }

    // when user clicks on an fish, shove them to a random new x,y coordinate
    /*
    class FishOnTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, final MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    FishView tmpFish = (FishView) v;
                    PointF centerPoint = tmpFish.getCenterPoint();

                    float pushX = motionEvent.getX() - centerPoint.x;
                    pushX = pushX * -200;
                    float pushY = motionEvent.getY() - centerPoint.y;
                    pushY = pushY * -200;

                    // check coordinates against boundaries of view
                    if (pushX < 0) pushX = 0;
                    if (pushX > getWidth()) pushX = getWidth();
                    if (pushX < 0) pushY = 0;
                    if (pushX > getHeight()) pushY = getHeight();

                    double distance = Math.hypot(pushX - centerPoint.x, pushY - centerPoint.y);
                    long duration = (long) (tmpFish.getSpeed() * distance);

                    pushObject(tmpFish, pushX, pushY, duration);

                    return true;

                default:
                    return false;
            }
        }
    }
    */

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        for (FishView tmpFish : fishViewList) {

            final int fishWidth = 60;
            final int fishHeight = 130;

            PointF randPoint = getRandCoordinate();

            //Center the FishView in this BackgroundView.
            //Calls the onLayout method of the FishView.
            //left, top, right, bottom
            tmpFish.layout(
                    (int) randPoint.x - (fishWidth),
                    (int) randPoint.y - (fishHeight),
                    (int) randPoint.x + (fishWidth),
                    (int) randPoint.y + (fishHeight));

            tmpFish.setCenterPoint(randPoint.x, randPoint.y);
        }
    }

    private PointF getRandCoordinate() {
        // testing animation
        //int xRand = xDest[destCount % 4];
        //int yRand = yDest[destCount % 4];

        Random r = new Random();
        int screenWidth = getWidth();
        int screenHeight = getHeight();
        int xRand = r.nextInt(screenWidth) + 1;
        int yRand = r.nextInt(screenHeight) + 1;

        return new PointF(xRand, yRand);
    }

    private void pushObject(final FishView tmpFish, final float xNewCoor, final float yNewCoor) {

        PointF tmpCurrentPoint = tmpFish.getCenterPoint();

        if (tmpCurrentPoint == null) {
            tmpCurrentPoint = new PointF(0, 0);
        }

        float distanceX = xNewCoor - tmpCurrentPoint.x;
        float distanceY = yNewCoor - tmpCurrentPoint.y;

        //3 o'clock is 0 degrees, 12 o'clock is 90 degrees, 6 o'clock is -90 degrees.
        float newAngle = (float)Math.toDegrees(Math.atan2(-distanceY, distanceX));

        //newAngle is in the range
        //-180 <= newAngle <= 180

        if (newAngle < 0f) {
            newAngle += 360f;
        }

        float rotationAngle = tmpFish.getFacingAngle() - newAngle;

        if (rotationAngle < 0f) {
            rotationAngle += 360f;
        }

        //Log.d("old x,y = ", tmpCurrentPoint.x + "," + tmpCurrentPoint.y);
        //Log.d("new x,y = ", xNewCoor + "," + yNewCoor);
        //Log.d("old angle, new angle = ", tmpFish.getFacingAngle() + "," + newAngle);

        ViewPropertyAnimator rotationAnimator = tmpFish.animate();
        rotationAnimator.setDuration(ROTATE_DURATION);    //milliseconds
        rotationAnimator.setInterpolator(new LinearInterpolator());

        rotationAnimator.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ViewPropertyAnimator moveAnimator = tmpFish.animate();
                moveAnimator.setDuration(MOVE_DURATION);    //milliseconds
                moveAnimator.setInterpolator(new LinearInterpolator());
                moveAnimator.x(xNewCoor).y(yNewCoor);
                //Log.d("moveto x,y = ", xNewCoor + "," + yNewCoor);
                /* For use with testing pushObject method
                moveAnimator.setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        destCount += 1;
                    }
                });
                */

            }
        });

        rotationAnimator.rotationBy(rotationAngle);

        tmpFish.setCenterPoint(xNewCoor, yNewCoor);
        tmpFish.setFacingAngle(newAngle);
    }

    public long getANIMATION_LENGTH() {
        return ANIMATION_LENGTH;
    }
}
