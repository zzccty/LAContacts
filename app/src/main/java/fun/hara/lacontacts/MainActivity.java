package fun.hara.lacontacts;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;


import fun.hara.lacontacts.domain.ContactInfo;
import fun.hara.lacontacts.fragment.CallFragment;
import fun.hara.lacontacts.fragment.ContactsFragment;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private CallFragment callFragment;
    private ContactsFragment contactsFragment;


    /**
     * 初始化相关fragment
     */
    public void initFragment() {
        callFragment = new CallFragment();
        contactsFragment = new ContactsFragment();
        // 开启一个事务将fragment动态加载到组件
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.add(R.id.content, callFragment).add(R.id.content, contactsFragment);
        // 隐藏fragment
        tx.hide(callFragment).hide(contactsFragment);
        //返回到上一个显示的fragment
        tx.addToBackStack(null);
        tx.commit();
        // 展示通话界面
        switchNav(R.id.navigation_call);
    }


    /**
     * 切换导航对应的fragment
     *
     * @param navId
     */
    public void switchNav(int navId) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        switch (navId) {
            case R.id.navigation_call:
                beginTransaction.hide(contactsFragment);
                beginTransaction.show(callFragment);
                break;
            case R.id.navigation_contacts:
                beginTransaction.hide(callFragment);
                beginTransaction.show(contactsFragment);
                break;
        }
        beginTransaction.addToBackStack(null);
        beginTransaction.commit();

    }


    /**
     * 应用权限检查
     */
    private void checkPermission() {
        // 权限
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALL_LOG)
                        != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)
                        != PackageManager.PERMISSION_GRANTED
                ) {
            ActivityCompat.requestPermissions((Activity) this,
                    new String[]{android.Manifest.permission.READ_CONTACTS,
                            android.Manifest.permission.WRITE_CONTACTS,
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CALL_PHONE,
                            android.Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.WRITE_CALL_LOG,
                            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
                    },
                    1);
        }

    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_call:
                    switchNav(R.id.navigation_call);
                    return true;
                case R.id.navigation_contacts:
                    switchNav(R.id.navigation_contacts);
                    return true;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();  // 权限检查
        setContentView(R.layout.activity_main);
        initFragment();     // 初始化fragment
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    public void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.main, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                MainActivity ctx = MainActivity.this;
                switch (itemId) {
                    // 设置每个item的响应事件
                    case R.id.action_scan:
                        Intent intent = new Intent(ctx, CaptureActivity.class);
                        startActivityForResult(intent, 0);
                        break;
                    case R.id.action_phoneInfo:
                        // 跳转到个人信息页面
                        Intent intent2 = new Intent(ctx, PhoneInfoActivity.class);
                        startActivityForResult(intent2, 0);
                        break;
                    case R.id.action_backup:
                        // 跳转到个人信息页面
                        Intent intent3 = new Intent(ctx, ContactsBackupActivity.class);
                        startActivityForResult(intent3, 0);
                        break;
                }
                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });
        popupMenu.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == 0 && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                String name = null;
                String phone = null;
                ContactInfo contactInfo = null;
                try{
                     contactInfo = JSON.parseObject(content, ContactInfo.class);
                    name = contactInfo.getName();
                    phone = contactInfo.getPhone();
                }catch (RuntimeException e){
                    Toast.makeText(this,"扫描结果: " + content,Toast.LENGTH_LONG ).show();
                    return;
                }
                // 空值检查
                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)){
                    Toast.makeText(this,"扫描结果: " + content,Toast.LENGTH_LONG ).show();
                    return;
                }
                Intent intent = new Intent(MainActivity.this, ContactEditActivity.class);
                intent.putExtra("name", contactInfo.getName());
                intent.putExtra("phone", contactInfo.getPhone());
                startActivity(intent);
            }
        }
    }
}
