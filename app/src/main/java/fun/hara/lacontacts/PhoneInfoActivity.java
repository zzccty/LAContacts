package fun.hara.lacontacts;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import fun.hara.lacontacts.domain.ContactInfo;
import fun.hara.lacontacts.preferences.PhoneInfoPreferences;
import fun.hara.lacontacts.util.EditTextUtil;
import fun.hara.lacontacts.util.QRCodeUtil;

public class PhoneInfoActivity extends AppCompatActivity {

    public static final String FILE = "/sdcard/lacontacts/PhoneInfo.properties";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_info);
        init();

    }

    private void init(){
        ContactInfo info = getConfig(this);
        // 读取配置文件
        String name = info.getName();
        String phone = info.getPhone();
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)){
            Toast.makeText(this, "请先编辑个人信息！", Toast.LENGTH_LONG).show();
            // 跳转到编辑模式
            modeChange(true);
        }else{
            // 生成二维码
            setQRImage(name, phone);
            // 设置基本信息
            setBaseInfo(name, phone);

            modeChange(false);
        }
    }

    /**
     * 切换到编辑模式
     * @param view
     */
    public void switchEditMode(View view){
        modeChange(true);
    }

    /**
     * 更新个人信息
     * @param view
     */
    public void updatePhoneInfo(View view){
        // 使用到的控件
        EditText namePI = (EditText) findViewById(R.id.namePI);
        EditText phonePI = (EditText) findViewById(R.id.phonePI);

        // 判断是否为空
        String name = namePI.getText().toString();
        String phone = phonePI.getText().toString();
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)){
            Toast.makeText(this, "姓名或电话不能为空！", Toast.LENGTH_LONG).show();
            return;
        }

        // 保存
       saveConfig(name,phone);
        setQRImage(name, phone);
        modeChange(false);
    }

    private void saveConfig(String name, String phone) {
        SharedPreferences preferences = getSharedPreferences("PhoneInfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("name", name);
        editor.putString("phone", phone);
        editor.commit();//提交数据
    }

    public void cancelEditPI(View view){
        ContactInfo info = getConfig(this);
        // 读取配置
        String name = info.getName();
        String phone = info.getPhone();
        setBaseInfo(name, phone);
    }




    private void setBaseInfo(String name, String phone){
        // 使用到的控件
        EditText namePI = (EditText) findViewById(R.id.namePI);
        EditText phonePI = (EditText) findViewById(R.id.phonePI);
        namePI.setText(name);
        phonePI.setText(phone);
        modeChange(false);
    }

    private void modeChange(boolean isEdit){
        // 使用到的控件
        EditText namePI = (EditText) findViewById(R.id.namePI);
        EditText phonePI = (EditText) findViewById(R.id.phonePI);
        ImageButton editModeBtnPI = findViewById(R.id.editModeBtnPI);
        ImageButton updatePIBtn = findViewById(R.id.updatePIBtn);
        ImageButton cancelEditPIBtn = findViewById(R.id.cancelEditPIBtn);
        // 是编辑模式
        if(isEdit){
            editModeBtnPI.setVisibility(View.GONE);
            updatePIBtn.setVisibility(View.VISIBLE);
            cancelEditPIBtn.setVisibility(View.VISIBLE);
            EditTextUtil.editable(namePI,phonePI);
        }else{
            updatePIBtn.setVisibility(View.GONE);
            cancelEditPIBtn.setVisibility(View.GONE);
            editModeBtnPI.setVisibility(View.VISIBLE);
            EditTextUtil.uneditable(namePI,phonePI);
        }
    }

    /**
     * 设置二维码
     * @param name
     * @param phone
     */
    private void setQRImage(String name, String phone){
        // 生成二维码
        ImageView QRImagePI = (ImageView) findViewById(R.id.QRImagePI);
        Bitmap QRCodeBitmap = QRCodeUtil.createQRCodeBitmap(JSON.toJSONString(new ContactInfo(0,name,phone)), 480, 480);
        QRImagePI.setImageBitmap(QRCodeBitmap);
    }

    private ContactInfo getConfig(Context ctx){
        SharedPreferences preference=  getSharedPreferences("PhoneInfo", MODE_PRIVATE);
        Map<String, String> map=new HashMap<String, String>();
        String name = preference.getString("name", "");
        String phone = preference.getString("phone", "");
        return new ContactInfo(name, phone);

    }



    /**
     * 普通回退
     * @param view
     */
    public void back(View view){
        setResult(0);
        finish();
    }

}
