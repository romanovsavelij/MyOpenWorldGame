package sch179.ru.openworld.game.landscape;

import android.util.Log;

import sch179.ru.openworld.engine.Light;
import sch179.ru.openworld.engine.ShaderProgram;
import sch179.ru.openworld.utils.GameUtils;

public class LandscapeShaderProgram extends ShaderProgram {

    private int location_transformationMatrix;
    private int location_toLightVector;
    private int location_lightColor;

    public LandscapeShaderProgram() {
        super(GameUtils.loadShaderSourse("landscapeVertexShader.glsl"), GameUtils.loadShaderSourse("landscapeFragmentShader.glsl"));
    }

    public void loadTransformationMatrix(float[] transformationMatrix) {
        super.loadMatrix(location_transformationMatrix, transformationMatrix);
    }

    public void loadLight(Light light) {
        super.loadVector3f(location_toLightVector, light.getPosition());
        super.loadVector3f(location_lightColor, light.getColor());
    }

    @Override
    protected void getAllUniformLocation() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_lightColor = super.getUniformLocation("lightColor");
        location_toLightVector = super.getUniformLocation("toLightVector");
        System.out.println();
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }
}
