package in.co.chicmic.canvas.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static File createNewFile(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date());
        //folder stuff
        File imagesFolder =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        // Uri uriSavedImage = Uri.fromFile(image);
        return new File(imagesFolder, "canvas_" + timeStamp + ".png");
    }

    public static void addImageToGallery(final String filePath, final Context context) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);
        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    public static Bitmap getBitmapFromPath(String filePath){
        File image = new File(filePath);
        return BitmapFactory.decodeFile(image.getAbsolutePath());
    }
}
