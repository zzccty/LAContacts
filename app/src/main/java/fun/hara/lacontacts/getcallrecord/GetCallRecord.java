package fun.hara.lacontacts.getcallrecord;

/**
 * 用于获取通信信息
 */
import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import fun.hara.lacontacts.callrecord.CallRecord;
import fun.hara.lacontacts.domain.ContactInfo;

import static android.provider.CallLog.CONTENT_URI;

public class GetCallRecord {

    /**
     * 获取通话记录
     * @param context 上下文。通话记录需要从系统的【通话应用】中的内容提供者中获取，内容提供者需要上下文。通话记录保存在联系人数据库中：data/data/com.android.provider.contacts/databases/contacts2.db库中的calls表。
     * @return 包含所有通话记录的一个集合
     */
//    private  final int sum=0;
    public int sum;
    private static final String TAG = "GetCallRecord";
    public List<CallRecord> getCallInfos(Context context) {
//        List<CallRecord> infos = new ArrayList<CallRecord>();
        List<CallRecord> infos = new LinkedList<>();//为了排序而设
        List<CallRecord> infos2 = new LinkedList<>();//真正返回的list
        ContentResolver resolver = context.getContentResolver();
         Uri uri=  CallLog.Calls.CONTENT_URI;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i(TAG, "授权失败，无法获取通话记录！");
            return null;
        }
        Cursor cursor = resolver.query(uri,null, null, null, null);
        //游标指向下一组数据
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            long date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
            int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
            String phone=cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            infos.add(new CallRecord(name, date, type,phone));
        }
//        Log.v(TAG, "getCallInfos: "+sum);
        //游标指向下一组数据，为了逆序
        while (cursor.moveToPrevious()){
//            Log.v(TAG, "getCallInfos: "+sum);
            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            long date = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
            int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
            String phone=cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            infos2.add(new CallRecord(name, date, type,phone));
        }
        cursor.close();
        return infos2;//返回数据
    }


}
