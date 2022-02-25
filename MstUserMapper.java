package jp.co.internous.amethyst.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import jp.co.internous.amethyst.model.domain.MstUser;

/**
 * mst_userにアクセスするDAO
 * @author AzuSkgch
 *
 */
@Mapper
public interface MstUserMapper {
	
	/**
	 * ユーザー名とパスワードを条件にユーザー情報を取得する
	 * @param userName ユーザー名
	 * @param password パスワード
	 * @return ユーザー情報
	 */
	@Select("SELECT * FROM mst_user WHERE user_name = #{userName} AND password = #{password}")
	MstUser findByUserNameAndPassword(
			@Param("userName") String userName,
			@Param("password") String password);
	
	/**
	 * ユーザ名を条件に件数を取得する
	 * @param userName ユーザー名
	 * @return 件数
	 */
	@Select("SELECT count(id) FROM mst_user WHERE user_name = #{userName}")
	int findCountByUserName(@Param("userName") String userName);

}
