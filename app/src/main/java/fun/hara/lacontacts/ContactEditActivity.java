package fun.hara.lacontacts;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import fun.hara.lacontacts.dao.ContactsDAO;
import fun.hara.lacontacts.domain.ContactInfo;
import fun.hara.lacontacts.util.QRCodeUtil;

public class ContactEditActivity extends AppCompatActivity {
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_SCAN = 0x0000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_edit);
        /*
        Intent intent = getIntent();
        String id=intent.getStringExtra("id");
        if(id!=null && !"".equals(id)){
            // 编辑时
            // 修改标题
            TextView editContactTitle = (TextView) findViewById(R.id.editContactTitle);
            editContactTitle.setText("编辑联系人");
            // 回显信息
            String name = intent.getStringExtra("name");
            String phone = intent.getStringExtra("phone");
            TextView nameTV = (TextView) findViewById(R.id.editContactName);
            TextView phoneTV = (TextView) findViewById(R.id.editContactPhone);
            nameTV.setText(name);
            phoneTV.setText(phone);
            // 生成二维码
            ImageView QRCodeImg = (ImageView) findViewById(R.id.QRCodeImg);
            QRCodeImg.setVisibility(View.VISIBLE);
            Bitmap QRCodeBitmap = QRCodeUtil.createQRCodeBitmap(JSON.toJSONString(new ContactInfo(0,name,phone)), 480, 480);
            QRCodeImg.setImageBitmap(QRCodeBitmap);
            // 显示底部操作区
            RelativeLayout bottom = (RelativeLayout) findViewById(R.id.editContactBottom);
            bottom.setVisibility(View.VISIBLE);
        }else{
            ImageButton QRCodeScanBtn = (ImageButton) findViewById(R.id.QRCodeScanBtn);
            QRCodeScanBtn.setVisibility(View.VISIBLE);
        }*/
    }

    public void back(View view){
        setResult(0);
        finish();
    }
    public void saveContact(View view){
       /* TextView nameTV = (TextView)findViewById(R.id.editContactName);
        TextView phoneTV = (TextView)findViewById(R.id.editContactPhone);

        String name = nameTV.getText().toString();
        String phone = phoneTV.getText().toString();
        // 空值检查
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)){
            Toast.makeText(this,"姓名或电话不能为空！",Toast.LENGTH_LONG ).show();
            return;
        }


        new ContactsDAO(this).save(new ContactInfo(0, name, phone));
        setResult(1);
        finish();*/
    }
/*
    *//**
     * 跳转到扫码界面扫码
     *//*
    private void goScan(){
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goScan();
                } else {
                    Toast.makeText(this, "你拒绝了权限申请，可能无法打开相机扫码哟！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                //返回的文本内容
                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                //返回的BitMap图像
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
                Toast.makeText(this,content,Toast.LENGTH_LONG ).show();
            }
        }
    }*/
}
