package util.lb.com.utils.test;

import android.test.InstrumentationTestCase;
import android.util.Log;

import util.lb.com.utils.MainActivity;

/**
 * Created by Administrator on 2016/4/17.
 */
public class Testcase extends InstrumentationTestCase{

    public void testGettext(){
        Log.e("tag",MainActivity.gettext());
    }

}
