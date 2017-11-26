package sch179.ru.openworld.engine;

public class Model {
    
    private int vaoId;
    private int indicesId;
    private int vertexCount;
    private int textureId;
    private boolean hasTransperancy = false;

    public Model(int vaoId, int indicesId, int vertexCount) {
        this.vaoId = vaoId;
        this.indicesId = indicesId;
        this.vertexCount = vertexCount;
    }

    public void hasTransperancy() {
        hasTransperancy = true;
    }

    public boolean getTransperancy() {
        return hasTransperancy;
    }

    public int getTextureId() {
        return textureId;
    }

    public void setTextureId(int textureId) {
        this.textureId = textureId;
    }

    public int getVaoId() {
        return vaoId;
    }
    public int getIndicesId() { return indicesId; }
    public int getVertexCount() {
        return vertexCount;
    }

}
