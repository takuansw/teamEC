<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/jaguar.css">
<link rel="stylesheet" href="./css/login.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="./js/login.js"></script>
<title>ログイン画面</title>
</head>
<body>
	<jsp:include page="header.jsp" /><!-- header -->

	<h1>ログイン画面</h1>

	<s:if test = "userIdErrorMessageList != null && userIdErrorMessageList.size() > 0">
		<div class = "message bg_red">
			<s:iterator value ="userIdErrorMessageList">
				<s:property /><br>
			</s:iterator>
		</div>
	</s:if>

	<s:if test = "passwordErrorMessageList != null && passwordErrorMessageList.size() > 0">
		<div class = "message bg_red">
			<s:iterator value ="passwordErrorMessageList">
				<s:property /><br>
			</s:iterator>
		</div>
	</s:if>

	<s:if test = "isNotUserInfoMessage != null && !isNotUserInfoMessage.isEmpty()">
		<div class = "message bg_red">
			<s:property value = "isNotUserInfoMessage" />
		</div>
	</s:if>

	<s:form id = "loginForm">
		<table class = "row_table">
			<tr>
				<th><s:label>ユーザーID</s:label></th>

				<s:if test = "#session.savedUserIdFlg == true">
					<td><s:textfield name = "userId" value = "%{session.savedUserId}" placeholder = "ユーザーID" onfocus="this.placeholder=''" onblur="this.placeholder='ユーザーID'"/></td>
				</s:if>

				<s:else>
					<td><s:textfield name = "userId" value = "%{userId}" placeholder = "ユーザーID" onfocus="this.placeholder=''" onblur="this.placeholder='ユーザーID'"/></td>
				</s:else>
			</tr>

			<tr>
				<th><s:label>パスワード</s:label></th>
				<td><s:password name = "password" placeholder = "パスワード" onfocus="this.placeholder=''" onblur="this.placeholder='パスワード'"/></td>
			</tr>
		</table>

		<div class = "box">
			<s:if test = "#session.savedUserIdFlg == true">
				<s:checkbox name = "savedUserIdFlg" checked = "checked"/>
			</s:if>

			<s:else>
				<s:checkbox name = "savedUserIdFlg" />
			</s:else>

			<s:label>ユーザーID保存</s:label>
		</div>

		<div class="center_btn_box">
			<s:submit value = "ログイン" class="btn_green" onclick = "goLoginAction()" />
		</div>

		<div class = "center_btn_box">
			<s:submit value = "新規ユーザー登録" class="btn_blue" onclick = "goCreateUserAction()" />
		</div>

		<div class = "center_btn_box">
			<s:submit value = "パスワード再設定" class="btn_blue" onclick = "goResetPasswordAction()" />
		</div>

	</s:form>
</body>
</html>