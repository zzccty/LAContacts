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


        Uri rawContacts = ContactsContract.RawContacts.CONTENT_URI;
        //1,获取当前最大的联系人id
        Cursor cursor = ctx.getContentResolver().query(rawContacts, new String[]{"contact_id"}, null, null, null);
        cursor.moveToLast();
        //生成最大的联系人id，这将是我们添加进去之后的id了
        int newId = cursor.getInt(0) + 1;
        cursor.close();

        //2.添加一个联系人id进raw_contacts表
        ContentValues contentValues = new ContentValues();
        contentValues.put("contact_id", newId);
         ctx.getContentResolver().insert(rawContacts, contentValues);

        Uri dataUri = ContactsContract.Data.CONTENT_URI;

        //3.添加信息
        contentValues.clear();
        contentValues.put("raw_contact_id", newId);//联系人id
        contentValues.put("data1", contactInfo.getName());//联系人名称
        contentValues.put("mimetype", "vnd.android.cursor.item/name");//联系人名称
        ctx.getContentResolver().insert(dataUri, contentValues);

        contentValues.clear();
        contentValues.put("raw_contact_id", newId);//联系人id
        contentValues.put("data1", contactInfo.getPhone());//数据
        contentValues.put("mimetype", "vnd.android.cursor.item/phone_v2");//数据类型
        ctx.getContentResolver().insert(dataUri, contentValues);

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
