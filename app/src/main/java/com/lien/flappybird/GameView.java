package com.lien.flappybird;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Random;


public class GameView extends View {


    Runnable runnable;
    Bitmap bgImg,topTube,bottomTube;
    Bitmap[] birds = new Bitmap[2];

    int scrHeight, scrWidth;
    Rect scrC;
    int birdFrame = 0;
    Handler handler;
    int UPDATE_TIME = 40;
    int bird_X, bird_Y;
    int velocity_X, velocity_Y;
    int velocity = 30;
    int gravity = 3;
    boolean gamestatus = false;
    int gap =400;
    int minTubeOffset,maxTubeOffset;int numberOfTubes=4;
    int distanceBetweenTubes;
    int[] tubeX= new int[numberOfTubes];
    int[] topTubeY=new int[numberOfTubes];
    Random random;
    int tubeVelocity =8;


    public GameView(Context context) {
        super(context);

        handler = new Handler();
        random = new Random();

        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        bgImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_img);
        topTube = getTubeBitmap(R.drawable.toptube);
        bottomTube = getTubeBitmap(R.drawable.bottomtube);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        scrHeight = displayMetrics.heightPixels;
        scrWidth = displayMetrics.widthPixels;

        scrC = new Rect(0, 0, scrWidth, scrHeight);
        birds[0] = getBirdBitmap(R.drawable.bird);
        birds[1] = getBirdBitmap(R.drawable.bird2);

        bird_X = scrWidth / 2 - birds[0].getWidth() / 2;
        bird_Y = scrHeight / 2 - birds[0].getHeight() / 2;

        distanceBetweenTubes = scrWidth*3/4;
        minTubeOffset= gap/2;
        maxTubeOffset = scrHeight-minTubeOffset-gap;
       for(int i=0;i<numberOfTubes;i++){

           tubeX[i] = scrWidth + i*distanceBetweenTubes;
           topTubeY[i] = minTubeOffset+random.nextInt(maxTubeOffset-minTubeOffset+1);
       }

    }

    private Bitmap getBirdBitmap(int bird) {
        Bitmap b = BitmapFactory.decodeResource(getResources(), bird);
        int width = 100;
        int height = (b.getHeight()*width)/b.getWidth();
        Bitmap b_new = Bitmap.createScaledBitmap(b, width, height, false);
        b.recycle();
        return b_new;
    }

    private Bitmap getTubeBitmap(int tube) {
        Bitmap b = BitmapFactory.decodeResource(getResources(), tube);
        int width = 150;
        int height = (b.getHeight()*width)/b.getWidth();
        Bitmap b_new = Bitmap.createScaledBitmap(b, width, height, false);
        b.recycle();
        return b_new;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bgImg, null, scrC, null);
        if (birdFrame == 0) {
            birdFrame = 1;
        } else {
            birdFrame = 0;
        }
        if (gamestatus) {
            if (bird_Y < scrHeight - birds[0].getHeight() || velocity < 0) {
                velocity += gravity;
                bird_Y += velocity;
            }
            for(int i=0;i<numberOfTubes;i++) {
                tubeX[i] -= tubeVelocity;
                if(tubeX[i]<-topTube.getWidth()){
                    tubeX[i]+=numberOfTubes*distanceBetweenTubes;
                    topTubeY[i] = minTubeOffset+random.nextInt(maxTubeOffset-minTubeOffset+1);
                }
                canvas.drawBitmap(topTube, tubeX[i], topTubeY[i] - topTube.getHeight(), null);
                canvas.drawBitmap(bottomTube, tubeX[i], topTubeY[i] + gap, null);
            }
        }
        canvas.drawBitmap(birds[birdFrame], bird_X + velocity_X, bird_Y - velocity_Y, null);
        handler.postDelayed(runnable, UPDATE_TIME);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                velocity = -30;
                gamestatus = true;
                break;

        }

        return true;
    }
}
