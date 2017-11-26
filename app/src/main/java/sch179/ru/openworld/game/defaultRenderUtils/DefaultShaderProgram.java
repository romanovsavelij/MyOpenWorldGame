package sch179.ru.openworld.game.defaultRenderUtils;

import android.util.Log;

import sch179.ru.openworld.engine.Light;
import sch179.ru.openworld.engine.ShaderProgram;
import sch179.ru.openworld.utils.GameUtils;

public class DefaultShaderProgram extends ShaderProgram {

    private int location_transformationMatrix;
    private int location_toLightVector;
    private int location_lightColor;

    public DefaultShaderProgram() {
        super(GameUtils.loadShaderSourse("defaultVertexShader.glsl"), GameUtils.loadShaderSourse("defaultFragmentShader.glsl"));
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
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }
}
