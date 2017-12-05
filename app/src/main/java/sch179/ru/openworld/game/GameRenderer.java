package sch179.ru.openworld.game;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import sch179.ru.openworld.R;
import sch179.ru.openworld.engine.Camera;
import sch179.ru.openworld.engine.Light;
import sch179.ru.openworld.engine.LoadManager;
import sch179.ru.openworld.engine.Model;
import sch179.ru.openworld.engine.Transformation;
import sch179.ru.openworld.game.defaultRenderUtils.DefaultRenderer;
import sch179.ru.openworld.game.landscape.LandscapeModel;
import sch179.ru.openworld.game.landscape.LandscapeRenderer;
import sch179.ru.openworld.utils.GameUtils;

public class GameRenderer implements GLSurfaceView.Renderer  {

    private LandscapeRenderer landscapeRenderer;
    private DefaultRenderer defaultRenderer;
    private LoadManager loadManager;
    private Light light = new Light(new GameUtils.Vector3f(512f, 1e4f, 512f), new GameUtils.Vector3f(0.3f, 0.3f, 0.3f));
    float[] projectionMatrix = new float[16];

    Camera camera = new Camera(this);

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        defaultRenderer = new DefaultRenderer();
        landscapeRenderer = new LandscapeRenderer();
        loadManager = new LoadManager();
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
        enableOptimizations();
    }

    void enableOptimizations() {
        GLES30.glEnable(GLES30.GL_CULL_FACE);
        GLES30.glCullFace(GLES30.GL_BACK);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        createProjectionMatrix(width, height);
    }

    void createProjectionMatrix(int width, int height) {
        final float ratio = (float) width / (float)height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 4f;
        final float far = 512.0f;
        Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far);
    }

    LandscapeModel land;
    Model tree, dragon;
    List<Transformation> positions;


    boolean loaded = false;

    float[] screenMatrix = new float[16];

    void generateScareenMatrix() {
        if (camera.getViewMatrix() == null) {
            for (int i = 0; i < 16; ++i)
                screenMatrix[i] = projectionMatrix[i];
            return;
        }
        Matrix.multiplyMM(screenMatrix, 0, projectionMatrix, 0, camera.getViewMatrix(), 0);
    }

/// CHANGED
    private void renderPlayer() {
        GameUtils.Vector3f pos = camera.getPosition();
        float[] transformation = new float[16];
        Matrix.setIdentityM(transformation, 0);
        Matrix.translateM(transformation, 0, pos.x, land.getH((int)(Math.abs(pos.x) / 512f) * land.getVertexCount(), (int)(Math.abs(pos.z) / 512f)) * land.getVertexCount(), pos.z);
        Matrix.scaleM(transformation, 0, 0.5f, 0.5f, 0.5f);
       // System.out.println(pos.x + " " + pos.y + " " + pos.z);
        defaultRenderer.renderModel(dragon, transformation, screenMatrix);
    }

    public float getHeight(float x, float y) {
        return land.getH((int)(Math.abs(x) / 512f) * land.getVertexCount(), (int)(Math.abs(y) / 512f)) * land.getVertexCount() + 10f;
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        generateScareenMatrix();
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
        GLES30.glClearColor(0f, 1f, 1f, 0f);
        if (!loaded) {
            dragon = new LoadManager().loadObjModel("dragon.obj");
            dragon.setTextureId(GameUtils.loadTexture(R.drawable.white));
            land = new LandscapeModel(-1f, -1, GameUtils.loadTexture(R.drawable.grass), GameUtils.loadBitmap(R.drawable.hmap), loadManager);
            tree = loadManager.loadObjModel("tree.obj");
            tree.setTextureId(GameUtils.loadTexture(R.drawable.tree));
            positions = land.getObjects();
            loaded = true;
        }
        float[] tmp = new float[16];
        Matrix.setIdentityM(tmp, 0);
        GameUtils.Vector3f pos = positions.get(0).getPosition();
        Matrix.translateM(tmp, 0, pos.x, pos.y, pos.z);
        Matrix.scaleM(tmp, 0, 20, 20, 20);
        landscapeRenderer.setLight(light);
        defaultRenderer.setLight(light);
        landscapeRenderer.renderModel(land.getModel(), land.getTransformationMatrix(), screenMatrix);
        defaultRenderer.renderModels(tree, positions, screenMatrix);

        renderPlayer();



    }

    public Camera getCamera() {
        return camera;
    }
}