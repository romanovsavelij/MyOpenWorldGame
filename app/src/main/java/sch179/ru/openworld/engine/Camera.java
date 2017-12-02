package sch179.ru.openworld.engine;

import android.opengl.Matrix;

import sch179.ru.openworld.utils.GameUtils;

public class Camera {

    private GameUtils.Vector3f position = new GameUtils.Vector3f(0, 0, 0);
    private float[] viewMatrix = new float[16];
    private float yaw = 0f, pitch = 0f;

    public GameUtils.Vector3f getPosition() {
        return position;
    }

    public void TopButtonPressed(float s) {
        position.y -= s * Math.sin(pitch);
        float pr = (float) Math.cos(pitch);
        position.x += pr * Math.sin(yaw);
        position.z -= pr * Math.cos(yaw);
    }

    public void RightButtonPressed(float s) {
        position.x += s * Math.cos(yaw);
        position.z += s * Math.sin(yaw);
    }

    public void LeftButtonPressed(float s) {
        position.x -= s * Math.cos(yaw);
        position.z -= s * Math.sin(yaw);
    }

    public void BottomButtonPressed(float s) {
        position.y += s * Math.sin(pitch);
        float pr = (float) Math.cos(pitch);
        position.x -= pr * Math.sin(yaw);
        position.z += pr * Math.cos(yaw);

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
    }

    public float[] getViewMatrix() {
        Matrix.setIdentityM(viewMatrix, 0);
        Matrix.rotateM(viewMatrix, 0, pitch, 1f, 0f, 0f);
        Matrix.rotateM(viewMatrix, 0, yaw, 0f, 1f, 0f);
        Matrix.translateM(viewMatrix, 0, position.x, position.y, position.z);
        return viewMatrix;
    }

}
