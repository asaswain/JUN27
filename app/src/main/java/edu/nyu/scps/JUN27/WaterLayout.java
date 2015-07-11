package edu.nyu.scps.JUN27;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.ViewPropertyAnimator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Random;

public class WaterLayout extends RelativeLayout {
    private ArrayList<FishView> fishViewList = new ArrayList<>();

    private final long ANIMATION_LENGTH = 500L;

    private boolean animationRunning = true;

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

        //FishOnTouchListener tmpOnTouchListener = new FishOnTouchListener();

        for (int i = 0; i < FISH_COUNT; ++i) {
            int speed = 5 + i;
            FishView tmpFish = new FishView(getContext(), fishList[i], speed);
            fishViewList.add(tmpFish);
            addView(tmpFish);

            //tmpFish.setOnTouchListener(tmpOnTouchListener);
        }

        super.layout(0, 0, getWidth(), getHeight());

    }

    public void animateObjects() {
        for (FishView tmpFish : fishViewList) {

            PointF newPoint = getRandCoordinate();
            PointF currentPoint = tmpFish.getCenterPoint();
            double distance = Math.hypot(newPoint.x - currentPoint.x, newPoint.y - currentPoint.y);
            long duration = (long) (tmpFish.getSpeed() * distance);

            pushObject(tmpFish, newPoint.x, newPoint.y, duration);
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
        Random r = new Random();
        int screenWidth = getWidth();
        int screenHeight = getHeight();
        int xRand = r.nextInt(screenWidth) + 1;
        int yRand = r.nextInt(screenHeight) + 1;

        return new PointF(xRand, yRand);
    }

    private void pushObject(final FishView tmpFish, final float xNewCoor, final float yNewCoor, final long duration) {

        PointF tmpCurrentPoint = tmpFish.getCenterPoint();

        if (tmpCurrentPoint == null) {
            tmpCurrentPoint = new PointF(0, 0);
        }

        // I can't figured out how to rotate the fish the correct angle for the new x,y coordinate, so I'm commenting out this code
         /*
        float newAngle = (float) Math.toDegrees(Math.atan2(tmpCurrentPoint.y - yNewCoor, tmpCurrentPoint.x - xNewCoor));
        //float newAngle = (int) (Math.atan2(tmpCurrentPoint.y - yNewCoor, tmpCurrentPoint.x - xNewCoor) * 180 / Math.PI);

        float rotationAngle = (newAngle) - tmpFish.getFacingAngle();
        */

        /* I can't use this code because Android Studio wont let me run with a newer API than 16, can't figure out how to run with API 21
        ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(tmpFish, "rotation", 180);

        ObjectAnimator moveAnim = ObjectAnimator.ofObject(
                WaterLayout.this,
                "center",   //name of field
                new PointFEvaluator(),
                new PointF(tmpCurrentPoint.x, tmpCurrentPoint.y),   //start value
                new PointF(xNewCoor, yNewCoor)                      //end value
        );

        moveAnim.setInterpolator(new LinearInterpolator());
        moveAnim.setDuration(1000L); //milliseconds, defaults to 300

        // Sequence the two animations to play one after the other
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(rotateAnim).before(moveAnim);
        */

        ViewPropertyAnimator rotationAnimator = tmpFish.animate();
        rotationAnimator.setDuration(duration / 2);    //milliseconds
        rotationAnimator.setInterpolator(new LinearInterpolator());

        Runnable endAction = new Runnable() {
            public void run() {
                ViewPropertyAnimator moveAnimator = tmpFish.animate();
                moveAnimator.setDuration(duration / 2);    //milliseconds
                moveAnimator.setInterpolator(new AnticipateInterpolator());
                moveAnimator.x(xNewCoor).y(yNewCoor).rotationBy(0);
            }
        };


        rotationAnimator.x(tmpCurrentPoint.x).y(tmpCurrentPoint.y).rotationBy(90).withEndAction(endAction);

        tmpFish.setCenterPoint(xNewCoor, yNewCoor);
        //tmpFish.setFacingAngle(newAngle);
    }

    public long getANIMATION_LENGTH() {
        return ANIMATION_LENGTH;
    }
}
