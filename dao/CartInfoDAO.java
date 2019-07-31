package com.internousdev.jaguar.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.internousdev.jaguar.dto.CartInfoDTO;
import com.internousdev.jaguar.util.DBConnector;

public class CartInfoDAO {

	/**
	 * ユーザーID と 紐づいている 商品情報 を 全て抽出
	 * @return List<CartInfoDTO>型 :
	 */
	public List<CartInfoDTO> getCartInfoDTOList(String userId){

		DBConnector db = new DBConnector();
		Connection con = db.getConnection();

		List<CartInfoDTO> cartInfoDTOList = new ArrayList<>();

		String sql = "SELECT"
						+ " cart_info.id,"
						+ " cart_info.product_id,"
						+ " cart_info.product_count,"
						+ " product_info.product_name,"
						+ " product_info.product_name_kana,"
						+ " product_info.image_file_path,"
						+ " product_info.image_file_name,"
						+ " product_info.price,"
						+ " product_info.release_company,"
						+ " product_info.release_date,"
						+ "	cart_info.regist_date,"
						+ "	cart_info.update_date,"
						+ " (product_info.price * cart_info.product_count) AS sub_total"
						+ " FROM cart_info LEFT JOIN product_info"
						+ " ON cart_info.product_id = product_info.product_id"
						+ " WHERE cart_info.user_id = ?"
						+ "	ORDER BY cart_info.update_date DESC, cart_info.regist_date DESC";

		try{

			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);

			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				CartInfoDTO dto = new CartInfoDTO();
				dto.setProductId(rs.getInt("product_id"));
				dto.setProductName(rs.getString("product_name"));
				dto.setProductNameKana(rs.getString("product_name_kana"));
				dto.setImageFilePath(rs.getString("image_file_path"));
				dto.setImageFileName(rs.getString("image_file_name"));
				dto.setPrice(rs.getInt("price"));
				dto.setReleaseCompany(rs.getString("release_company"));
				dto.setReleaseDate(rs.getDate("release_date"));
				dto.setProductCount(rs.getInt("product_count"));
				dto.setSubTotal(rs.getInt("sub_total"));
				cartInfoDTOList.add(dto);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return cartInfoDTOList;
	}

	/**
	 * 合計金額算出
	 * @return int 型
	 */
	public int getTotalPrice(String userId){

		DBConnector db = new DBConnector();
		Connection con = db.getConnection();

		int totalPrice = 0;

		// userId が 一致する レコード内 の product_count と price を 乗算
		String sql = "SELECT"
						+ " SUM(cart_info.product_count * product_info.price) AS total_price"
						+ " FROM cart_info JOIN product_info"
						+ " ON cart_info.product_id = product_info.product_id"
						+ " WHERE cart_info.user_id = ?"
						+ " GROUP BY user_id = ?";

		try{

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, userId);
			ps.setString(2, userId);

			ResultSet rs = ps.executeQuery();

			if(rs.next()){
				totalPrice = rs.getInt("total_price");
			}

		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return totalPrice ;
	}

