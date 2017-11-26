package sch179.ru.openworld.game.landscape;

public class LandscapeBackgound {

	private int blendMap;
	private int backgroundTexture;
	private int textureR;
	private int textureG;
	private int textureB;

	public LandscapeBackgound(int blendMap, int backgroundTexture, int textureR, int textureG, int textureB) {
		this.blendMap = blendMap;
		this.backgroundTexture = backgroundTexture;
		this.textureR = textureR;
		this.textureG = textureG;
		this.textureB = textureB;
	}

	public int getBlendMap() {
		return blendMap;
	}

	public void setBlendMap(int blendMap) {
		this.blendMap = blendMap;
	}

	public int getBackgroundTexture() {
		return backgroundTexture;
	}

	public void setBackgroundTexture(int backgroundTexture) {
		this.backgroundTexture = backgroundTexture;
	}

	public int getTextureR() {
		return textureR;
	}

	public void setTextureR(int textureR) {
		this.textureR = textureR;
	}

	public int getTextureG() {
		return textureG;
	}

	public void setTextureG(int textureG) {
		this.textureG = textureG;
	}

	public int getTextureB() {
		return textureB;
	}

	public void setTextureB(int textureB) {
		this.textureB = textureB;
	}

}
