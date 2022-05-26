package com.frontroot.activitybg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ActitityWithResult extends AppCompatActivity {
    Button backBtn;
    EditText ipText;
    TextView textRec;
    String stp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actitity_with_result);

        backBtn = findViewById(R.id.btnBack);
        ipText = findViewById(R.id.editTextAc);
        textRec = findViewById(R.id.textRec);

        Intent intent = getIntent();
        String intent_rcv_txt = intent.getStringExtra("passVal");

        textRec.setText(intent_rcv_txt);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stp = ipText.getText().toString();
                if(stp.equals("")){
                    backBtn.setText("Enter Value in Field");
                }else if(stp.length()<3){
                    backBtn.setText("Enter More than 3");
                }else{
                    Intent data = new Intent();
                    data.putExtra("passVal", stp);
                    setResult(RESULT_OK, data);
                    finish();
//                    finishActivity(123);
                }
            }
        });
    }
}