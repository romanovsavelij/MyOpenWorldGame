package sch179.ru.openworld.game.landscape;

import android.opengl.GLES30;
import android.opengl.Matrix;
import android.util.Log;

import java.util.ArrayList;

import sch179.ru.openworld.engine.Light;
import sch179.ru.openworld.engine.Model;
import sch179.ru.openworld.engine.Transformation;
import sch179.ru.openworld.game.defaultRenderUtils.DefaultShaderProgram;
import sch179.ru.openworld.utils.GameUtils;

public class LandscapeRenderer {

    private Light light;
    private LandscapeShaderProgram shader;

    public LandscapeRenderer() { shader = new LandscapeShaderProgram(); }

    public void setLight(Light light) {
        this.light = light;
    }

    public void renderModel(Model model, float[] transformationMatrix, float[] screenMatrix) {

        if (model.getTransperancy()) {
            GLES30.glDisable(GLES30.GL_CULL_FACE);
        }

        float[] finalMatrix = new float[16];
        Matrix.multiplyMM(finalMatrix, 0, screenMatrix, 0, transformationMatrix, 0);

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

    public void renderModels(Model model, ArrayList<Transformation> transformations, float[] screenMatrix) {

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
