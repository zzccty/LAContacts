package fun.hara.lacontacts.domain;

import android.provider.CallLog;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by hanaii on 2019/6/21.
 */

public class CallRecord {

         private Integer id; // 主键
        private String name; // 名字
        private long date;   // 日期
        private int type;    // 类型：来电、去电、未接
        private String phone;//电话


    public CallRecord(Integer id, String name, long date, int type, String phone) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.type = type;
        this.phone = phone;
    }

    /**
     * 返回类型对应的中文字符串
     * @return
     */
    public String getTypeStr(){
        if (CallLog.Calls.INCOMING_TYPE == type) {
            return "来电";
        } else if (CallLog.Calls.OUTGOING_TYPE == type) {
            return "去电";
        }else if (CallLog.Calls.MISSED_TYPE == type) {
            return "未接";
        }else if (CallLog.Calls.VOICEMAIL_TYPE == type) {
            return "语音邮件";
        }else if (CallLog.Calls.REJECTED_TYPE == type) {
            return "拒绝";
        }else if (CallLog.Calls.BLOCKED_TYPE == type) {
            return "阻止";
        } else {
            return "未知";
        }
    }

    /**
     * 返回时间对应字符串
     * @param pattern
     * @return
     */
    public String getDateStr(String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
         return format.format(date);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }



    public void setType(int type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
