package util.lb.com.utils;

import android.app.Activity;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import util.lb.com.utils.myVolley.MyVolley;
import util.lb.com.utils.net.BaseResponse;
import util.lb.com.utils.net.NetCallBack;
import util.lb.com.utils.net.NetUtil;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.text)).setText(Constant.CONTANT_TEST);
        NetUtil.getUrl("http://192.168.0.200:8001/1.0/order/order/list?city_id=0&platform=android&id_customer=88430&platform_version=17-4.2.2&page=1&app_version=4.1.0&longitude=0.0&device_id=000000000000000_89014103211118510720&serialVersionUID=1&latitude=0.0&channel=_360&requestTag=kFinanceAppReservationsRequestTag&customer_id=88430&md5=ebaf21",
                BaseResponse.class, new NetCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse clazz) {
                        Log.e("tag",clazz.getStatus());
                    }

                    @Override
                    public void onFailuer(String msg) {
                        Log.e("tag",msg);
                    }
                });
        MyVolley.getImageLoader().get("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png", (ImageView) findViewById(R.id.imgv));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static String gettext(){
        return "textxxxxxx";
    }
}
