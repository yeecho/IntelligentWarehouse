package com.greatwall.iws.bean;

public class MaterialBean {

	private int materialId;
	private String materialName;
	private String materialPosition;
	private int amountTotal;
	private int amountTake;
	
	public int getMaterialId() {
		return materialId;
	}
	public void setMaterialId(int materialId) {
		this.materialId = materialId;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getMaterialPositon() {
		return materialPosition;
	}
	public void setMaterialPositon(String materialPositon) {
		this.materialPosition = materialPositon;
	}
	public int getAmountTotal() {
		return amountTotal;
	}
	public void setAmountTotal(int amountTotal) {
		this.amountTotal = amountTotal;
	}
	public int getAmountTake() {
		return amountTake;
	}
	public void setAmountTake(int amountTake) {
		this.amountTake = amountTake;
	}
	
}
