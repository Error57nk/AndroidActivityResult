package com.frontroot.savetextlocal;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class TextMethod {

    public boolean save(Context context, String data) {
        File file = new File(Environment.getExternalStorageDirectory() + "/" + File.separator + "test.txt");
        if (!file.exists()) {
            if (!file.mkdir()) {
                Toast.makeText(context, "Fail to create dir", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        try {
//            File gpxfile = new File(file, "sample");
            FileWriter writer = new FileWriter(file);
            writer.append(data);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved Success", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Saved Fail", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public boolean saveText(Context context, String content) throws FileNotFoundException {
        String MEME_TYPE_TEXT = "text/plain", MEME_TYPE_IMAGE = "image/jpeg";
        String FILE_NAME = "";
        OutputStream fos = null;
        ContentResolver resolver = context.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis() + ".txt");
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, MEME_TYPE_TEXT);
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + File.separator + "NMLKit");
        try {
            Uri textUri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues);
            fos = resolver.openOutputStream(Objects.requireNonNull(textUri));
            fos.write(content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
