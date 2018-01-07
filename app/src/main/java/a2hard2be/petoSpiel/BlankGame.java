package a2hard2be.petoSpiel;

import android.app.Activity;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Point;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;

import android.util.Log;
import android.view.Display;

import java.io.IOException;


public class BlankGame extends Activity {

    private MegaVista megavista;
    SoundPool soundPool;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // creacion del sonido



        int soundID = -1;


        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

        try {
            // Create objects of the 2 required classes
            AssetManager assetManager = this.getAssets();
            AssetFileDescriptor descriptor;

            // Load our fx in memory ready for use
            descriptor = assetManager.openFd("warp.ogg");
            soundID = soundPool.load(descriptor, 0);

        } catch (IOException e) {
            // Print an error message to the console
            Log.e("error", "failed to load sound files");
        }
        android.os.SystemClock.sleep(1000);
        soundPool.play(soundID, 1, 1, 0, 0, 1);


        Display display = getWindowManager().getDefaultDisplay();
        Point resolution = new Point();
        display.getSize(resolution);

        megavista = new MegaVista(this, resolution.x, resolution.y);
        //        Abajo estaba lo siguiente: R.layout.activity_blank_game, antes estaba ourview.
        setContentView(megavista);

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();


    @Override
    protected void onResume(){
        super.onResume();
        megavista.resume();
    }

    @Override
    protected  void onPause(){
        super.onPause();
        megavista.pause();
    }

    }
