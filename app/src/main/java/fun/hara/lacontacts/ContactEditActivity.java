package fun.hara.lacontacts;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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
import fun.hara.lacontacts.util.CallUtil;
import fun.hara.lacontacts.util.MessageUtil;
import fun.hara.lacontacts.util.QRCodeUtil;

public class ContactEditActivity extends AppCompatActivity {
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_SCAN = 0x0000;
    private ContactsDAO contactsDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_edit);
        init();
    }

    /**
     * 初始化操作
     */
    private void init(){
        // 创建DAO对象
        contactsDAO = new ContactsDAO(this);

        // 使用到的控件
        EditText nameET = (EditText) findViewById(R.id.editContactName);
        EditText phoneET = (EditText) findViewById(R.id.editContactPhone);

        // 获取intent中的数据
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String phone = intent.getStringExtra("phone");
        String id = intent.getStringExtra("id");

        // 回显数据
        nameET.setText(name);
        phoneET.setText(phone);

        if(!TextUtils.isEmpty(id)){
            /* id存在时 */
            TextView idTV = (TextView)findViewById(R.id.contactId);
            idTV.setText(id);
            // 修改标题
            TextView editContactTitle = (TextView) findViewById(R.id.editContactTitle);
            editContactTitle.setText("编辑联系人");
            // 禁用编辑
            uneditable(nameET, phoneET);
            // 生成二维码
            ImageView QRCodeImg = (ImageView) findViewById(R.id.QRCodeImg);
            QRCodeImg.setVisibility(View.VISIBLE);
            Bitmap QRCodeBitmap = QRCodeUtil.createQRCodeBitmap(JSON.toJSONString(new ContactInfo(0,name,phone)), 480, 480);
            QRCodeImg.setImageBitmap(QRCodeBitmap);
            // 显示底部操作区
            RelativeLayout bottom = (RelativeLayout) findViewById(R.id.editContactBottom);
            bottom.setVisibility(View.VISIBLE);
        }else{
            /* id不存在时，即新增联系人 */
            // 使用到的控件
            ImageButton editModeBtn = (ImageButton) findViewById(R.id.editModeBtn);
            ImageButton saveContactBtn = (ImageButton) findViewById(R.id.saveContactBtn);
            ImageButton QRCodeScanBtn = (ImageButton) findViewById(R.id.QRCodeScanBtn);
            // 可见性设置
            editModeBtn.setVisibility(View.GONE);
            saveContactBtn.setVisibility(View.VISIBLE);
            QRCodeScanBtn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 拨号
     * @param view
     */
    public void call(View view){
        EditText phoneET = (EditText) findViewById(R.id.editContactPhone);
        CallUtil.call(ContactEditActivity.this, phoneET.getText().toString());
    }

    /**
     * 普通回退
     * @param view
     */
    public void back(View view){
        setResult(0);
        finish();
    }


    /**
     * 跳转到短信APP
     * @param view
     */
    public void jumpMessageApp(View view){
        EditText phoneET = (EditText) findViewById(R.id.editContactPhone);
        MessageUtil.jumpMessageApp(ContactEditActivity.this, phoneET.getText().toString());
    }


    /**
     * 保存联系人
     * @param view
     */
    public void saveContact(View view){
        EditText nameET = (EditText) findViewById(R.id.editContactName);
        EditText phoneET = (EditText) findViewById(R.id.editContactPhone);
        String name = nameET.getText().toString();
        String phone = phoneET.getText().toString();
        // 空值检查
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)){
            Toast.makeText(this,"姓名或电话不能为空！",Toast.LENGTH_LONG ).show();
            return;
        }
        contactsDAO.save(new ContactInfo(0, name, phone));
        setResult(1);
        finish();
    }

    /**
     * 删除联系人
     * @param view
     */
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

    /**
     * 切换到编辑模式
     * @param view
     */
    public void switchEditMode(View view){
        EditText nameET = (EditText) findViewById(R.id.editContactName);
        EditText phoneET = (EditText) findViewById(R.id.editContactPhone);
        view.setVisibility(View.GONE);
        ImageButton updateContactBtn = (ImageButton) findViewById(R.id.updateContactBtn);
        ImageButton cancelEditBtn1 = (ImageButton) findViewById(R.id.cancelEditBtn1);
        updateContactBtn.setVisibility(View.VISIBLE);
        cancelEditBtn1.setVisibility(View.VISIBLE);
        editable(nameET, phoneET);
        RelativeLayout editContactBottom = (RelativeLayout) findViewById(R.id.editContactBottom);
        editContactBottom.setVisibility(View.GONE);
    }

    /**
     * 退出更新模式
     * @param view
     */
    public void cancelEditMode(View view){
        EditText nameET = (EditText) findViewById(R.id.editContactName);
        EditText phoneET = (EditText) findViewById(R.id.editContactPhone);
        view.setVisibility(View.GONE);
        ImageButton updateContactBtn = (ImageButton) findViewById(R.id.updateContactBtn);
        ImageButton editModeBtn = (ImageButton) findViewById(R.id.editModeBtn);
        updateContactBtn.setVisibility(View.GONE);
        editModeBtn.setVisibility(View.VISIBLE);
        RelativeLayout editContactBottom = (RelativeLayout) findViewById(R.id.editContactBottom);
        editContactBottom.setVisibility(View.VISIBLE);
        uneditable(nameET, phoneET);

        nameET.setText(getIntent().getStringExtra("name"));
        phoneET.setText( getIntent().getStringExtra("phone"));
    }

    /**
     * 更新联系人
     * @param view
     */
    public void updateContact(View view){
        EditText nameET = (EditText) findViewById(R.id.editContactName);
        EditText phoneET = (EditText) findViewById(R.id.editContactPhone);
        String name = nameET.getText().toString();
        String phone = phoneET.getText().toString();
        TextView idTV = (TextView)findViewById(R.id.contactId);
        Integer id = Integer.parseInt(idTV.getText().toString());
        // 空值检查
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)){
            Toast.makeText(this,"姓名或电话不能为空！",Toast.LENGTH_LONG ).show();
            return;
        }
        contactsDAO.update(new ContactInfo(id, name, phone));
        setResult(2);
        finish();
    }

    /**
     * 扫描二维码
     * @param view
     */
    public void scan(View view){
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


    /**
     * 让EditText不可编辑
     * @param editTexts
     */
    private void uneditable(EditText ... editTexts){
        for (EditText editText : editTexts) {
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
        }

    }

    /**
     * 让EditText可被编辑
     * @param editTexts
     */
    private void editable(EditText ... editTexts){
        for (EditText editText : editTexts) {
            editText.setFocusableInTouchMode(true);
            editText.setFocusable(true);
            editText.requestFocus();
        }
    }


}
