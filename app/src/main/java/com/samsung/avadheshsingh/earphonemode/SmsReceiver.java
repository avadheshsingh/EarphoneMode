package com.samsung.avadheshsingh.earphonemode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Avadhesh Singh on 02-May-17.
 */



public class SmsReceiver extends BroadcastReceiver{
    String speak;
    String avi="Hi new Sms from  ",msz="",no;
    Cursor c;
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle=intent.getExtras();
        try {
            if(bundle!=null)
            {
                Object object[]=(Object[]) bundle.get("pdus");
                for (int i = 0; i < object.length; i++) {
                    SmsMessage message=SmsMessage.createFromPdu((byte[]) object[i]);
                    msz =message.getDisplayMessageBody().toString();
                    no =message.getOriginatingAddress();
                    if(no.length()>10)
                    {
                        int n=no.length()-10;
                        no = no.substring(++n);
                    }
                    String contact_name=contact(no,context);
                    Toast.makeText(context,contact_name+" : "+msz,Toast.LENGTH_LONG).show();
                    speak=avi+contact_name+" :  "+msz;
                    MyService.tts.speak(speak,TextToSpeech.QUEUE_FLUSH, null);
                }

            }
        } catch (Exception e) {}
    }

    public String contact(String no,Context context)
    {String value=no;
        Uri allcontacts=ContactsContract.Contacts.CONTENT_URI;
        String[] pro={ContactsContract.Contacts.DISPLAY_NAME,ContactsContract.Contacts._ID,ContactsContract.Contacts.HAS_PHONE_NUMBER};
        if(android.os.Build.VERSION.SDK_INT<11)
        {
            //c=managedQuery(allcontacts, pro, null, null, null);

        }
        else {
            CursorLoader cursorLoader=new CursorLoader(context, allcontacts, null, null, null, null);
            c=cursorLoader.loadInBackground();
        }
        String[] contacts=new String[]{ContactsContract.Contacts.DISPLAY_NAME,ContactsContract.Contacts._ID};

        if(c.moveToFirst())
        {
            do{
                String contactid= c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                String name =c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                int hasphone=c.getInt(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if(hasphone==1)
                {
                    Cursor phonecursor=context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = "+contactid,null,null);
                    while (phonecursor.moveToNext()) {
                        String temp=((phonecursor.getString(phonecursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))));
                        if(temp.length()>10)
                        {
                            int i=temp.length()-10;
                            temp = temp.substring(++i);
                        }
                        if(no.equals(temp))
                        {
                            value=name;
                        }
                        Log.v("content provider",phonecursor.getString(phonecursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    }
                    phonecursor.close();
                }
            }while(c.moveToNext());

        }
        return value;
    }


}
