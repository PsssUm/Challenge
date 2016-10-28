package com.evgenyvyaz.audiospectr;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import java.io.File;

public class FilePickerActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST = 1;
    private Button pickFileBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker);
        pickFileBTN = (Button) findViewById(R.id.pickFileBTN);
        pickFileBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPicker();
            }
        });
        askForPhoneStatePermission();
    }

    private void showDialogPicker() {
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath());
        properties.extensions = null;
        FilePickerDialog dialog = new FilePickerDialog(FilePickerActivity.this, properties);

        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                //files is the array of the paths of files selected by the Application User.
                if (files.length > 0) {
                    Intent intent = new Intent(FilePickerActivity.this, MainActivity.class);
                    intent.putExtra(MainActivity.PATH, files[0]);
                    startActivity(intent);

                } else {
                    showDialogPicker();
                }

            }
        });
        dialog.show();
    }

    public void askForPhoneStatePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED

                    ) {

                // Should we show an explanation?
                if (
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)

                        ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("write external storage access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm write external storage");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}
                                    , PERMISSION_REQUEST);
                        }
                    });
                    builder.show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                            PERMISSION_REQUEST);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {

                showDialogPicker();
                //    startApp();
                //  getContact();
            }
        } else {
            showDialogPicker();
            // startApp();
            //  getContact();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST) {

            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                showDialogPicker();
                //   startApp();
                // permission was granted, yay! Do the
                // contacts-related task you need to do.

            } else {
                Toast.makeText(this, "No permission for write external storage", Toast.LENGTH_LONG).show();// ToastMaster.showMessage(MainActivity.this,"No permission for contacts");
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
            return;


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