	/**
	 * カート内重複確認
	 * @return boolean 型
	 */
	public boolean checkCartInfo(String userId, int productId){
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();

		boolean ret = false;

		String sql ="SELECT"
						+ " *"
						+ " FROM cart_info"
						+ " WHERE cart_info.user_id = ? AND cart_info.product_id = ?";
		try{
			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, userId);
			ps.setInt(2, productId);

			ResultSet rs = ps.executeQuery();

			if(rs.next()){
				ret = true;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return ret ;
	}

	/**
	 * 購入個数更新
	 * @return int 型
	 */
	public int countUpProduct(String userId, int productId, int productCount){
		DBConnector db = new DBConnector() ;
		Connection con = db.getConnection() ;

		int count = 0;

		String sql = "UPDATE"
						+ " cart_info"
						+ " SET product_count = (product_count + ?),"
						+ " update_date = NOW()"
						+ " WHERE user_id = ? AND product_id = ?";
		try{
			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, productCount);
			ps.setString(2, userId);
			ps.setInt(3, productId);

			count = ps.executeUpdate();

		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count ;
	}

	/**
	 * 商品新規追加
	 * @return int 型
	 */
	public int createProductId(String userId, int productId, int productCount){
		DBConnector db = new DBConnector() ;
		Connection con = db.getConnection() ;

		int count = 0;

		String sql ="INSERT INTO"
						+ " cart_info(user_id, product_id, product_count, regist_date, update_date)"
						+ " VALUES(?, ?, ?, NOW(), NOW())";
		try{
			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, userId);
			ps.setInt(2, productId);
			ps.setInt(3, productCount);

			count = ps.executeUpdate();

		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count ;
	}

	/**
	* userId に紐づく productId の 存在確認
	* @return boolean型 : 存在すれば true
	*/
	public boolean isExistsSameProduct(String userId, int productId){

		DBConnector db = new DBConnector() ;
		Connection con = db.getConnection() ;

		boolean result = false;

		String sql = "SELECT"
						+ " COUNT(*) AS count"
						+ " FROM  cart_info"
						+ " WHERE user_id = ? AND product_id = ?";

		try{
			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, userId);
			ps.setInt(2, productId);

			ResultSet rs = ps.executeQuery();

				while(rs.next()){

					if(rs.getInt("count") > 0){

						result = true;
					}
				}
		}catch(SQLException e){
			e.printStackTrace();
		}try{
			con.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	* cart_infoテーブル の userId が持つ productId の productCount を アップデート
	* @return int型 : 成功した数
	*/
	public int updateProductCount(String userId, int productId, int productCount){

		DBConnector db = new DBConnector() ;
		Connection con = db.getConnection() ;

		int count  = 0;

		String sql = "UPDATE"
						+ " cart_info"
						+ " SET product_count = (product_count + ?),"
						+ " update_date = now()"
						+ " WHERE user_id = ? AND product_id = ?";

		try{
			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, productCount);
			ps.setString(2, userId);
			ps.setInt(3, productId);

			count = ps.executeUpdate();

		}catch(SQLException e){
			e.printStackTrace();
		}try{
			con.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}

		return count;
	}

	/**
	* userId と productId を条件に データを削除
	* @return int型 : 成功した数
	*/
	public int deleteCartInfo(String userId, int productId){

		DBConnector db = new DBConnector() ;
		Connection con = db.getConnection() ;

		int count = 0;

		String sql = "DELETE"
						+ " FROM cart_info"
						+ " WHERE user_id = ? AND product_id = ?";

		try{
			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, userId);
			ps.setInt(2, productId);

			count = ps.executeUpdate();

		}catch(SQLException e){
			e.printStackTrace();
		}try{
			con.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return count;

	}

	/**
	* prpductId の tempUserId を userId に アップデート
	* @return int型 : 成功した数
	*/
	public int updateUserId(String userId, String tempUserId, int productId) {

			DBConnector db = new DBConnector() ;
			Connection con = db.getConnection() ;

			int count = 0;

			String sql = "UPDATE"
							+ " cart_info"
							+ " SET user_id = ? ,"
							+ " update_date = now()"
							+ " WHERE user_id = ? AND product_id = ?";

			try {
				PreparedStatement ps = con.prepareStatement(sql);

				ps.setString(1, userId);
				ps.setString(2, tempUserId);
				ps.setInt(3,productId);

				count = ps.executeUpdate();

			}catch(SQLException e){
				e.printStackTrace();
			}try{
				con.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
			return count;
	}

	/**
	* Settlement用 userId に 紐づく カート情報 を 全て削除
	* @return int 型 : Action分岐用 変数 count
	*/
	public int deleteAllCartInfo(String userId){

		int count = 0;

		DBConnector db = new DBConnector();
		Connection con = db.getConnection();

		String sql ="DELETE"
						+ " FROM cart_info"
						+ " WHERE user_id = ?";

		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);

			count = ps.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try {
				con.close();
			}catch (SQLException e){
				e.printStackTrace();
			}
		}

		return count ;
	}

	/**
	* AddCartAction用 商品Id に 紐づく 金額 を 取得
	* @return int 型 : thisProductPrice
	*/
	public int getPrice(int productId) {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		String sql = "select price from product_info where product_id = ?";
		int thisProductPrice = 0;
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, productId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				thisProductPrice = rs.getInt("price");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				con.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return thisProductPrice;
	}
}