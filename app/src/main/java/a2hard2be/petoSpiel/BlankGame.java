package a2hard2be.petoSpiel;

import android.app.Activity;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;

import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;


public class BlankGame extends Activity {

LadrillosView ladrillosView;

@Override
    protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    ladrillosView = new LadrillosView(this);
    setContentView(ladrillosView);

}

    class LadrillosView extends SurfaceView implements Runnable {

        Thread gameThread = null;
        SurfaceHolder ourHolder;
        volatile boolean playing;
        boolean paused = true;
        Canvas canvas;
        Paint paint;
        long fps;
        private long timeThisFrame;

        int screenX;
        int screenY;

        Paddle paddle;

        public LadrillosView(Context context) {
            super(context);
            ourHolder = getHolder();
            paint = new Paint();
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenX = size.x;
            screenY = size.y;
            paddle = new Paddle(screenX, screenY);
        }

        @Override
        public void run() {
            while (playing) {
                long startFrameTime = System.currentTimeMillis();
                if (!paused) {
                    update();
                }
                draw();
                timeThisFrame = System.currentTimeMillis() - startFrameTime;
                if (timeThisFrame >= 1) {
                    fps = 1000 / timeThisFrame;
                }
            }

        }

        public void update() {

            paddle.update(fps);

        }

        public void draw() {
            if (ourHolder.getSurface().isValid()) {
                canvas = ourHolder.lockCanvas();
                canvas.drawColor(Color.argb(255, 26, 128, 182));
                paint.setColor(Color.argb(255, 255, 255, 255));

                //Aqui se dibujan todas las cosas

                canvas.drawRect(paddle.getRect(), paint);

                ourHolder.unlockCanvasAndPost(canvas);

            }

        }

        public void pause() {
            playing = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("error", "joining thread");
            }

        }

        public void resume() {
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();

        }

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    paused=false;
                    if(motionEvent.getX() > screenX / 2){
                        paddle.setMovementState(paddle.RIGHT);}
                             else{
                    paddle.setMovementState(paddle.LEFT);
                }
                break;
                case MotionEvent.ACTION_UP:
                    paddle.setMovementState(paddle.STOPPED);
                    break;
            }
            return true;

        }
    }

@Override
    protected void onResume(){
        super.onResume();
        ladrillosView.resume();

}

    @Override
    protected void onPause(){
        super.onPause();
        ladrillosView.pause();

    }
}