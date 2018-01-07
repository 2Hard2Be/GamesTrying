package a2hard2be.petoSpiel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Peto-1 on 1/6/2018.
 */

public class VistaLadrillos extends SurfaceView implements Runnable {

    Thread gameThread = null;
    SurfaceHolder ourHolder;
    volatile boolean playing;
    boolean paused = true;
    Canvas canvas;
    Paint paint;
    long fps;
    private long timeThisFrame;
    public VistaLadrillos(Context context){
        super(context);
        ourHolder = getHolder();
        paint = new Paint();
    }
    @Override
    public void run(){
        while (playing){
            long startFrameTime = System.currentTimeMillis();
            if (!paused){
                update();
            }
            draw();
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1){
                fps = 1000 / timeThisFrame;
            }
        }

    }

    public void update(){

    }

    public void draw(){
      if (ourHolder.getSurface().isValid()){
          canvas = ourHolder.lockCanvas();
          canvas.drawColor(Color.argb(255,26,128,182));
          paint.setColor(Color.argb(255,255,255,255));

          //Aqui se dibujan todas las cosas

          ourHolder.unlockCanvasAndPost(canvas);

      }

    }

    public void pause(){
        playing = false;
        try{
            gameThread.join();
        }catch (InterruptedException e){
            Log.e("error", "joining thread");}

    }

    public void resume(){
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;

        }



    }

