package com.samsung.avadheshsingh.earphonemode;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Switch speakingSms,CallerTalker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speakingSms=(Switch) findViewById(R.id.switchSms);
        CallerTalker=(Switch) findViewById(R.id.switchCall);

        speakingSms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Intent intent=new Intent(MainActivity.this,MyService.class);
               if(isChecked){
                   startService(intent);
                   Log.d(ShareData.TAG,"started");
               }
               else{
                   stopService(intent);
                   Log.d(ShareData.TAG,"stopped");
               }
            }
        });

        CallerTalker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){

                }
                else{

                }
            }
        });


    }
}
