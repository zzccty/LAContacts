package fun.hara.lacontacts.adapter;

import android.content.Context;
import android.graphics.Color;
import android.provider.CallLog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fun.hara.lacontacts.R;
import fun.hara.lacontacts.domain.CallRecord;

/**
 * Created by hanaii on 2019/6/21.
 */

public class CallRecordAdapter extends BaseAdapter {
    private List<CallRecord> list = new ArrayList<>();
    private Context ctx;
    private LinearLayout layout;//TODO：视图格式
    public CallRecordAdapter(List<CallRecord> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }
    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        layout = (LinearLayout) inflater.inflate(R.layout.call_record_info, null);
        TextView nameRecord = (TextView) layout.findViewById(R.id.recordItemName);//名字
        TextView statusRecord = (TextView) layout.findViewById(R.id.recordItemStatus);//T呼出呼入
        TextView timeRecord = (TextView) layout.findViewById(R.id.recordItemTime);//时间
        String name = list.get(i).getName();
        // 若已存储联系人，则显示姓名，若未存储，则显示手机号码
        if(TextUtils.isEmpty(name)){
            nameRecord.setText(list.get(i).getPhone());
        }else{
            nameRecord.setText(name);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm" , Locale.getDefault());
        String dateString = format.format(list.get(i).getDate());
        timeRecord.setText(dateString);
//        // 类型
        String type = null;
        int textColor = 0;
        switch (list.get(i).getType()){
            case CallLog.Calls.INCOMING_TYPE: // 来电，字体蓝色
                type = "呼入";
                textColor = Color.rgb(47,153,101);
                break;
            case CallLog.Calls.OUTGOING_TYPE: // 去电，字体绿色
                type = "呼出";
                textColor = Color.rgb(0,183,195);
                break;
            case CallLog.Calls.MISSED_TYPE:   // 未接，字体红色
                type = "未接";
                textColor = Color.RED;
                break;
        }
        statusRecord.setText(type);
        statusRecord.setTextColor(textColor);
        return layout;
    }
}
