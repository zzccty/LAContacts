package fun.hara.lacontacts.dao;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import fun.hara.lacontacts.domain.ContactInfo;



public class ContactsDAO {

    private Context ctx;

    public ContactsDAO(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * 获取联系人列表
     */
    public List<ContactInfo> listAll(){
       List<ContactInfo> list = new LinkedList<>();
       Cursor cursor = ctx.getContentResolver().query(
                Phone.CONTENT_URI,
                null,
                    null,
                null,
                null
        );
       while(cursor.moveToNext()){
                list.add(new ContactInfo(  //
                        cursor.getInt(cursor.getColumnIndex(Phone._ID)),  //
                        cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME)),  //
                        cursor.getString(cursor.getColumnIndex(Phone.NUMBER)) //
                ));
        }
        cursor.close();
        // 排序
        Object[] arr = list.toArray();
        Arrays.sort(arr);
        list.clear();
        for(int i=0; i<arr.length; i++){
            list.add((ContactInfo) arr[i]);
        }
       return list;
    }

    /**
     * 保存联系人信息
     * @param contactInfo
     */
    public void save(ContactInfo contactInfo){

        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)  // 此处传入null添加一个raw_contact空数据
                .build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)  // RAW_CONTACT_ID是第一个事务添加得到的，因此这里传入0，applyBatch返回的ContentProviderResult[]数组中第一项
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contactInfo.getName())
                .build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contactInfo.getPhone())
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                .build());

        try {
            ctx.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据指定id删除对应联系人数据
     * @param id
     */
    public void delete(Integer id){
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        ops.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
                .withSelection(ContactsContract.Data._ID + "=?", new String[]{String.valueOf(id)})
                .build());
        try {
            ctx.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新指定联系人数据
     * @param contactInfo 必须包含id
     */
    public void update(ContactInfo contactInfo){
       delete(contactInfo.getId());
       save(contactInfo);
    }
}
