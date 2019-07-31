package com.internousdev.jaguar.dto;

import java.util.Date; // sql を util に 変更しました 2019 07 16

public class CartInfoDTO {

	private String userId; // ユーザーID
	private int productId; // 商品ID
	private int productCount; // 個数（商品個数）

	private String productName; // 商品名
	private String productNameKana; // 商品名かな
	private int price; // 価格（商品価格）
	private String imageFilePath; // 画像ファイルパス
	private String imageFileName; // 画像ファイル名
	private Date releaseDate; // 発売年月
	private String releaseCompany; // 発売会社

	private int subTotal; // 小計（商品*個数）

	public String getUserId(){
		return userId;
	}
	public void setUserId(String userId){
		this.userId = userId;
	}
	public int getProductId(){
		return productId;
	}
	public void setProductId(int productId){
		this.productId = productId;
	}
	public int getProductCount(){
		return productCount;
	}
	public void setProductCount(int productCount){
		this.productCount = productCount;
	}

	public String getProductName(){
		return productName;
	}
	public void setProductName(String productName){
		this.productName = productName;
	}
	public String getProductNameKana(){
		return productNameKana;
	}
	public void setProductNameKana(String productNameKana){
		this.productNameKana = productNameKana;
	}
	public int getPrice(){
		return price;
	}
	public void setPrice(int price){
		this.price = price;
	}
	public String getImageFilePath(){
		return imageFilePath;
	}
	public void setImageFilePath(String imageFilePath){
		this.imageFilePath = imageFilePath;
	}
	public String getImageFileName(){
		return imageFileName;
	}
	public void setImageFileName(String imageFileName){
		this.imageFileName = imageFileName;
	}
	public Date getReleaseDate(){
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate){
		this.releaseDate = releaseDate;
	}
	public String getReleaseCompany(){
		return releaseCompany;
	}
	public void setReleaseCompany(String releaseCompany){
		this.releaseCompany = releaseCompany;
	}

	public int getSubTotal(){
		return subTotal;
	}
	public void setSubTotal(int subTotal){
		this.subTotal = subTotal;
	}
}
