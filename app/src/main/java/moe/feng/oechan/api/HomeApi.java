package moe.feng.oechan.api;

import android.annotation.SuppressLint;
import android.util.Log;

import moe.feng.oechan.model.BaseMessage;
import moe.feng.oechan.model.PageListResult;
import moe.feng.oechan.support.HttpUtils;

@SuppressLint("DefaultLocale")
public class HomeApi {

	/*总共一项函数
	* 还有一个URL常量*/

	private static final String GET_PAGE_URL = "https://oneechan.moe/%1$s/api/list?page=%2$d";
	private static String TAG = "HomeApi";

	/*
	* 传入了"zh-TW", integers[0] - 1 —— 在第一页刷新的话传入的integers[0]等于1*/
	public static BaseMessage<PageListResult> getPage(String preferLang, int page) {
		/*
		* 创建一个带Code和String类型的数据 Code和String联系在一起的strMsg数据
		* 跳转至HttpUtils的getString方
		* GET_PAGE_URL中的字符串的%1$s被替换成了preferLang中的字符，即（zh-TW）， %2$d被替换成了page，也就是0
		*
		* */
		BaseMessage<String> strMsg = HttpUtils.getString(
				String.format(GET_PAGE_URL, preferLang, page), false
		);
		BaseMessage<PageListResult> result = new BaseMessage<>();
		Log.e(TAG, "getPage: " + strMsg.getCode());
		if (strMsg.getCode() == BaseMessage.CODE_OKAY) {
			result.setData(PageListResult.fromJson(strMsg.getData()));
			result.setCode(BaseMessage.CODE_OKAY);
		} else {
			result.setCode(BaseMessage.CODE_ERROR);
		}
		return result;
	}

}
