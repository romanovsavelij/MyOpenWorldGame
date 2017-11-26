package sch179.ru.openworld.engine;


import android.opengl.GLES20;
import android.opengl.GLES30;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import sch179.ru.openworld.utils.GameUtils;
import sch179.ru.openworld.utils.GameUtils.Vector3f;

public class LoadManager {

    private ArrayList<Integer> VAOs;
    private ArrayList<Integer> VBOs;

    public LoadManager() {
        VAOs = new ArrayList<Integer>();
        VBOs = new ArrayList<Integer>();
    }

    public Model loadModel(float[] positions, int[] indices, float[] textureCoords, float[] normals) {
        int[] vao = new int[1];
        GLES30.glGenVertexArrays(1, vao, 0);
        VAOs.add(vao[0]);

        GLES30.glBindVertexArray(vao[0]);

        int[] vbo = new int[4];
        GLES20.glGenBuffers(4, vbo, 0);

        VBOs.add(vbo[0]);
        VBOs.add(vbo[1]);
        VBOs.add(vbo[2]);
        VBOs.add(vbo[3]);

        FloatBuffer posBuffer = GameUtils.createFloatBuffer(positions.length);
        posBuffer.put(positions);
        posBuffer.position(0);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbo[0]);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, positions.length * 4, posBuffer, GLES30.GL_STATIC_DRAW);
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, 0);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);

        IntBuffer indicesBuf = GameUtils.createIntBuffer(indices.length);
        indicesBuf.put(indices).position(0);

        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, vbo[1]);
        GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER, indicesBuf.capacity() * 4, indicesBuf, GLES30.GL_STATIC_DRAW);
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, 0);

        FloatBuffer textureBuffer = GameUtils.createFloatBuffer(textureCoords.length);
        textureBuffer.put(textureCoords);
        textureBuffer.position(0);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbo[2]);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, textureBuffer.capacity() * 4, textureBuffer, GLES30.GL_STATIC_DRAW);
        GLES30.glVertexAttribPointer(1, 2, GLES30.GL_FLOAT, false, 0, 0);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);


        FloatBuffer normalsBuffer = GameUtils.createFloatBuffer(normals.length);
        normalsBuffer.put(normals);
        normalsBuffer.position(0);


        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbo[3]);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, normalsBuffer.capacity() * 4, normalsBuffer, GLES30.GL_STATIC_DRAW);
        GLES30.glVertexAttribPointer(2, 3, GLES30.GL_FLOAT, false, 0, 0);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);

        GLES30.glBindVertexArray(0);

        return new Model(vao[0], vbo[1], indices.length);
    }


    public Model loadObjModel(String objectFile) {

        BufferedReader reader = null;
        try {
            reader = GameUtils.getReader(objectFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        String line;
        List<Vector3f> vertices = new ArrayList<Vector3f>();
        List<GameUtils.Vector2f> textures = new ArrayList<GameUtils.Vector2f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<Integer> indices = new ArrayList<Integer>();
        float[] verticesArray = null;
        float[] normalsArray = null;
        float[] textureArray = null;
        int[] indicesArray = null;
        try {

            while (true) {
                line = reader.readLine();
                String[] currentLine = line.split(" ");
                if (line.startsWith("v ")) {
                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3]));
                    vertices.add(vertex);
                } else if (line.startsWith("vt ")) {
                    GameUtils.Vector2f texture = new GameUtils.Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
                    textures.add(texture);
                } else if (line.startsWith("vn ")) {
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")) {
                    textureArray = new float[vertices.size() * 2];
                    normalsArray = new float[vertices.size() * 3];
                    break;
                }
            }
            while (line != null) {
                if (!line.startsWith("f ")) {
                    line = reader.readLine();
                    continue;
                }
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
                processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
                processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);
                line = reader.readLine();
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;
        for (Vector3f vertex : vertices) {
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }

        Model model = loadModel(verticesArray, indicesArray, textureArray, normalsArray);
        if (model == null)
            Log.d("LOAD_MANAGER", "NULL_POINTER");
        else
            Log.d("LOAD_MANAGER", "NOT_NULL_POINTER");
        return model;
    }

    private static void processVertex(String[] vertexData, List<Integer> indices, List<GameUtils.Vector2f> textures,
                                      List<Vector3f> normals, float[] textureArray, float[] normalsArray) {
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertexPointer);
        GameUtils.Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
        textureArray[currentVertexPointer * 2] = currentTex.x;
        textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;
        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalsArray[currentVertexPointer * 3] = currentNorm.x;
        normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
        normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;

    }


    public void release() {
        int[] vaoArray = new int[VAOs.size()];
        for (int i = 0; i < VAOs.size(); ++i)
            vaoArray[i] = VAOs.get(i);
        GLES30.glDeleteVertexArrays(VAOs.size(), vaoArray, 0);
        int[] vboArray = new int[VBOs.size()];
        for (int i = 0; i < VBOs.size(); ++i)
            vaoArray[i] = VBOs.get(i);
        GLES30.glDeleteBuffers(VBOs.size(), vboArray, 0);
        VAOs.clear();
        VBOs.clear();
    }


}
