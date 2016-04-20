package util.lb.com.utils.net;

/**
 * Created by Libin on 2016/4/20.
 */
public interface NetCallBack<T> {

    public void onSuccess(T response);
    public void onFailuer(String msg);
}
