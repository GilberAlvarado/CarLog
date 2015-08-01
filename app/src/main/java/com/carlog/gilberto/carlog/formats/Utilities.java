package com.carlog.gilberto.carlog.formats;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Base64;

import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.activity.Login;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;


public class Utilities {

    public static final int GALLERY_INTENT=415;
    public static final int CAMERA_INTENT=416;

    public static String ImageToBase64 (Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 70, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    public static Bitmap Base64ToImage (String previouslyEncodedImage){
        if( !previouslyEncodedImage.equalsIgnoreCase("") ){
            byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            return bitmap;
        }
        return null;
    }

    public static void selectImage (final Activity activity){
        final CharSequence[] items = { activity.getString(R.string.take_picture), activity.getString(R.string.gallery_picture), activity.getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.select_picture));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(activity.getString(R.string.take_picture))) {
                    selectImageCamera(activity);
                } else if (items[item].equals(activity.getString(R.string.gallery_picture))) {
                    selectImageGallery(activity);
                } else if (items[item].equals(activity.getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public static void selectImageCamera (Activity activity){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(android.os.Environment.getExternalStorageDirectory(), activity.getString(R.string.temp_picture));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        activity.startActivityForResult(intent, CAMERA_INTENT);
    }

    public static void selectImageGallery (Activity activity){
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activity.startActivityForResult(Intent.createChooser(intent, activity.getString(R.string.select_picture)), GALLERY_INTENT);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            activity.startActivityForResult(intent, GALLERY_INTENT);
        }
    }


    public static Bitmap getBitMapFromUri (Context ctx, Uri pictureUri) throws Exception{
        if (Build.VERSION.SDK_INT < 19) {
            String selectedImagePath = getPathPictureFromUri(ctx, pictureUri);
            return BitmapFactory.decodeFile(selectedImagePath);
        } else {
            ParcelFileDescriptor parcelFileDescriptor;
            try {
                parcelFileDescriptor = ctx.getContentResolver().openFileDescriptor(pictureUri, "r");
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                parcelFileDescriptor.close();

                return bitmap;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw e;
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    public static String getPathPictureFromUri (Context ctx, Uri uri) {
        if( uri == null ) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = ctx.getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }


    public static String getRealPathFromURI(Context ctx, Uri contentURI) {
        Cursor cursor = ctx.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }



}
