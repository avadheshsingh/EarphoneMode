package com.samsung.avadheshsingh.earphonemode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Avadhesh Singh on 04-May-17.
 */

public class PhoneStateReceiver extends BroadcastReceiver {
    Cursor c;

    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
       String contactName= contact(incomingNumber,context);

        if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
            if(contactName.length()>1){
                Toast.makeText(context,contactName+" is calling Number is -"+incomingNumber,Toast.LENGTH_SHORT).show();
                MyService.tts.speak(contactName+" is calling", TextToSpeech.QUEUE_FLUSH,null);
            }else{
                Toast.makeText(context,"Ringing State Number is -"+incomingNumber,Toast.LENGTH_SHORT).show();
                MyService.tts.speak(incomingNumber+" is calling", TextToSpeech.QUEUE_FLUSH,null);
            }



        }
        if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))){
            Toast.makeText(context,"Received State",Toast.LENGTH_SHORT).show();
        }
        if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
            Toast.makeText(context,"Idle State",Toast.LENGTH_SHORT).show();
        }

    }

    public String contact(String no,Context context)
    {String value=no;
        Uri allcontacts= ContactsContract.Contacts.CONTENT_URI;
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
