package sch179.ru.openworld.game.landscape;

import android.graphics.Bitmap;
import android.opengl.Matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sch179.ru.openworld.engine.LoadManager;
import sch179.ru.openworld.engine.Model;
import sch179.ru.openworld.engine.Transformation;
import sch179.ru.openworld.utils.GameUtils;

public class LandscapeModel {

	private static float SIZE = 512;
	private float[] transformationMatrix = new float[16];
	float[][] heights;

	private float x;
	private float y;
	private Model model;
	private List<Transformation> objects;

	public LandscapeModel(float gridX, float gridY, int textureId, Bitmap heightMap, LoadManager loadManager) {
		objects = new ArrayList<Transformation>();
		x = gridX * SIZE;
		y = gridY * SIZE;
		model = generateLandscape(heightMap, loadManager);
		model.setTextureId(textureId);
		createTransformationMatrix();
	}


	private void createTransformationMatrix() {

		Matrix.setIdentityM(transformationMatrix, 0);
		Matrix.translateM(transformationMatrix, 0, x, 0f, y);
		Matrix.scaleM(transformationMatrix, 0, 1f, 1f, 1f);

	}

	public float getH(int i, int j) {
		return heights[i][j];
	}

	public float[] getTransformationMatrix() {
		return transformationMatrix;
	}

	public static float getSIZE() {
		return SIZE;
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public Model getModel() {
		return model;
	}
	public List<Transformation> getObjects() {
		return objects;
	}

	private int VERTEX_COUNT;

	public int getVertexCount() {
		return VERTEX_COUNT;
	}

	private Model generateLandscape(Bitmap im, LoadManager loader) {

		VERTEX_COUNT = im.getHeight();

		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int vertexPointer = 0;
		Random random = new Random();
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer * 3 + 1] = getHeight(j, i, im);
				heights[i][j] = vertices[vertexPointer * 3 + 1];
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
				if (objects.size() < 400 && random.nextInt() % 5000 == 0) {
					objects.add(new Transformation(new GameUtils.Vector3f(vertices[vertexPointer * 3] + x,
							vertices[vertexPointer * 3 + 1], vertices[vertexPointer * 3 + 2] + y),
							new GameUtils.Vector3f(0, 0, 0), 10));
				}
				GameUtils.Vector3f vec = getNormals(j, i, im);
				normals[vertexPointer * 3] = vec.x;
				normals[vertexPointer * 3 + 1] = vec.y;
				normals[vertexPointer * 3 + 2] = vec.z;
				textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
			for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadModel(vertices, indices, textureCoords, normals);
	}

	private GameUtils.Vector3f getNormals(int i, int j, Bitmap im) {
		float hL = getHeight(i - 1, j, im);
		float hR = getHeight(i + 1, j, im);
		float hD = getHeight(i, j - 1, im);
		float hU = getHeight(i, j + 1, im);
		GameUtils.Vector3f vec = new GameUtils.Vector3f(hL - hR, 2f, hD - hU);
		vec.normalize();
		return vec;
	}

	private float getHeight(int i, int j, Bitmap im) {

		if (i < 0 || i >= im.getHeight() || j < 0 || j >= im.getHeight())
			return 0;
		int rgba = im.getPixel(i, j);
		float MAXH = 20.0f;
		int MAXC = 256 * 256 * 256;
		return ((float) rgba + (float) MAXC / 2.0f) / (MAXC / 2.0f) * MAXH;

	}

}
