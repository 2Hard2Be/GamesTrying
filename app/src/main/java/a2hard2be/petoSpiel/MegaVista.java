package a2hard2be.petoSpiel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by Peto-1 on 9/23/2017.
 */

public class MegaVista extends SurfaceView implements Runnable {
ArrayList<Background> backgrounds;
private volatile boolean running;
private Thread gameThread = null;
private Paint paint;
private Canvas canvas;
private SurfaceHolder ourHolder;
Context context;
long fps = 60;
int screenWidth;
int screenHeight;

// agregado
volatile boolean playing;
private long timeThisFrame;
Bitmap bitmapBob;
    boolean isMoving = false;
    float walkSpeedPerSecond = 150;
    float bobXPosition = 10;
    // agregado


MegaVista(Context context, int screenWidth, int screenHeight) {
    super(context);
    this.context = context;
    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;
    ourHolder = getHolder();
    paint = new Paint();

    // agregado
    bitmapBob = BitmapFactory.decodeResource(this.getResources(), R.drawable.bob);
    // agregado

    backgrounds = new ArrayList<>();
    backgrounds.add(new Background(
            this.context,
            screenWidth,
            screenHeight,
            "atras", 0, 80, 50));
    backgrounds.add(new Background(
            this.context,
            screenWidth,
            screenHeight,
            "frente", 70, 100, 200));
// agrega mas backgrounds aca abajo
}


@Override
    public void run(){

   while (running){
       long startFrameTime = System.currentTimeMillis();
       update();
       draw();
       long timeThisFrame = System.currentTimeMillis() - startFrameTime;
       if (timeThisFrame>1){fps = 1000/timeThisFrame;
       }
   }

    while (playing){
        long startFrameTime = System.currentTimeMillis();
        update();
        draw();
        timeThisFrame = System.currentTimeMillis()- startFrameTime;
        if (timeThisFrame>0){
            fps = 1000/ timeThisFrame;
        }
    }
}

private void update(){
        // update todas las posiciones de background
    for (Background bg : backgrounds){

        bg.update(fps);
    }

    // agregado
    if(isMoving){
        bobXPosition = bobXPosition + (walkSpeedPerSecond/fps);

    }
    //agregado

}

private void draw(){
if (ourHolder.getSurface().isValid()){
canvas = ourHolder.lockCanvas();
canvas.drawColor(Color.argb(255,0,3,70));

// dibuja el parallax del background
    drawBackground(0);

    // dibuja lo demas del juego

paint.setTextSize(60);
paint.setColor(Color.argb(255,255,255,255));
canvas.drawText("Napo",350,screenHeight/100*5,paint);
paint.setTextSize(220);
canvas.drawText("Leon",50,screenHeight/100*80,paint);

// agregado

    paint.setColor(Color.argb(225,249,129,0));
    paint.setTextSize(45);
    canvas.drawText("FPS:"+ fps, 20, 40, paint);
    canvas.drawBitmap(bitmapBob, bobXPosition, 200, paint);



    // Make the brush blue
    paint.setColor(Color.argb(255,  26, 128, 182));
    // We can change this around as well


    // Draw a line
    canvas.drawLine(50,50,250,250,paint);

    // Draw some text
    canvas.drawText("Mi abuelo amoroso", 50, 50, paint);

    // Draw a pixel
    canvas.drawPoint(40,50,paint);

    // Draw a circle
    canvas.drawCircle(350,250,100,paint);

    // Change the brush color
    paint.setColor(Color.argb(255,  249, 129, 0));

    // Draw a rectangle
    canvas.drawRect(50,450,500,550,paint);

    // agregado

drawBackground(1);
//parallax

ourHolder.unlockCanvasAndPost(canvas);

}

}

private void drawBackground (int position){
    // copiar el background necesario
    Background bg = backgrounds.get(position);
    // definir que porcion de las imagenes capturar y en que coordenadas dibujasr
// regular bitmap
    Rect fromRect1 = new Rect(0,0, bg.width- bg.xClip, bg.height);
    Rect toRect1 = new Rect(bg.xClip, bg.startY, bg.width,bg.endY);
  // reversed background
    Rect fromRect2 = new Rect(bg.width- bg.xClip,0,bg.width,bg.height);
    Rect toRect2 = new Rect(0,bg.startY,bg.xClip,bg.endY);
    // Dibuja los dos bitmap del background
    if (!bg.reversedFirst){
        canvas.drawBitmap(bg.bitmap,fromRect1,toRect1,paint);
        canvas.drawBitmap(bg.bitmapReversed,fromRect2, toRect2, paint);
    }
    else {
     canvas.drawBitmap(bg.bitmap, fromRect2, toRect2, paint);
     canvas.drawBitmap(bg.bitmapReversed, fromRect1, toRect1, paint);

    }
}

public void  pause(){
  running = false;
  try {gameThread.join();}catch(InterruptedException e){}

}
    public void resume (){

    running = true;
    gameThread = new Thread(this);
    gameThread.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        switch (motionEvent.getAction() & motionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                isMoving = true;
                break;
            case MotionEvent.ACTION_UP:
                isMoving = false;
                break;
        }
        return true;

    }
}
