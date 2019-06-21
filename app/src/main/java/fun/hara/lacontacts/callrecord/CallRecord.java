package fun.hara.lacontacts.callrecord;

/**
 * 通讯记录类结构
 */
import android.support.annotation.NonNull;

import com.alibaba.fastjson.annotation.JSONField;

import java.sql.Date;
import java.text.Collator;
import java.util.Comparator;

public  class CallRecord {

    private String name; // 名字
    private long date;     // 日期
    private int type;      // 类型：来电、去电、未接
    private String phone;   //电话

    public CallRecord(String name, long date, int type,String phone) {
        this.name = name;
        this.date = date;
        this.type = type;
        this.phone = phone;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
    @Override
    public String toString() {
        return "CallRecord{" +
                "name='" + name + '\'' +
                ", date='" + date +'\'' +
                ", type='" + type +'\'' +
                ",phone='"+phone+
                '}';
    }
}

