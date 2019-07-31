package com.internousdev.jaguar.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.jaguar.dao.CartInfoDAO;
import com.internousdev.jaguar.dao.UserInfoDAO;
import com.internousdev.jaguar.dto.CartInfoDTO;
import com.internousdev.jaguar.dto.UserInfoDTO;
import com.internousdev.jaguar.util.InputChecker;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport implements SessionAware{

	private String userId;
	private String password;
	private boolean savedUserIdFlg;
	private String isNotUserInfoMessage;
	private List<String> userIdErrorMessageList;
	private List<String> passwordErrorMessageList;
	private List<CartInfoDTO> cartInfoDTOList;
	private int totalPrice;
	private Map<String,Object> session;

	public String execute() {

        //  仮IDの有無でタイムアウトのチェック
		if(!session.containsKey("tempUserId")){
			return "sessionTimeout";
		}

	    //登録完了画面から自動遷移した時用
		if(session.containsKey("userIdForCreateUser")){
			userId = session.get("userIdForCreateUser").toString();
			password = session.get("password").toString();
			//代入後は不要
			session.remove("userIdForCreateUser");
			session.remove("password");
		}

		// ID保存チェック済でsessionにuserIdとFlgの保存
		if(savedUserIdFlg){

			session.put("savedUserIdFlg",true);
			session.put("savedUserId", userId);
		}
		else {
			session.remove("savedUserIdFlg");
			session.remove("savedUserId");
		}

	// 入力エラー時の処理

		String result = ERROR;
		InputChecker ic = new InputChecker();

		userIdErrorMessageList = ic.doCheck("ユーザーID", userId, 1, 8, true, false, false, true, false, false);
		passwordErrorMessageList = ic.doCheck("パスワード", password, 1, 16, true, false, false, true, false, false);

		if(userIdErrorMessageList.size() > 0  ||
			passwordErrorMessageList.size() > 0	){

			session.put("logined", 0);
			return result;
		}

	// ログイン認証チェック
		UserInfoDAO userInfoDAO = new UserInfoDAO();

		if(userInfoDAO.isExistsUserInfo(userId, password) &&   //ユーザー存在確認
				userInfoDAO.login(userId, password) > 0) {     // ログイン認証

			// 紐づけ
			       CartInfoDAO cartInfoDAO = new CartInfoDAO();
			       String tempUserId = session.get("tempUserId").toString();
			       List<CartInfoDTO> cartInfoDTOListForTempUser = cartInfoDAO.getCartInfoDTOList(tempUserId);

			       // 仮IDで追加した商品があれば
			       if(cartInfoDTOListForTempUser != null) {

			    	   boolean himoduke = LinkToCartInfo(tempUserId, cartInfoDTOListForTempUser);
			    	   //  カート情報更新メソッドの実行 (詳細はこのクラスのもう一つのメソッドに)

			    	   if(!himoduke){
			    		   result = "DBError";
			    	   }
			       }

			       if(session.containsKey("cartFlg")){
		    		   result = "cart";
		    		   session.remove("cartFlg");
		    	   }
			       else {
			    	   result = SUCCESS;
		    	   }

		//セッションにユーザーIDとログインフラグを入れる 仮IDは削除

			UserInfoDTO userInfoDTO =  userInfoDAO.getUserInfo(userId, password);
			session.put("userId", userInfoDTO.getUserId());
			session.put("logined", 1);
			session.remove("tempUserId");

		} else {
			isNotUserInfoMessage = "ユーザーIDまたはパスワードが異なります。";
		}

		return result;
	}

	/**
	 * カート情報を仮IDからユーザーIDに紐づけする。
	 * 紐づけ成功したら、userIdに基づくカート情報リストと合計金額がフィールドに代入される。
	 * @param tempUserId : 仮ID String型
	 * @param cartInfoDTOListForTempUser : 仮IDのカート情報リスト List<CartInfoDTO>型
	 * @return 全て完了でtrue : boolean型
	 */
	public boolean LinkToCartInfo(String tempUserId, List<CartInfoDTO> cartInfoDTOListForTempUser){

		boolean result = false;
		int count = 0;
		CartInfoDAO cartInfoDAO = new CartInfoDAO();

		for(CartInfoDTO dto : cartInfoDTOListForTempUser){

			//仮IDが持つ商品IDがユーザーIDのカート情報に存在するか
			if(cartInfoDAO.isExistsSameProduct(userId,dto.getProductId())){

			      count = count + cartInfoDAO.updateProductCount(userId, dto.getProductId(), dto.getProductCount());

			      cartInfoDAO.deleteCartInfo(tempUserId, dto.getProductId());
			}
			else {
				count = count + cartInfoDAO.updateUserId(userId, tempUserId, dto.getProductId());
			}
		}

		if(cartInfoDTOListForTempUser.size()  == count) {

			cartInfoDTOList = cartInfoDAO.getCartInfoDTOList(userId);
			totalPrice = cartInfoDAO.getTotalPrice(userId);
			result = true;
		}
		return result;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isSavedUserIdFlg() {
		return savedUserIdFlg;
	}
	public void setSavedUserIdFlg(boolean savedUserIdFlg) {
		this.savedUserIdFlg = savedUserIdFlg;
	}

	public String getIsNotUserInfoMessage() {
		return isNotUserInfoMessage;
	}
	public void setIsNotUserInfoMessage(String isNotUserInfoMessage) {
		this.isNotUserInfoMessage = isNotUserInfoMessage;
	}

	public List<String> getUserIdErrorMessageList() {
		return userIdErrorMessageList;
	}
	public void setUserIdErrorMessageList(List<String> userIdErrorMessageList) {
		this.userIdErrorMessageList = userIdErrorMessageList;
	}

	public List<String> getPasswordErrorMessageList() {
		return passwordErrorMessageList;
	}
	public void setPasswordErrorMessageList(List<String> passwordErrorMessageList) {
		this.passwordErrorMessageList = passwordErrorMessageList;
	}

	public List<CartInfoDTO> getCartInfoDTOList() {
		return cartInfoDTOList;
	}
	public void setCartInfoDTOList(List<CartInfoDTO> cartInfoDTOList) {
		this.cartInfoDTOList = cartInfoDTOList;
	}

	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Map<String, Object> getSession() {
		return session;
	}
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
