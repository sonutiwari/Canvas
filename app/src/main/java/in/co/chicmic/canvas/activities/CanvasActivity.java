package in.co.chicmic.canvas.activities;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import in.co.chicmic.canvas.R;
import in.co.chicmic.canvas.customViews.CanvasView;
import in.co.chicmic.canvas.utilities.Constants;
import in.co.chicmic.canvas.utilities.Utils;

public class CanvasActivity extends AppCompatActivity implements View.OnClickListener{

    private CanvasView mCanvasView;
    private Button mSaveButton;
    private Button mAddButton;
    private Button mEraseButton;
    private Button mNewButton;
    private LinearLayout mParentLL;
    private PermissionListener dialogPermissionListener;
    private boolean isEraseModeEnabled = true;

    String mPath;
    private boolean isAddOptionEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);
        initViews();
        setListeners();
    }

    private void initViews() {
        mSaveButton  = findViewById(R.id.btn_save);
        mAddButton  = findViewById(R.id.btn_add);
        mEraseButton  = findViewById(R.id.btn_eraser);
        mNewButton  = findViewById(R.id.btn_new);
        mParentLL = findViewById(R.id.canvas_view);
        dialogPermissionListener =
        DialogOnDeniedPermissionListener.Builder
                .withContext(this)
                .withTitle(R.string.permission_header)
                .withMessage(R.string.permission_message)
                .withButtonText(R.string.string_ok)
                .build();
    }

    private void setListeners(){
        mSaveButton.setOnClickListener(this);
        mAddButton.setOnClickListener(this);
        mEraseButton.setOnClickListener(this);
        mNewButton.setOnClickListener(this);
    }

    private void addCanvasView() {
        mCanvasView = new CanvasView(this);
        mParentLL.addView(mCanvasView);
        if (getIntent().getExtras() != null){
            mPath = getIntent().getExtras().getString(Constants.sIMAGE_PATH);
            Bitmap bitmap = Utils.getBitmapFromPath(mPath);
            if (bitmap != null) {
                mCanvasView.setBitmap(bitmap);
                isAddOptionEnabled = true;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        addCanvasView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add:
                showAllCanvasImages();
                break;
            case R.id.btn_eraser:
                if (mCanvasView != null){
                    mCanvasView.setEraseModeSwitch();
                    isEraseModeEnabled = !isEraseModeEnabled;
                }
                if (isEraseModeEnabled){
                    mEraseButton.setText(R.string.erase);
                } else {
                    mEraseButton.setText(R.string.draw);
                }
                break;
            case R.id.btn_new:
                isAddOptionEnabled = false;
                setNewCanvas();
                break;
            case R.id.btn_save:
                Dexter.withActivity(this)
                        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(dialogPermissionListener)
                        .check();
                String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
                int res = this.checkCallingOrSelfPermission(permission);
                if (res == PackageManager.PERMISSION_GRANTED) {
                    saveToGallery();
                } else {
                    Dexter.withActivity(this)
                            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .withListener(dialogPermissionListener)
                            .check();
                }
                break;
        }
    }

    private void showAllCanvasImages() {
        String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res = this.checkCallingOrSelfPermission(permission);
        if (res == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(this, GalleryActivity.class));
            finish();
        } else {
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(dialogPermissionListener)
                    .check();
        }
    }

    private void setNewCanvas() {
        mCanvasView = new CanvasView(this);
        mParentLL.removeAllViews();
        mParentLL.addView(mCanvasView);
    }


    private void saveToGallery() {
        Bitmap mImage = mCanvasView.getBitmap();
        if (isAddOptionEnabled){
            File file = new File(mPath);
            try(FileOutputStream outputStream = new FileOutputStream(file)){
                mImage.compress(Bitmap.CompressFormat.PNG, Constants.sQUALITY, outputStream);
            } catch (IOException e){
                e.printStackTrace();
            }
        } else {
            saveToAppStorage(mImage);
            try (FileOutputStream out = new FileOutputStream(Utils.createNewFile())) {
                mImage.compress(Bitmap.CompressFormat.PNG, 100, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setNewCanvas();
        Snackbar.make(mParentLL, R.string.successfully_saved, Snackbar.LENGTH_SHORT).show();
    }

    private void saveToAppStorage(Bitmap image) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir(Constants.sPROFILE, Context.MODE_PRIVATE);
        if (!directory.exists()) {
            directory.mkdir();
        }
        String timeStamp = new SimpleDateFormat(Constants.sDATE_FORMAT, Locale.getDefault())
                .format(new Date());
        File mypath = new File(directory, Constants.sFILE_PREFIX + timeStamp + Constants.sFILE_EXTENSION);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(mypath);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            Log.e("SAVE_IMAGE", e.getMessage(), e);
        }
    }
}
