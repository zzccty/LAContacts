package fun.hara.lacontacts.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import fun.hara.lacontacts.ContactEditActivity;
import fun.hara.lacontacts.ContactsAdapter;
import fun.hara.lacontacts.R;
import fun.hara.lacontacts.dao.ContactsDAO;
import fun.hara.lacontacts.domain.ContactInfo;


public class ContactsFragment extends Fragment {
    private boolean isGetData = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_contacts,container,false);
        contactListViewInit(view); // 联系人listview初始化
        return view;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //   进入当前Fragment
        if (enter && !isGetData) {
            isGetData = true;
            contactListViewInit(this.getView());
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

    /**
     * 联系人listview初始化
     * @param view
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
                startActivity(intent);
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
        contactListViewInit(getView()); // 联系人listview初始化
        if(requestCode==0 && resultCode == 1){
            Toast.makeText(getActivity() ,"新增成功",Toast.LENGTH_LONG ).show();
        }
    }
}
