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
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import fun.hara.lacontacts.ContactEditActivity;
import fun.hara.lacontacts.ContactsAdapter;
import fun.hara.lacontacts.R;
import fun.hara.lacontacts.dao.ContactsDAO;
import fun.hara.lacontacts.domain.ContactInfo;


public class ContactsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_contacts,container,false);
        List<ContactInfo> list = ContactsDAO.listAll(getActivity());
        ListView contactsInfo = (ListView)view.findViewById(R.id.contactsInfo);
        final ContactsAdapter adapter = new ContactsAdapter(list, getActivity());
        contactsInfo.setAdapter(adapter);
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


        return view;
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
                startActivity(intent);

            }
        });


    }
}
