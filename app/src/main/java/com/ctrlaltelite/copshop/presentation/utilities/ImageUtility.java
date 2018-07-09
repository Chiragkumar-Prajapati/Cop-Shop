package com.ctrlaltelite.copshop.presentation.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Date;

public class ImageUtility {

    public static String getPictureName() {
        return "copshopImage" + new Date().getTime() + ".jpg";
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int rotationAmount) {

        Matrix matrix = new Matrix();

        switch (rotationAmount){
            case 0:
                break;
            case 1:
                matrix.setRotate(90);
                break;
            case 2:
                matrix.setRotate(180);
                break;
            case 3:
                matrix.setRotate(270);
        }

        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static String[] imageDecode(String imageData) {
        return imageData.split(" ", 2);
    }

    public static Bitmap uriToBitmap(Context context, Uri selectedFileUri) {
        Bitmap image = null;
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    context.getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
