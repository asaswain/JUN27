package edu.nyu.scps.JUN27;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;

public class FishView extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap fish1;
    float facingAngle;
    private PointF fishLocation;

    // constructor for fish
    public FishView(Context context, int fishColor) {
        super(context);
        paint.setColor(Color.BLACK);
        Bitmap tmpFish = BitmapFactory.decodeResource(getResources(), fishColor);
        fish1 = makeTransparent(tmpFish, Color.WHITE);
        facingAngle = 90;
        fishLocation = null;
    }

    public float getFacingAngle() {
        return facingAngle;
    }

    public void setFacingAngle(float facingAngle) {
        this.facingAngle = facingAngle;
    }

    public PointF getCenterPoint(){
        return fishLocation;
    }

    public void setCenterPoint(float x, float y){
        fishLocation = new PointF(x,y);
    }

    // make white color in the fish bitmap image transparent
    private Bitmap makeTransparent(Bitmap src, int colorToReplace) {
        int width = src.getWidth();
        int height = src.getHeight();
        int[] pixels = new int[width * height];
        // get pixel array from source
        src.getPixels(pixels, 0, width, 0, 0, width, height);

        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

        // iteration through pixels
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                // get current index in 2D-matrix
                int index = y * width + x;
                int pixel = pixels[index];
                if (pixel == colorToReplace) {
                    //change RGB individually
                    int R = Color.red(pixel);
                    int G = Color.green(pixel);
                    int B = Color.blue(pixel);
                    pixels[index] = Color.argb(0,R,G,B);
                }
            }
        }
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmOut;
    }

    // onLayout is called when the FishView is created and when the WaterLayout calls the layout method of the FishView
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    // called when Android draws the fish
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(fish1, 0, 0, paint);
    }
}