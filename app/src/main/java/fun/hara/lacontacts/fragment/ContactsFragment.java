package fun.hara.lacontacts.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

import fun.hara.lacontacts.ContactEditActivity;
import fun.hara.lacontacts.ContactsAdapter;
import fun.hara.lacontacts.R;
import fun.hara.lacontacts.dao.ContactsDAO;
import fun.hara.lacontacts.domain.ContactInfo;


public class ContactsFragment extends Fragment {

    private boolean isGetData;  // 是否重新加载联系人列表
    private String keyword;     // 关键词:可以是姓名或电话(支持模糊查询)

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_contacts,container,false);
        if(this.keyword != null){
            contactListViewInit(view, keyword);
        }else{
            contactListViewInit(view); // 联系人listview初始化
        }
        return view;
    }

    /**
     * 初始化联系人列表
     */
    private void initContactList(View view){
        if(this.keyword != null){
            contactListViewInit(getView(), keyword);
        }else{
            contactListViewInit(getView()); // 联系人listview初始化
        }
    }

    private void contactListViewInit(View view, String keyword) {
        // 获取联系人列表，填充到listview中
        List<ContactInfo> list = new ContactsDAO(getActivity()).listAll();
        Iterator<ContactInfo> it = list.iterator();
        while(it.hasNext()){
            ContactInfo contactInfo =  it.next();
            if(!(contactInfo.getName().contains(keyword) || contactInfo.getPhone().contains(keyword))){
                it.remove();
            }
        }

        if(list.size() == 0){
            Toast.makeText(getActivity(),"不存在对应的记录！" ,Toast.LENGTH_LONG ).show();
            return;
        }
        ListView contactsInfo = (ListView)view.findViewById(R.id.contactsInfo);
        final ContactsAdapter adapter = new ContactsAdapter(list, getActivity());
        contactsInfo.setAdapter(adapter);
        // 设置点击事件
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

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //   进入当前Fragment
        if (enter && !isGetData) {
            isGetData = true;
            if(this.keyword != null){
                contactListViewInit(getView(), keyword);
            }else{
                contactListViewInit(getView()); // 联系人listview初始化
            }
        } else {
            isGetData = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

   /* private void contactListViewInit() {
        List<ContactInfo> list = new ContactsDAO(getActivity()).listAll();
        contactListViewInit(list);
    }*/

    @Override
    public void onPause() {
        super.onPause();
        isGetData = false;
    }

    /**
     * 联系人listview初始化
     */
    private void contactListViewInit(View view){
        // 获取联系人列表，填充到listview中
        List<ContactInfo> list = new ContactsDAO(getActivity()).listAll();
        ListView contactsInfo = (ListView)view.findViewById(R.id.contactsInfo);
        final ContactsAdapter adapter = new ContactsAdapter(list, getActivity());
        contactsInfo.setAdapter(adapter);
        // 设置点击事件
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
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 新增联系人按钮跳转
        FloatingActionButton addContactBtn = (FloatingActionButton) getActivity().findViewById(R.id.addContactBtn);
        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ContactEditActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 刷新
        if(this.keyword != null){
            contactListViewInit(getView(), keyword);
        }else{
            contactListViewInit(getView()); // 联系人listview初始化
        }
        contactListViewInit(getView());     // 联系人listview初始化
        if(requestCode==0 && resultCode == 1){
            Toast.makeText(getActivity() ,"新增成功",Toast.LENGTH_LONG ).show();
        }
    }
    /**
     * 暴露给Activity传递参数
     * @param keyword
     */
    public void setKeyword(String keyword){
        this.keyword = keyword;
    }
}
