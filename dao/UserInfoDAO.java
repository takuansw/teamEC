package com.internousdev.jaguar.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.internousdev.jaguar.dto.UserInfoDTO;
import com.internousdev.jaguar.util.DBConnector;

public class UserInfoDAO {
	/**
	 * マイページ用
	 * @param userId
	 * @return UserInfoDTO型：userInfoDTO
	 */
	public UserInfoDTO getUserInfo(String userId) {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		UserInfoDTO userInfoDTO = new UserInfoDTO();
		String sql = "select * from user_info where user_id = ?";
		
		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
//		userIdでDB検索
			ResultSet rs = ps.executeQuery();
//		rsをDTOに格納
			while(rs.next()) {
				userInfoDTO.setId(rs.getInt("id"));
				userInfoDTO.setUserId(rs.getString("user_id"));
				userInfoDTO.setPassword(rs.getString("password"));
				userInfoDTO.setFamilyName(rs.getString("family_name"));
				userInfoDTO.setFirstName(rs.getString("first_name"));
				userInfoDTO.setFamilyNameKana(rs.getString("family_name_kana"));
				userInfoDTO.setFirstNameKana(rs.getString("first_name_kana"));
				userInfoDTO.setSex(rs.getInt("sex"));
				userInfoDTO.setEmail(rs.getString("email"));
				userInfoDTO.setLogined(rs.getInt("logined"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}try{
			con.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return userInfoDTO;
//		値が格納されたDTOを返す
	}
/**
 * ユーザー新規登録
 * @param familyName
 * @param firstName
 * @param familyNameKana
 * @param firstNameKana
 * @param sex
 * @param email
 * @param userId
 * @param password
 * @return int型:count
 */
	public int createUser(String familyName, String firstName, String familyNameKana, 
			String firstNameKana, String sex, String email, String userId, String password) {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		int count = 0;
		String sql = "insert into user_info(user_id, password, family_name, first_name, family_name_kana,"
				+"first_name_kana, sex, email, status, logined, regist_date, update_date)"
				+"values(?,?,?,?,?,?,?,?,?,?, now(), now())";
//		now()関数は現在の日付、時刻を取得
		
		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, password);
			ps.setString(3, familyName);
			ps.setString(4, firstName);
			ps.setString(5, familyNameKana);
			ps.setString(6, firstNameKana);
			ps.setString(7, sex);
			ps.setString(8, email);
			ps.setInt(9, 0);
			ps.setInt(10, 1);
			count = ps.executeUpdate();
//		ユーザー情報をDBにセット
		}catch(SQLException e){
			e.printStackTrace();
		}try{
			con.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return count;
//		変更した件数を返す
	}
	/**
	 * ユーザーの存在確認
	 * @param userId
	 * @param password
	 * @return boolean型：result
	 */
// boolean値true, falseで返す
	public boolean isExistsUserInfo(String userId, String password) {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		boolean result = false;
		
		String sql = "select count(*) as count from user_info where user_id = ? and password = ?";
//	countで行を数える。countとして行数を返す
		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, password);
			
			ResultSet rs = ps.executeQuery();
//	ｓユーザーIDとパスワードを使いDBからデータを取ってくる。countとして整数が結果に出る
			while(rs.next()) {
				if(rs.getInt("count") > 0) {
//	1件以上の結果があれば、trueを返す
					result = true;
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try{
			con.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * ユーザー登録用
	 * @param userId
	 * @return boolean型：result
	 */
	//登録しようとしたユーザーIDが既に存在しているかをチェックするメソッド。結果をboolean型のtrue or falseで返す。
	public Boolean isExistsUserInfo(String userId){
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		boolean result = false;
		//count(*)でクエリの条件に一致するレコード結果を全て取得する。（as countでcountという列名をつける）
		String sql="select count(*) as count from user_info where user_id = ?";
		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				//現在の列の値をint型で取得し、一件以上存在するかをチェック
				if(rs.getInt("count") > 0){
				//存在すればtrueを返す
					result = true;
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		try{
			con.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * ユーザー情報呼び出し
	 * @param userId
	 * @param password
	 * @return UserInfoDTO型：userInfoDTO
	 */
	public UserInfoDTO getUserInfo(String userId, String password) {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		UserInfoDTO userInfoDTO = new UserInfoDTO();
		
		String sql = "select * from user_info where user_id = ? and password = ?";
		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
//	userIdとpasswordでSQL文を実行
			while(rs.next()) {
				userInfoDTO.setId(rs.getInt("id"));
				userInfoDTO.setUserId(rs.getString("user_id"));
				userInfoDTO.setPassword(rs.getString("password"));
				userInfoDTO.setFamilyName(rs.getString("family_name"));
				userInfoDTO.setFirstName(rs.getString("first_name"));
				userInfoDTO.setFamilyNameKana(rs.getString("family_name_kana"));
				userInfoDTO.setFirstNameKana(rs.getString("first_name_kana"));
				userInfoDTO.setSex(rs.getInt("sex"));
				userInfoDTO.setEmail(rs.getString("email"));
				userInfoDTO.setLogined(rs.getInt("logined"));
//	データをDTOに格納
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}try{
			con.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return userInfoDTO;
	}
	/**
	 * パスワードリセット
	 * @param userId
	 * @param password
	 * @return int型：result
	 */
	public int resetPassword(String userId, String password) {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		int result = 0;
		
		String sql = "update user_info set password = ?, update_date = now() where user_id = ?";
		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, password);
			ps.setString(2, userId);
			result = ps.executeUpdate();
//	passwordとuserIdをSQL文に代入して実行
		}catch(SQLException e) {
			e.printStackTrace();
		}try{
			con.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
//	更新した件数を代入して返す
	}
	/**
	 * ログイン認証
	 * @param userId
	 * @param password
	 * @return int型：result
	 */
	public int login(String userId, String password) {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		int result = 0;
		
		String sql = "update user_info set logined = 1, update_date = now() where user_id = ? and password = ?";
		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, password);
			result = ps.executeUpdate();
//	DBのユーザーIDとパスワードに紐付くユーザーのloginedに1をセット、1ログイン済。　更新した件数を返す
		}catch(SQLException e) {
			e.printStackTrace();
		}try {
			con.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
//	件数を代入して返す
	}
	/**
	 * ログアウト
	 * @param userId
	 * @return int型：result
	 */
	public int logout(String userId) {
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		int result = 0;
		
		String sql = "update user_info set logined = 0, update_date = now() where user_id = ?";
		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			result = ps.executeUpdate();
//	userIdで検索したデータのloginedに0をセット、0未ログイン。更新した件数を返す
		}catch (SQLException e){
			e.printStackTrace();
		}try {
			con.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return result;
//	件数を代入して返す
	}
}
