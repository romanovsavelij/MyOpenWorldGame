package sch179.ru.openworld.engine;

import android.opengl.Matrix;
import android.renderscript.Matrix4f;
import android.util.Log;

import sch179.ru.openworld.utils.GameUtils;

public class Camera {

    private GameUtils.Vector3f position = new GameUtils.Vector3f(0, 0, 0);
    private float[] viewMatrix = new float[16];
    private float yaw = 0f, pitch = 0f;


    public void TopButtonPressed(float s) {
        position.y -= Math.sin(pitch);
        float pr = (float) Math.cos(pitch);
        position.x += (float) pr * Math.sin(yaw);
        position.z -= (float) pr * Math.cos(yaw);
    }

    public void RightButtonPressed(float s) {
        position.x += Math.sin(yaw);
        position.z += Math.cos(yaw);
    }

    public void LeftButtonPressed(float s) {
        position.x -= Math.sin(yaw);
        position.z -= Math.cos(yaw);
    }

    public void BottomButtonPressed(float s) {
        position.y += Math.sin(pitch);
        float pr = (float) Math.cos(pitch);
        position.x -= (float) pr * Math.sin(yaw);
        position.z += (float) pr * Math.cos(yaw);

    }

    public void UpButtonPressed(float s) {
        position.y -= s;

    }

    public void DownButtonPressed(float s) {
        position.y += s;

    }

    public void turn(float x, float y) {
        yaw += x;
        pitch += y;
        Log.d("pitch", pitch);
    }

    public float[] getViewMatrix() {

        Matrix.setIdentityM(viewMatrix, 0);
        /*float dist = 100f;
        float CentX = (float) (position.x + dist * Math.cos(yaw) * Math.cos(pitch));
        float CentY = (float) (position.y + dist * Math.sin(pitch));
        float CentZ = (float) (position.z + dist * Math.cos(pitch) * Math.sin(yaw));*/
        //Matrix.setLookAtM(viewMatrix, 0, position.x, position.y, position.z, dist * CentX, dist * CentY, dist * CentZ, 0f, 1f, 0f);
        Matrix.rotateM(viewMatrix, 0, pitch, 1f, 0f, 0f);
        //Matrix.rotateM(viewMatrix, 0, yaw, 1f, 0f, 0f);
        Matrix.translateM(viewMatrix, 0, position.x, position.y, position.z);
        return viewMatrix;
    }

}
