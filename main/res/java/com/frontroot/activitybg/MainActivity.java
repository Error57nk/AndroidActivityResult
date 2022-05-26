package com.frontroot.activitybg;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView showText1, showText2;
    EditText inputText;
    String ipVal;
    Button button;
    Boolean btnKey = false;
//    private final ActivityResultRegistry mRegistry;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showText1 = findViewById(R.id.textViewShow1);
        showText2 = findViewById(R.id.textViewShow2);
        button = findViewById(R.id.button);

        inputText = findViewById(R.id.editText);
        Intent intent = new Intent(this, ActitityWithResult.class);


        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent data = result.getData();
                        String rcvSt = data.getStringExtra("passVal");
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            showText2.setText(rcvSt);
                        }
                    }
                });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (btnKey) {
                    btnKey = false;
                    showText1.setText("Previous Val: \n" + ipVal);
                    button.setText("Set");
                    intent.putExtra("passVal", ipVal);
//                    startActivityForResult(intent,123);
                    launcher.launch(intent);

                } else {
                    ipVal = inputText.getText().toString();
                    if (ipVal != "" && ipVal.length() >= 3) {
                        btnKey = true;
                        inputText.setText("");
                        inputText.setHint("Enter Val");
                        showText1.setText("Val Set for: \n" + ipVal);
                        button.setText("Launch");
                    } else if (ipVal.length() < 3 && ipVal.length() > 0) {
                        inputText.setText("");
                        inputText.setHint("less than 3");
                    } else {
                        inputText.setText("");
                        inputText.setHint("Empty");
                    }
                }
            }
        });


    }
}