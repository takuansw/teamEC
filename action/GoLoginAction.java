package com.internousdev.jaguar.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class GoLoginAction extends ActionSupport implements SessionAware{

	private Map<String, Object> session;
	public String execute() {

		//sessionの仮IDが切れていたらセッションタイムアウト画面へ
		if(!session.containsKey("tempUserId")){
			return "sessionTimeout";
		}
		//未ログイン状態で決済ボタンから飛んだ場合立てられるフラグ
		session.remove("cartFlg");

		return SUCCESS;
	}

	public Map<String,Object> getSession() {
		return session;
	}
	public void setSession (Map<String, Object> session) {
		this.session = session;
	}
}
