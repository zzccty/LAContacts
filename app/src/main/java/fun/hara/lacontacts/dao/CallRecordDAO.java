package fun.hara.lacontacts.dao;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import fun.hara.lacontacts.domain.CallRecord;
import fun.hara.lacontacts.domain.ContactInfo;

/**
 * Created by hanaii on 2019/6/21.
 */

public class CallRecordDAO {
    private Context ctx;


    public CallRecordDAO(Context ctx) {
        this.ctx = ctx;
    }

    public List<CallRecord> listAll(){
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        List<CallRecord> infos = new LinkedList<>();
        Cursor cursor = ctx.getContentResolver().query(
                CallLog.Calls.CONTENT_URI,
                null,
                null,
                null,
                CallLog.Calls.DEFAULT_SORT_ORDER
        );
        List<ContactInfo> contacts = new ContactsDAO(ctx).listAll();
        //游标指向下一组数据
            while (cursor.moveToNext()) {
                Integer id = cursor.getInt(cursor.getColumnIndex(CallLog.Calls._ID));
                //String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                String phone = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                String name = queryNameByPhone(phone, contacts);
                long date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
                int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
                infos.add(new CallRecord(id, name, date, type, phone));
            }
        return infos;//返回数据
    }

    private String queryNameByPhone(String phone, List<ContactInfo> contacts) {
        for (ContactInfo contact : contacts) {
            if(contact.getPhone().equals(phone)){
                return contact.getName();
            }
        }
        return null;
    }

    public void delete(String id) {
        ContentResolver resolver = ctx.getContentResolver();
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        int result = resolver.delete(CallLog.Calls.CONTENT_URI, "_id=? ", new String[]{id});
    }

    /**
     * 删除电话号码对应的所有通话记录
     * @param phone
     */
    public void deleteRecordsByPhone(String phone){
        ContentResolver resolver = ctx.getContentResolver();
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        int result = resolver.delete(CallLog.Calls.CONTENT_URI, "number=? ", new String[]{phone});
    }


}
