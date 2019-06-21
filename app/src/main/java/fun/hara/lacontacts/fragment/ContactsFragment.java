package fun.hara.lacontacts.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

import fun.hara.lacontacts.ContactEditActivity;
import fun.hara.lacontacts.adapter.ContactsAdapter;
import fun.hara.lacontacts.MainActivity;
import fun.hara.lacontacts.R;
import fun.hara.lacontacts.dao.ContactsDAO;
import fun.hara.lacontacts.domain.ContactInfo;


public class ContactsFragment extends Fragment {

    private boolean isGetData;  // 用于判断是否需要重新加载联系人列表
    private String keyword;     // 关键词:可以是姓名或电话(支持模糊查询)

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts,container,false);
        initContactList(view);
        return view;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //   进入当前Fragment
        if (enter && !isGetData) {
            isGetData = true;
            initContactList();
        } else {
            isGetData = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onPause() {
        super.onPause();
        isGetData = false;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 新增联系人按钮
        FloatingActionButton addContactBtn = (FloatingActionButton) getActivity().findViewById(R.id.addContactBtn);
        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ContactEditActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        // 重置联系人搜索框
        final ImageButton resetContactListBtn = (ImageButton) getActivity().findViewById(R.id.resetContactListBtn);
        resetContactListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setKeyword(null);
                TextView keywordTV = (TextView) getActivity().findViewById(R.id.keyword);
                keywordTV.setText("");
                resetContactListBtn.setVisibility(View.GONE);
                initContactList();
            }
        });
        // 联系人搜索处理
        ImageButton searchContactBtn = (ImageButton) getActivity().findViewById(R.id.searchContactBtn);
        searchContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity)getActivity();
                TextView keywordTV = (TextView) getActivity().findViewById(R.id.keyword);
                String keyword = keywordTV.getText().toString();
                if(TextUtils.isEmpty(keyword)){
                    Toast.makeText(getActivity(),"请输入联系人姓名或电话号码！" ,Toast.LENGTH_LONG ).show();
                    return;
                }
                setKeyword(keyword);
                // 显示重置按钮
                ImageButton resetContactListBtn = (ImageButton) getActivity().findViewById(R.id.resetContactListBtn);
                resetContactListBtn.setVisibility(View.VISIBLE);
                initContactList();
                mainActivity.switchNav(R.id.navigation_contacts);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        initContactList();
        if(requestCode==0 && resultCode == 1){
            Toast.makeText(getActivity() ,"新增成功",Toast.LENGTH_LONG ).show();
        }else if(requestCode==0 && resultCode==2){
            Toast.makeText(getActivity() ,"更新成功",Toast.LENGTH_LONG ).show();
        }
    }

    /**
     * 初始化联系人列表
     * @param view
     */
    private void initContactList(View view){
        // 获取所有联系人列表
        List<ContactInfo> list = new ContactsDAO(getActivity()).listAll();

        // 当传入的keyword不为null时，进行模糊匹配，筛选调不符合条件的数据
        if(!TextUtils.isEmpty(keyword)){
            Iterator<ContactInfo> it = list.iterator();
            while(it.hasNext()){
                ContactInfo contactInfo =  it.next();
                if(!(contactInfo.getName().contains(keyword) || contactInfo.getPhone().contains(keyword))){
                    it.remove();
                }
            }
        }
        // 将联系人数据填充到listview中
        ListView contactsInfo = (ListView)view.findViewById(R.id.contactsInfo);
        final ContactsAdapter adapter = new ContactsAdapter(list, getActivity());
        contactsInfo.setAdapter(adapter);
        // 为每行设置点击响应事件
        contactsInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ContactInfo contact = (ContactInfo) adapter.getItem(position);
                Intent intent = new Intent(getActivity(), ContactEditActivity.class);
                intent.putExtra("id", contact.getId().toString());
                intent.putExtra("name", contact.getName());
                intent.putExtra("phone", contact.getPhone());
                startActivityForResult(intent, 0);
            }
        });
    }

    /**
     *  初始化联系人列表，onCreateView()调用无效
     */
    private void initContactList(){
        initContactList(getView());
    }

    /**
     * 暴露给Activity传递参数
     * @param keyword
     */
    public void setKeyword(String keyword){
        this.keyword = keyword;
    }
}
