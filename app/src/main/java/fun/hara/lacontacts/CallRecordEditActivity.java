package fun.hara.lacontacts;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CallLog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import fun.hara.lacontacts.dao.ContactsDAO;
import fun.hara.lacontacts.domain.ContactInfo;
import fun.hara.lacontacts.fragment.CallFragment;
import fun.hara.lacontacts.getcallrecord.GetCallRecord;

import static android.provider.CallLog.Calls.INCOMING_TYPE;

/**
 * TODO:通讯记录编辑界面
 */
public class CallRecordEditActivity extends AppCompatActivity {
    private GetCallRecord getCallList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_record_edit);
        init();
    }

    private void init(){
        Intent intent = getIntent();
        // 回显信息
        String name = intent.getStringExtra("name");
        final String phone = intent.getStringExtra("phone");
        String time= intent.getStringExtra("date");
        String type = intent.getStringExtra("type");
        TextView mRecordName = (TextView) findViewById(R.id.recordName);
        TextView mRecordPhone = (TextView) findViewById(R.id.recordPhone);
        TextView mRecordStatus = (TextView) findViewById(R.id.recordStatus);
        TextView mRecordTime = (TextView) findViewById(R.id.recordTime);
        mRecordName.setText(name);
        mRecordPhone.setText(phone);
        String status = null;
        switch (type){
            case "2": // 来电
                status = "呼入";
                break;
            case "1": // 去电
                status = "呼出";
                break;
            case "0":   // 未接
                status = "未接";
                break;
        }//判断颜色
        mRecordStatus.setText(status);
        mRecordTime.setText(time);
        ImageButton mRecordBackBtn = (ImageButton) findViewById(R.id.recordBackBtn);
        ImageButton mRecordDeleteBtn = (ImageButton) findViewById(R.id.recordDeleteBtn);
        ImageButton mRecordCallBtn = (ImageButton) findViewById(R.id.recordCallBtn);
        ImageButton mRecordMessageBtn = (ImageButton) findViewById(R.id.recordMessageBtn);
        mRecordBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//返回按钮
                setResult(0);
                finish();
            }
        });
        mRecordDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//删除按钮

            }
        });
        mRecordCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//拨号按钮
                Intent intent = new Intent();
                //设置拨打电话的动作
                intent.setAction(Intent.ACTION_CALL);
                //设置拨打电话的号码
                intent.setData(Uri.parse("tel:" + phone));
                //开启打电话的意图
                startActivity(intent);
            }
        });
        mRecordMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//发送信息按钮
                doSendSMSTo(phone,"");
            }
        });
    }
    public void doSendSMSTo(String phoneNumber,String message){
        if(PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)){
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phoneNumber));
            intent.putExtra("sms_body", message);
            startActivity(intent);
        }
    }
}
