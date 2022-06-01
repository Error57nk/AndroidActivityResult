package com.frontroot.savetextlocal;

import static com.frontroot.savetextlocal.R.color.red;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Button saveBtn,loadBtn;
    private TextView dataBox;
    private EditText dataKey,dataValue;
    private int RQST_CODE = 101;

    String textData ="",textKey ="";

    ActivityResultLauncher<String> mGetMyContent;
    ActivityResultLauncher<Intent> mGetPermission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveBtn = findViewById(R.id.buttonSave);
        loadBtn = findViewById(R.id.buttonLoad);
        dataBox = findViewById(R.id.textViewShow);
        dataKey = findViewById(R.id.editTextKey);
        dataValue = findViewById(R.id.editTextValue);
        TextMethod textMethod = new TextMethod();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textKey = dataKey.getText().toString();
                textData = dataValue.getText().toString();
                boolean saveKey = true;
                int red = getResources().getColor(R.color.red);
                if(textKey.equals("") || textKey.trim().length() == 0){
                    dataKey.setBackgroundColor(getColor(R.color.red));
                    saveKey = false;
                }else{
                    dataKey.setBackgroundColor(getColor(R.color.white));
                    dataKey.setTextColor(getColor(R.color.black));
                }
                if(textData.equals("") || textData.trim().length() == 0){
                    dataValue.setBackgroundColor(getColor(R.color.red));
                    dataValue.setTextColor(getColor(R.color.black));
                    saveKey = false;
                }else{
                    dataValue.setBackgroundColor(getColor(R.color.white));
                }

                if(saveKey){
                    Toast.makeText(MainActivity.this,"Validated Successfuly",Toast.LENGTH_SHORT).show();
                    String data = String.format("{key: %s, value: %s }, \n",textKey,textData);
                    textMethod.save(MainActivity.this, data);
                    try {
                        textMethod.saveText(MainActivity.this,data);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(MainActivity.this,"Validated Fail",Toast.LENGTH_SHORT).show();

                }

            }
        });
        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"This Function Not Implemented",Toast.LENGTH_SHORT).show();
                selectFile(".txt");
            }
        });



        mGetMyContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                //We will get content Uri
                try{
                    Log.d("Result", result.toString());
//                    saveToPhoneStore(result);
//                    saveTextToPhoneStore(cotent* ,meme_type* ,path)
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        mGetPermission = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == MainActivity.RESULT_OK){
                    Toast.makeText(getApplicationContext(),"Permission Granted in 11",Toast.LENGTH_SHORT);

                }else{
                    Toast.makeText(getApplicationContext(),"Permission Not Granted in 11",Toast.LENGTH_SHORT);
                }
            }
        });

    }

    private void selectFile(){
        checkPermission();
        mGetMyContent.launch("image/*");
//        mGetMyContent.launch("text/*");
    }
    private void selectFile(String fileType){
        String MEME_TYPE = "text/*";
        if(fileType.equals(".txt") ||fileType.equals("text") ){
            MEME_TYPE = "text/*";
        }else if(fileType.equals(".jpg") || fileType.equals("img")){
            MEME_TYPE = "image/*";
        }
        mGetMyContent.launch(MEME_TYPE);
    }
    private void saveToPhoneStore(Uri uri) throws IOException {
        OutputStream fos;
        ContentResolver resolver =  getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,System.currentTimeMillis() + ".jpeg");
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_PICTURES + File.separator + "NMLKit");

        Uri imgUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        fos = resolver.openOutputStream(Objects.requireNonNull(imgUri));

//            fos.write();
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
        Objects.requireNonNull(fos);


    }



    // Required Permission

    private void takePermissions() {
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.R){
            try{
                Intent intent= new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%",getApplicationContext().getPackageName())));
                mGetPermission.launch(intent);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            //If api < 30
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},RQST_CODE);
        }
    }//End isPermissionGiven

    private boolean isPermissionGranted(){
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.R){
            return Environment.isExternalStorageManager();
        }else {
            //If api < 30
            int readExtStorePer = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return readExtStorePer == PackageManager.PERMISSION_GRANTED;
        }
    }

    public  void checkPermission(){
        if(isPermissionGranted()){
            Log.d("Error57nk","Permission Already Granted");
        }else{
            Log.d("Error57nk","Taking Required Permission");
            takePermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0){
            if(requestCode== RQST_CODE){
                boolean readExtStore= grantResults[0]== PackageManager.PERMISSION_GRANTED;
                if(readExtStore){
                    Toast.makeText(getApplicationContext(),"Permission Granted for < 10",Toast.LENGTH_SHORT);
                }else{
                    Toast.makeText(getApplicationContext(),"Permission Not Given",Toast.LENGTH_SHORT);
                    takePermissions();
                }
            }
        }
    }
}