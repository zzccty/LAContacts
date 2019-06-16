package fun.hara.lacontacts;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import fun.hara.lacontacts.dao.ContactsDAO;
import fun.hara.lacontacts.fragment.CallFragment;
import fun.hara.lacontacts.fragment.ContactsFragment;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private CallFragment callFragment;
    private ContactsFragment contactsFragment;


    /**
     * 初始化相关fragment
     */
    public void init() {


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
     *  切换导航对应的fragment
     * @param navId
     */
    private void switchNav(int navId) {
        FragmentTransaction beginTransaction= getSupportFragmentManager().beginTransaction();
        switch (navId){
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
           switch (item.getItemId()) {
                case  R.id.navigation_call:
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
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this,
                    new String[]{android.Manifest.permission.READ_CONTACTS},
                    1);
        }

        setContentView(R.layout.activity_main);
        init();
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



    }
}
