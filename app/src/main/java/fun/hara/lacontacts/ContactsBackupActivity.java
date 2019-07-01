package fun.hara.lacontacts;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fun.hara.lacontacts.dao.ContactsDAO;
import fun.hara.lacontacts.domain.ContactInfo;
import fun.hara.lacontacts.util.FileUtil;

public class ContactsBackupActivity extends AppCompatActivity {

    private FileUtil fileUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_backup);
        fileUtil =  new FileUtil();
    }

    /**
     * 备份联系人
     * @param view
     */
    public void backup(View view){
        List<ContactInfo> contacts = new ContactsDAO(this).listAll();
         String data = JSON.toJSONString(contacts);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HH_mm_ss", Locale.getDefault());
        String filename = "lacontacts_contacts_"+format.format(new Date()).toString()+".la";
        // 保存数据
        FileUtil.saveFile(data, filename);
        Toast.makeText(this, "联系人列表已备份：" + filename, Toast.LENGTH_LONG).show();
    }

    /**
     * 普通回退
     * @param view
     */
    public void back(View view){
        setResult(0);
        finish();
    }

    public void recover(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                String path = uri.getPath().toString();
                if(!path.endsWith("la")){
                    Toast.makeText(this, "文件格式错误！", Toast.LENGTH_LONG).show();
                    return;
                }
                try{
                    Toast.makeText(this, "恢复中，请稍等", Toast.LENGTH_LONG).show();

                    String contactsData = FileUtil.getFile(path);;
                    List<ContactInfo> list = JSON.parseArray(contactsData, ContactInfo.class);
                    for (ContactInfo contactInfo : list) {
                        new ContactsDAO(this).save(contactInfo);
                    }
                    Toast.makeText(this, "联系人列表已恢复", Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(this, "文件数据错误！", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
