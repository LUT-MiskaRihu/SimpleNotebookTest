package com.simplenotebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    Context context = null;
    EditText etFilename;
    EditText etEditor;
    Button btnLoad;
    Button btnSave;

    String path = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        etFilename = findViewById(R.id.etFilename);
        etEditor = findViewById(R.id.etEditor);
        btnLoad = findViewById(R.id.btnLoad);
        btnSave = findViewById(R.id.btnSave);
    }

    public void loadFile(View view) {
        //if (path != null) {
            String sFilename = etFilename.getText().toString();
            etFilename.setText(sFilename);

            if (sFilename.isEmpty()) {
                Toast.makeText(MainActivity.this, "Enter a filename", Toast.LENGTH_SHORT).show();
            } else {
                etEditor.setText("");
                try {
                    FileInputStream fileInputStream = context.openFileInput(sFilename);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                    String sLine;
                    while ((sLine = bufferedReader.readLine()) != null) {
                        etEditor.append(sLine + "\n");
                    }
                    bufferedReader.close();
                    fileInputStream.close();
                    Toast.makeText(MainActivity.this, "File loaded successfully", Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "File not found", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Loading failed: " + e, Toast.LENGTH_LONG).show();
                }
            }
        /*} else {
            Toast.makeText(context,"No file selected.", Toast.LENGTH_SHORT);
        }*/
    }

    public void saveFile(View view) {
        String sFilename = etFilename.getText().toString();
        String sContent = etEditor.getText().toString();

        if (sFilename.isEmpty()) {
            Toast.makeText(MainActivity.this, "Enter a filename", Toast.LENGTH_SHORT).show();
        } else {
            if (sContent.isEmpty()) {
                Toast.makeText(MainActivity.this, "There's nothing to save!", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    FileOutputStream fileOutputStream = context.openFileOutput(sFilename, Context.MODE_PRIVATE);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                    outputStreamWriter.write(sContent);
                    System.out.println(sContent);
                    outputStreamWriter.close();
                    Toast.makeText(MainActivity.this, "File saved successfully", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Saving failed: " + e, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    int requestCode = 1;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == requestCode && resultCode == Activity.RESULT_OK) {
            if(data == null) {
                return;
            }
            Uri uri = data.getData();
            path = uri.getPath();
            Toast.makeText(context,path,Toast.LENGTH_LONG).show();
        }
    }

    public void openFileChooser(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, requestCode);
        //loadFile(View view);
    }
}