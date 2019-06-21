package fun.hara.lacontacts;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.ImageButton;
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
        TextView mRecordTime = (TextView) findViewById(R.id.recordTime);

        // 回显信息
        String id = intent.getStringExtra("id");
        mRecordId.setText(id);
        String name = intent.getStringExtra("name");
        final String phone = intent.getStringExtra("phone");
        String time= intent.getStringExtra("date");
        String type = intent.getStringExtra("type");
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

}
