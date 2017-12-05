package sch179.ru.openworld;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.ActivityManager;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import sch179.ru.openworld.engine.Camera;
import sch179.ru.openworld.utils.GameUtils;
import sch179.ru.openworld.game.GameRenderer;

public class GameActivity extends Activity implements View.OnClickListener, View.OnTouchListener {

    private GLSurfaceView glSurfaceView;
    private boolean rendererSet = false;
    Camera camera;
    int s = 7;
    private float xold = -1, yold = -1;

    void createSuffaceView() {

        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(3);
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
        if (supportsEs2) {
            glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
            GameRenderer gameRenderer = new GameRenderer(); camera = gameRenderer.getCamera();
            glSurfaceView.setRenderer(gameRenderer); rendererSet = true;
        } else {
            Toast.makeText(this, "This device does not support OpenGL ES 2.0.", Toast.LENGTH_LONG).show(); return;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameUtils.setAssetManager(this.getAssets());
        GameUtils.setContext(this);

        setContentView(R.layout.game_activity);

        createSuffaceView();

        RelativeLayout item = findViewById(R.id.rl);

        item.addView(glSurfaceView);

        item.setOnTouchListener(this);

        Button button;
        button = findViewById(R.id.buttonLeft);
        button.setOnClickListener(this);
        button.setTag("left");
        button = findViewById(R.id.buttonRight);
        button.setOnClickListener(this);
        button.setTag("right");
        button = findViewById(R.id.buttonTop);
        button.setOnClickListener(this);
        button.setTag("top");
        button = findViewById(R.id.buttonBottom);
        button.setOnClickListener(this);
        button.setTag("bottom");
    }


    @Override
    protected void onPause() { super.onPause();
        if (rendererSet) {
            glSurfaceView.onPause();
        }
    }
    @Override
    protected void onResume() { super.onResume();
        if (rendererSet) {
            glSurfaceView.onResume();
        }
    }

    @Override
    public void onClick(View view) {
        Button button = (Button) view;

        if (button.getTag() == "left") {
            camera.LeftButtonPressed(s);
        }
        if (button.getTag() == "right") {
            camera.RightButtonPressed(s);
        }
        if (button.getTag() == "top") {
            camera.TopButtonPressed(s);
        }
        if (button.getTag() == "bottom") {
            camera.BottomButtonPressed(s);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (xold != -1 && yold != -1) {
                float deltax = x - xold;
                float deltay = y - yold;
                float speed = 1f;
                deltax *= speed;
                deltay *= speed;
                camera.turn(deltax / 5.0f, deltay / 5.0f);
            }
            xold = x;
            yold = y;
        }
        return true;
    }
}
