package sch179.ru.openworld.engine;


import android.opengl.GLES20;
import android.util.Log;

import sch179.ru.openworld.utils.GameUtils;

public abstract class ShaderProgram {

    protected int program;
    private int vertexShaderId;
    private int fragmentShaderId;

    public ShaderProgram(String vertexShaderSource, String fragmentShaderSource) {
        vertexShaderId = loadShader(vertexShaderSource, GLES20.GL_VERTEX_SHADER);
        fragmentShaderId = loadShader(fragmentShaderSource, GLES20.GL_FRAGMENT_SHADER);
        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShaderId);
        GLES20.glAttachShader(program, fragmentShaderId);
        bindAttributes();
        GLES20.glLinkProgram(program);
        GLES20.glValidateProgram(program);
        getAllUniformLocation();
    }

    protected abstract void getAllUniformLocation();

    protected void loadInt(int location, int value) {
        GLES20.glUniform1i(location, value);
    }

    protected void loadFloat(int location, float value) {
        GLES20.glUniform1f(location, value);
    }

    protected void loadMatrix(int location, float[] mat) {
        GLES20.glUniformMatrix4fv(location, 1, false, mat, 0);
    }

    protected void loadBool(int location, boolean value) {
        GLES20.glUniform1f(location, (value ? 1f : 0f));
    }

    protected void loadVector3f(int location, GameUtils.Vector3f data) {
        GLES20.glUniform3f(location, data.x, data.y, data.z);
    }

    protected int getUniformLocation(String uniform) {
        return GLES20.glGetUniformLocation(program, uniform);
    }

    public void start() {
        GLES20.glUseProgram(program);
    }

    public void stop() {
        GLES20.glUseProgram(0);
    }

    public void release() {
        stop();
        GLES20.glDetachShader(program, vertexShaderId);
        GLES20.glDetachShader(program, fragmentShaderId);
        GLES20.glDeleteShader(vertexShaderId);
        GLES20.glDeleteShader(fragmentShaderId);
        GLES20.glDeleteProgram(program);
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attrib, String varName) {
        GLES20.glBindAttribLocation(program, attrib, varName);
    }

    private static int loadShader(String shaderSource, int type) {
        int shaderID = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shaderID, shaderSource);
        GLES20.glCompileShader(shaderID);
        String compilation = GLES20.glGetShaderInfoLog(shaderID);
        if (compilation.length() != 0)
            Log.d("Shader : ", GLES20.glGetShaderInfoLog(shaderID));
        return shaderID;
    }

}
