package sch179.ru.openworld.game.defaultRenderUtils;

import android.graphics.PorterDuff;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.Matrix;

import java.util.ArrayList;
import java.util.List;

import sch179.ru.openworld.engine.Light;
import sch179.ru.openworld.engine.Model;
import sch179.ru.openworld.engine.Transformation;
import sch179.ru.openworld.utils.GameUtils;

public class DefaultRenderer {

    private Light light;
    private DefaultShaderProgram shader;

    public DefaultRenderer() { shader = new DefaultShaderProgram(); }

    public void setLight(Light light) {
        this.light = light;
    }

    public void renderModel(Model model, float[] transformationMatrix, float[] screenMatrix) {

        if (model.getTransperancy()) {
            GLES30.glDisable(GLES30.GL_CULL_FACE);
        }

        float[] finalMatrix = new float[16];
        if (transformationMatrix == null) {
            for (int i = 0; i < 16; ++i)
                finalMatrix[i] = screenMatrix[i];
        } else {
            Matrix.multiplyMM(finalMatrix, 0, screenMatrix, 0, transformationMatrix, 0);
        }


        shader.start();
        shader.loadLight(light);
        shader.loadTransformationMatrix(finalMatrix);
        GLES30.glBindVertexArray(model.getVaoId());
        GLES30.glEnableVertexAttribArray(0);
        GLES30.glEnableVertexAttribArray(1);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, model.getTextureId());
        GLES30.glEnableVertexAttribArray(2);
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, model.getIndicesId());
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, model.getVertexCount(), GLES30.GL_UNSIGNED_INT, 0);
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        GLES30.glDisableVertexAttribArray(0);
        GLES30.glDisableVertexAttribArray(1);
        GLES30.glDisableVertexAttribArray(2);
        GLES30.glBindVertexArray(0);
        shader.stop();

        if (model.getTransperancy()) {
            GLES30.glEnable(GLES30.GL_CULL_FACE);
            GLES30.glCullFace(GLES30.GL_BACK);
        }
    }

    public void renderModels(Model model, List<Transformation> transformations, float[] screenMatrix) {

        if (model.getTransperancy()) {
            GLES30.glDisable(GLES30.GL_CULL_FACE);
        }

        shader.start();
        shader.loadLight(light);


        GLES30.glBindVertexArray(model.getVaoId());
        GLES30.glEnableVertexAttribArray(0);
        GLES30.glEnableVertexAttribArray(1);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, model.getTextureId());
        GLES30.glEnableVertexAttribArray(2);
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, model.getIndicesId());

        for (Transformation transformation : transformations) {

            float[] tmp = new float[16];
            float[] finalMatrix = new float[16];
            Matrix.setIdentityM(tmp, 0);
            GameUtils.Vector3f pos = transformation.getPosition();
            Matrix.translateM(tmp, 0, pos.x, pos.y, pos.z);
            Matrix.scaleM(tmp, 0, 5, 5, 5);
            Matrix.multiplyMM(finalMatrix, 0, screenMatrix, 0, tmp, 0);

            shader.loadTransformationMatrix(finalMatrix);
            GLES30.glDrawElements(GLES30.GL_TRIANGLES, model.getVertexCount(), GLES30.GL_UNSIGNED_INT, 0);

        }

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);

        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, 0);
        GLES30.glDisableVertexAttribArray(0);
        GLES30.glDisableVertexAttribArray(1);
        GLES30.glDisableVertexAttribArray(2);
        GLES30.glBindVertexArray(0);

        shader.stop();

        if (model.getTransperancy()) {
            GLES30.glEnable(GLES30.GL_CULL_FACE);
            GLES30.glCullFace(GLES30.GL_BACK);
        }

    }





}
