package fun.hara.lacontacts.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hanaii on 2019/6/22.
 */

public class PhoneInfoPreferences {
    private Context context;
    public PhoneInfoPreferences(Context context)
    {
        this.context=context;
    }


    public void save(String name, String phone)
    {
        SharedPreferences preferences=context.getSharedPreferences("PhoneInfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("name", name);
        editor.putString("phone", phone);
        editor.commit();//提交数据
    }
}
