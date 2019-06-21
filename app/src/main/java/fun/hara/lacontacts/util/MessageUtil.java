package fun.hara.lacontacts.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.view.View;


/**
 * Created by hanaii on 2019/6/21.
 */

public class MessageUtil {

    public static void  jumpMessageApp(Activity activity, String phoneNumber){
        jumpMessageApp(activity, phoneNumber, "");
    }

    /**
     * 跳转到短信应用
     * @param activity
     * @param phoneNumber
     * @param message
     */
    public static void  jumpMessageApp(Activity activity, String phoneNumber, String message){
        if(PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)){
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phoneNumber));
            intent.putExtra("sms_body", message);
            activity.startActivity(intent);
        }
    }
}
