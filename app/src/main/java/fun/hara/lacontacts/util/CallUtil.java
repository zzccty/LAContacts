package fun.hara.lacontacts.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by hanaii on 2019/6/21.
 */

public class CallUtil {
    /**
     * 调用拨号功能
     * @param activity
     * @param phone
     */
    public static void call(Activity activity, String phone){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));
        activity.startActivity(intent);
    }
}
