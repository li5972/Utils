package util.lb.com.utils.commons;

import android.app.Application;

import util.lb.com.utils.myVolley.MyVolley;

/**
 * Created by Libin on 2016/4/20.
 */
public class BaseApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init(){
        MyVolley.init(getApplicationContext());
    }
}
