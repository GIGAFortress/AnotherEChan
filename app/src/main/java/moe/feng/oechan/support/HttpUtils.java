package moe.feng.oechan.support;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import moe.feng.oechan.model.BaseMessage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtils {

	private static OkHttpClient client = new OkHttpClient.Builder()
			.connectTimeout(10, TimeUnit.SECONDS)
			.readTimeout(10, TimeUnit.SECONDS)
			.build();

	private static final String UA_ANOTHERECHAN = "AnotherEChan (Android) 1.x";

	private static final String TAG = HttpUtils.class.getSimpleName();

	/*
	* 把URL和UA传入到这里进行实际的操作
	* */
	public static BaseMessage<String> getString(String url, String ua) {
		//Code-String类型result
		BaseMessage<String> result = new BaseMessage<>();

		/*
		* okhttp的框架，Request
		* URL为https://oneechan.moe/zh-TW/api/list?page=0 UA为AnotherEChan (Android) 1.x
		* */
		Request request = new Request.Builder().url(url).addHeader("User-Agent", ua).build();
		try {
			/*
			* okhttp协议Response
			* */
			Response response = client.newCall(request).execute();
			result.setCode(response.code());    //把Response的Code写入result -1
			result.setData(response.body().string());   //Response的body里面应该就是列表的数据
			Log.i(TAG, result.getData());
		} catch (IOException e) {
			result.setCode(BaseMessage.CODE_ERROR);
			e.printStackTrace();
		}

        /*
        * 该返回的结果应该包含了列表的数据
        * */
		return result;
	}

	/*
	* 上拉刷新页面的时候传入的URL是"https://oneechan.moe/zh-TW/api/list?page=0"，useLocalUA=False
	* useLocalUA为False，使用预设的 UA_ANOTHERECHAN
	* 使用HTC的直接使用System.getProperty("http.agent") 获取到以下信息
	* "Dalvik/1.6.0 (Linux; U; Android 4.3; HTC X920e Build/JSS15J)"
	* */
	public static BaseMessage<String> getString(String url, boolean useLocalUA) {
		return getString(
				url,
				useLocalUA ? System.getProperty("http.agent") : UA_ANOTHERECHAN
		);
	}

}
