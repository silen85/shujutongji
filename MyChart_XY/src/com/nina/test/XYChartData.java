package com.nina.test;

public class XYChartData {
	private String lineName;		//线的名称
	private int paintColor;			//线的颜色
	public String[] coordinateX;	//x坐标值
	public int[] coordinateY;		//y坐标值


	public XYChartData() {}

	public XYChartData(String lineName, String[] coordinateX, int[] coordinateY, int paintColor){
		this.lineName = lineName;
		this.coordinateX = coordinateX;
		this.coordinateY = coordinateY;
		this.paintColor = paintColor;
	}



	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String[] getCoordinateX() {
		return coordinateX;
	}

	public void setCoordinateX(String[] coordinateX) {
		this.coordinateX = coordinateX;
	}

	public int[] getCoordinateY() {
		return coordinateY;
	}

	public void setCoordinateY(int[] coordinateY) {
		this.coordinateY = coordinateY;
	}

	public int getPaintColor() {
		return paintColor;
	}

	public void setPaintColor(int paintColor) {
		this.paintColor = paintColor;
	}



}
