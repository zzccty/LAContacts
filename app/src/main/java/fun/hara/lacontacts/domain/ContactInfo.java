package fun.hara.lacontacts.domain;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.annotation.JSONField;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by hanaii on 2019/6/16.
 */

public class ContactInfo  implements Comparable<ContactInfo> {
    @JSONField(serialize=false)
    private Integer id;
    private String name;
    private String phone;

    public ContactInfo(Integer id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }
    public ContactInfo( String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public ContactInfo() {
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


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "ContactInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull ContactInfo o) {
        Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
        return com.compare(this.getName(),o.getName());
    }
}
