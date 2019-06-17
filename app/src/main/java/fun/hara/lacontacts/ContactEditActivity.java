package fun.hara.lacontacts;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import fun.hara.lacontacts.dao.ContactsDAO;
import fun.hara.lacontacts.domain.ContactInfo;
import fun.hara.lacontacts.util.QRCodeUtil;

public class ContactEditActivity extends AppCompatActivity {
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_SCAN = 0x0000;

    private ContactsDAO contactsDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactsDAO = new ContactsDAO(this);
        setContentView(R.layout.activity_contact_edit);

        Intent intent = getIntent();
        String id=intent.getStringExtra("id");
        // 回显信息
        String name = intent.getStringExtra("name");
        String phone = intent.getStringExtra("phone");
        TextView nameTV = (TextView) findViewById(R.id.editContactName);
        TextView phoneTV = (TextView) findViewById(R.id.editContactPhone);
        nameTV.setText(name);
        phoneTV.setText(phone);
        if(id!=null && !"".equals(id)){
            TextView idTV = (TextView)findViewById(R.id.contactId);
            idTV.setText(id);
            // 编辑时
            // 修改标题
            TextView editContactTitle = (TextView) findViewById(R.id.editContactTitle);
            editContactTitle.setText("编辑联系人");

            // 生成二维码
            ImageView QRCodeImg = (ImageView) findViewById(R.id.QRCodeImg);
            QRCodeImg.setVisibility(View.VISIBLE);
            Bitmap QRCodeBitmap = QRCodeUtil.createQRCodeBitmap(JSON.toJSONString(new ContactInfo(0,name,phone)), 480, 480);
            QRCodeImg.setImageBitmap(QRCodeBitmap);
            // 显示底部操作区
            RelativeLayout bottom = (RelativeLayout) findViewById(R.id.editContactBottom);
            bottom.setVisibility(View.VISIBLE);
        }else{
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) this,
                        new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
            ImageButton QRCodeScanBtn = (ImageButton) findViewById(R.id.QRCodeScanBtn);
            QRCodeScanBtn.setVisibility(View.VISIBLE);
        }
    }

    public void back(View view){
        setResult(0);
        finish();
    }
    public void saveContact(View view){
        TextView nameTV = (TextView)findViewById(R.id.editContactName);
        TextView phoneTV = (TextView)findViewById(R.id.editContactPhone);

        String name = nameTV.getText().toString();
        String phone = phoneTV.getText().toString();
        // 空值检查
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)){
            Toast.makeText(this,"姓名或电话不能为空！",Toast.LENGTH_LONG ).show();
            return;
        }

        contactsDAO.save(new ContactInfo(0, name, phone));
        setResult(1);
        finish();
    }

    public void deleteContact(View view){

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确定删除该联系人吗？")
                .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TextView idTV = (TextView)findViewById(R.id.contactId);
                        Integer id = Integer.parseInt(idTV.getText().toString());
                        contactsDAO.delete(id);
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

    public void scan(View view){
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    public void editContact(View view){
        view.setVisibility(View.GONE);
        ImageButton updateContactBtn = (ImageButton) findViewById(R.id.updateContactBtn);
        ImageButton cancelEditBtn1 = (ImageButton) findViewById(R.id.cancelEditBtn1);

        updateContactBtn.setVisibility(View.VISIBLE);
        cancelEditBtn1.setVisibility(View.VISIBLE);
    }
    public void updateContact(View view){
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(Constant.CODED_CONTENT);
                String name = null;
                String phone = null;
                try{
                    ContactInfo contactInfo = JSON.parseObject(content, ContactInfo.class);
                     name = contactInfo.getName();
                     phone = contactInfo.getPhone();
                }catch (RuntimeException e){
                    Toast.makeText(this,"二维码格式错误！",Toast.LENGTH_LONG ).show();
                    return;
                }

                // 空值检查
                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)){
                    Toast.makeText(this,"二维码格式错误！",Toast.LENGTH_LONG ).show();
                    return;
                }
                TextView nameTV = (TextView)findViewById(R.id.editContactName);
                TextView phoneTV = (TextView)findViewById(R.id.editContactPhone);
                nameTV.setText(name);
                phoneTV.setText(phone);

            }
        }
    }



}
