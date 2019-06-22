package fun.hara.lacontacts.util;

import android.widget.EditText;

/**
 * Created by hanaii on 2019/6/22.
 */

public class EditTextUtil {

    /**
     * 让EditText不可编辑
     * @param editTexts
     */
    public static void uneditable(EditText... editTexts){
        for (EditText editText : editTexts) {
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
        }

    }

    /**
     * 让EditText可被编辑
     * @param editTexts
     */
    public static void editable(EditText ... editTexts){
        for (EditText editText : editTexts) {
            editText.setFocusableInTouchMode(true);
            editText.setFocusable(true);
            editText.requestFocus();
        }
    }
}
