package fun.hara.lacontacts;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


import fun.hara.lacontacts.dao.CallRecordDAO;
import fun.hara.lacontacts.util.CallUtil;
import fun.hara.lacontacts.util.MessageUtil;

/**
 * TODO:通讯记录编辑界面
 */
public class CallRecordEditActivity extends AppCompatActivity {
    private CallRecordDAO callRecordDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_record_edit);
        init();
    }

    private void init(){
        Intent intent = getIntent();
        // 使用到的控件
        TextView mRecordId  = (TextView) findViewById(R.id.recordId);
        TextView mRecordName = (TextView) findViewById(R.id.recordName);
        TextView mRecordPhone = (TextView) findViewById(R.id.recordPhone);
        TextView mRecordStatus = (TextView) findViewById(R.id.recordStatus);

        // 回显信息
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        final String phone = intent.getStringExtra("phone");
        String time= intent.getStringExtra("date");
        String type = intent.getStringExtra("type");

        mRecordId.setText(id);
        if(TextUtils.isEmpty(name)){
            mRecordName.setText("未存储");
            RelativeLayout saveContactView = (RelativeLayout) findViewById(R.id.saveContactView);
            saveContactView.setVisibility(View.VISIBLE);
        }else{
            mRecordName.setText(name);
        }
        mRecordPhone.setText(phone);
        mRecordStatus.setText(time + "\t" + type);

        // 按钮相关事件
        ImageButton mRecordBackBtn = (ImageButton) findViewById(R.id.recordBackBtn);
        ImageButton mRecordCallBtn = (ImageButton) findViewById(R.id.recordCallBtn);
        ImageButton mRecordMessageBtn = (ImageButton) findViewById(R.id.recordMessageBtn);
        mRecordBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//返回按钮
                setResult(0);
                finish();
            }
        });
        final CallRecordEditActivity activity = CallRecordEditActivity.this;
        mRecordCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //拨号按钮
                CallUtil.call(activity, phone);
            }
        });
        mRecordMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//发送信息按钮
                MessageUtil.jumpMessageApp(activity, phone);
            }
        });
    }
    /**
     * 删除联系人
     * @param view
     */
    public void delete(View view){
        callRecordDAO = new CallRecordDAO(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确定删除该通话记录吗？")
                .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TextView idTV = (TextView)findViewById(R.id.recordId);
                        callRecordDAO.delete(idTV.getText().toString());
                        setResult(0);
                        finish();
                    }
                })
                .setNegativeButton("取消",null)
                .create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
    }

    /**
     * 保存联系人
     * @param view
     */
    public void saveContact(View view){
        TextView recordPhone = (TextView) findViewById(R.id.recordPhone);
        Intent intent = new Intent(CallRecordEditActivity.this, ContactEditActivity.class);
        intent.putExtra("phone", recordPhone.getText().toString());
        startActivity(intent);
    }

    /**
     * 删除指定电话号码对应的通话记录
     * @param view
     */
    public void deleteContactsByPhone(View view){
        callRecordDAO = new CallRecordDAO(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确定删除该号码对应的所有通话记录吗？")
                .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TextView recordPhone = (TextView) findViewById(R.id.recordPhone);
                        callRecordDAO.deleteRecordsByPhone(recordPhone.getText().toString());
                        setResult(0);
                        finish();
                    }
                })
                .setNegativeButton("取消",null)
                .create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
    }
}
