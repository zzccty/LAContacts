package fun.hara.lacontacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import fun.hara.lacontacts.domain.ContactInfo;

/**
 * Created by hanaii on 2019/6/16.
 */

public class ContactsAdapter extends BaseAdapter {
    private List<ContactInfo> list;
    private Context ctx;
    private LinearLayout layout;
    public ContactsAdapter(List<ContactInfo> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        layout = (LinearLayout) inflater.inflate(R.layout.contact_info, null);
        TextView nameTV = (TextView) layout.findViewById(R.id.contactName);
        TextView phoneTV = (TextView) layout.findViewById(R.id.contactPhone);
        nameTV.setText(list.get(i).getName());
        phoneTV.setText(list.get(i).getPhone());
        return layout;
    }
}
