package util.lb.com.utils.net;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Map;

import util.lb.com.utils.myVolley.MyVolley;
import util.lb.com.utils.myVolley.request.GsonRequest;

/**
 * Created by Libin on 2016/4/19.
 * 网络请求工具类
 */
public class NetUtil {


    /**
     * get请求
     */
    public static <T> void getUrl(String url, final Class<T> clazz, final NetCallBack callBack) {
        GsonRequest gsonRequest = new GsonRequest(url, clazz, null,
                new Response.Listener<T>() {
                    @Override
                    public void onResponse(T response) {
                        if (callBack != null) callBack.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callBack != null) callBack.onFailuer(error.getMessage());
                    }
                }
        );
        MyVolley.getRequestQueue().add(gsonRequest);
    }

    public static <T> void getUrl(String url, final Class<T> clazz, final NetCallBack callBack, Map<String, String> params) {
        GsonRequest gsonRequest = new GsonRequest(Request.Method.POST, url, clazz, null, params,
                new Response.Listener<T>() {
                    @Override
                    public void onResponse(T response) {
                        if (callBack != null) callBack.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callBack != null) callBack.onFailuer(error.getMessage());
                    }
                }
        );
        MyVolley.getRequestQueue().add(gsonRequest);
    }

    /**
     * post请求
     */
    public static <T> void postUrl(String url, final Class<T> clazz, final NetCallBack callBack, Map<String, String> params) {
        GsonRequest gsonRequest = new GsonRequest(Request.Method.POST, url, clazz, null, params,
                new Response.Listener<T>() {
                    @Override
                    public void onResponse(T response) {
                        if (callBack != null) callBack.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callBack != null) callBack.onFailuer(error.getMessage());
                    }
                }
        );
        MyVolley.getRequestQueue().add(gsonRequest);
    }
}
