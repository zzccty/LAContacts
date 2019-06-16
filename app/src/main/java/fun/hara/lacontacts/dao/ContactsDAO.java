package fun.hara.lacontacts.dao;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import fun.hara.lacontacts.domain.ContactInfo;

import static android.support.v4.app.ActivityCompat.requestPermissions;

/**
 * Created by hanaii on 2019/6/15.
 */

public class ContactsDAO {

    /**
     * 获取联系人列表
     * @param ctx
     */
    public static List<ContactInfo> listAll(Context ctx){

       Cursor cursor = ctx.getContentResolver().query(
                Phone.CONTENT_URI,
                null,
                    null,
                null,
                null
        );
        List<ContactInfo> list = new LinkedList<>();
       while(cursor.moveToNext()){
                list.add(new ContactInfo(  //
                        cursor.getInt(cursor.getColumnIndex(Phone._ID)),  //
                        cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME)),  //
                        cursor.getString(cursor.getColumnIndex(Phone.NUMBER)) //
                ));
            }
        cursor.close();
        Object[] arr = list.toArray();
        Arrays.sort(arr);
        list.clear();
        for(int i=0; i<arr.length; i++){
            list.add((ContactInfo) arr[i]);
        }
       return list;
    }
}
