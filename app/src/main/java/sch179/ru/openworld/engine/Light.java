package sch179.ru.openworld.engine;


import sch179.ru.openworld.utils.GameUtils;

public class Light {

	private GameUtils.Vector3f position;
	private GameUtils.Vector3f color;

	public Light(GameUtils.Vector3f position, GameUtils.Vector3f color) {
		super();
		this.position = position;
		this.color = color;
	}

	public GameUtils.Vector3f getPosition() {
		return position;
	}

	public void setPosition(GameUtils.Vector3f position) {
		this.position = position;
	}

	public GameUtils.Vector3f getColor() {
		return color;
	}

	public void setColor(GameUtils.Vector3f color) {
		this.color = color;
	}

}
