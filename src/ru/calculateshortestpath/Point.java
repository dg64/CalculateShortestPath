package ru.calculateshortestpath;;

public class Point {
	private float X;
	private float Y;
	private String name;
	

	public float getX() {
		return X;
	}

	public void setX(float x) {
		X = x;
	}

	public float getY() {
		return Y;
	}

	public void setY(float y) {
		Y = y;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Point(String name) {
		super();
		this.name = name;
	}

	public Point(String name, float x, float y) {
		super();
		X = x;
		Y = y;
		this.name = name;
	}
		
}