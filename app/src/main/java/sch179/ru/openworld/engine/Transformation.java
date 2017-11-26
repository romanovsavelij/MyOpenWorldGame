package sch179.ru.openworld.engine;

import android.opengl.Matrix;

import sch179.ru.openworld.utils.GameUtils;

public class Transformation {

    GameUtils.Vector3f position;
    GameUtils.Vector3f rotation;
    float scale;

    float[] transformationMatrix = new float[16];

    public Transformation(GameUtils.Vector3f position, GameUtils.Vector3f rotation, float scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        createTransformationMatrix();
    }

    public GameUtils.Vector3f getPosition() {
        return position;
    }

    public void setPosition(GameUtils.Vector3f position) {
        this.position = position;
    }

    public GameUtils.Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(GameUtils.Vector3f rotation) {
        this.rotation = rotation;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void createTransformationMatrix() {
        Matrix.setIdentityM(transformationMatrix, 0);
        Matrix.scaleM(transformationMatrix, 0, scale, scale, scale);
        Matrix.translateM(transformationMatrix, 0, position.x, position.y, position.z);
    }

    public float[] getTransformationMatrix() {
        return transformationMatrix;
    }

}
