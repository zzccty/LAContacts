package fun.hara.lacontacts.domain;

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
