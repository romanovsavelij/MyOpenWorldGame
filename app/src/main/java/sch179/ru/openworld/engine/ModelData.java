package sch179.ru.openworld.engine;

public class ModelData {

    public float[] positions;
    public float[] textureCoords;
    public float[] normals;
    public int[] indices;

    public ModelData(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
        this.positions = positions;
        this.textureCoords = textureCoords;
        this.normals = normals;
        this.indices = indices;
    }
}
