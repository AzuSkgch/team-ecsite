package jp.co.internous.amethyst.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import jp.co.internous.amethyst.model.domain.MstUser;
import jp.co.internous.amethyst.model.form.UserForm;
import jp.co.internous.amethyst.model.mapper.MstUserMapper;
import jp.co.internous.amethyst.model.mapper.TblCartMapper;
import jp.co.internous.amethyst.model.session.LoginSession;

/**
 * 認証に関する処理を行うコントローラー
 * @author AzuSkgch
 *
 */
@RestController
@RequestMapping("/amethyst/auth")
public class AuthController {
	
	@Autowired
	private MstUserMapper userMapper;
	
	@Autowired
	private TblCartMapper cartMapper;
	
	@Autowired
	private LoginSession loginSession;
	
	private Gson gson = new Gson();
	
	/**
	 * ログイン処理をおこなう
	 * @param f ユーザーフォーム
	 * @return ログインしたユーザー情報(JSON形式)
	 */
	@RequestMapping("/login")
	public String login(@RequestBody UserForm form) {
		MstUser user = userMapper.findByUserNameAndPassword(form.getUserName(), form.getPassword());
		
		int tmpUserId = loginSession.getTmpUserId();
		// 仮IDでカート追加されていれば、本ユーザーIDに更新する。
		
		if (user != null && tmpUserId != 0) {
			int count = cartMapper.findCountByUserId(tmpUserId);
			if (count > 0) {
				cartMapper.updateUserId(user.getId(), tmpUserId);
			}
		}
		
		if (user != null) {
			loginSession.setTmpUserId(0);
			loginSession.setLogined(true);
			loginSession.setUserId(user.getId());
			loginSession.setUserName(user.getUserName());
			loginSession.setPassword(user.getPassword());
		} else {
			loginSession.setLogined(false);
			loginSession.setUserId(0);
			loginSession.setUserName(null);
			loginSession.setPassword(null);
		}
		
		return gson.toJson(user);
	}
	
	/**
	 * ログアウト処理をおこなう
	 * @return 空文字
	 */
	@RequestMapping("/logout")
	public String logout() {
		loginSession.setTmpUserId(0);
		loginSession.setLogined(false);
		loginSession.setUserId(0);
		loginSession.setUserName(null);
		loginSession.setPassword(null);
		
		return "";
	}

}
