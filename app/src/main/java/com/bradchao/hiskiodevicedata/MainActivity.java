package com.bradchao.hiskiodevicedata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private ContentResolver cr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                        == PackageManager.PERMISSION_GRANTED
          ){
            init();
        }else{
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG},123);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            init();
        }else{
            finish();
        }
    }

    private void init(){
        cr = getContentResolver();
    }

    private Uri uriSettings  = Settings.System.CONTENT_URI;
    public void fetchSetting(View view){
        Cursor cursor = cr.query(uriSettings, null,null,null,null);
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String value = cursor.getString(cursor.getColumnIndex("value"));
            Log.v("bradlog", name + " = " + value);
        }

    }

    private Uri uriContact  = ContactsContract.Contacts.CONTENT_URI;
    public void fetchContact(View view) {
        Cursor cursor = cr.query(uriContact, null,null,null,null);
        String[] fields = cursor.getColumnNames();
        for (String field : fields){
            Log.v("bradlog", field);
        }
        Log.v("bradlog", "-------------");
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("display_name"));
            if (name != null) Log.v("bradlog", name);
        }
    }

    private Uri uriPhone  = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    public void fetchPhone(View view) {
        Cursor cursor = cr.query(uriPhone, null,null,null,null);
        String[] fields = cursor.getColumnNames();
        for (String field : fields){
            Log.v("bradlog", field);
        }
        Log.v("bradlog", "-------------");
        while (cursor.moveToNext()){
            String tel = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            if (name != null) Log.v("bradlog", name + ":" + tel);
        }
    }

    private Uri uriCalllog  = CallLog.Calls.CONTENT_URI;
    public void fetchCalllog(View view) {
        Cursor cursor = cr.query(uriCalllog, null,null,null,null);
//        String[] fields = cursor.getColumnNames();
//        for (String field : fields){
//            Log.v("bradlog", field);
//        }
//        Log.v("bradlog", "-------------");
        while (cursor.moveToNext()){
            int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
            String tel = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            switch (type){
                case CallLog.Calls.INCOMING_TYPE:
                    Log.v("bradlog", name + ":" + tel + ":" + "來電");
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    Log.v("bradlog", name + ":" + tel + ":" +  "去電");
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    Log.v("bradlog", name + ":" + tel + ":" + "未接");
                    break;
            }

        }
    }

}