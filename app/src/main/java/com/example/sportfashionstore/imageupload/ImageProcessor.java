package com.example.sportfashionstore.imageupload;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageProcessor {

    public static File processAndSaveImage(String originalPath, String outputDir) throws IOException {
        // Load bitmap
        Bitmap originalBitmap = BitmapFactory.decodeFile(originalPath);
        if (originalBitmap == null) {
            throw new IOException("Không thể đọc file ảnh");
        }

        // Scale to 16:9 ratio
        Bitmap scaledBitmap = scaleToRatio16_9(originalBitmap);

        // Fix rotation based on EXIF
        Bitmap rotatedBitmap = fixImageRotation(scaledBitmap, originalPath);

        // Generate filename with timestamp
        String fileName = generateFileName();

        // Save processed image
        File outputFile = new File(outputDir, fileName);
        FileOutputStream fos = new FileOutputStream(outputFile);
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
        fos.close();

        // Clean up bitmaps
        if (originalBitmap != scaledBitmap) originalBitmap.recycle();
        if (scaledBitmap != rotatedBitmap) scaledBitmap.recycle();
        rotatedBitmap.recycle();

        return outputFile;
    }

    private static Bitmap scaleToRatio16_9(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Calculate target dimensions for 16:9
        int targetWidth, targetHeight;

        if (width * 9 > height * 16) {
            // Image is wider than 16:9, crop width
            targetHeight = height;
            targetWidth = height * 16 / 9;
        } else {
            // Image is taller than 16:9, crop height
            targetWidth = width;
            targetHeight = width * 9 / 16;
        }

        // Calculate crop position (center crop)
        int x = (width - targetWidth) / 2;
        int y = (height - targetHeight) / 2;

        return Bitmap.createBitmap(bitmap, x, y, targetWidth, targetHeight);
    }

    private static Bitmap fixImageRotation(Bitmap bitmap, String imagePath) throws IOException {
        ExifInterface exif = new ExifInterface(imagePath);
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                break;
            default:
                return bitmap;
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }

    private static String generateFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        return "IMG_" + sdf.format(new Date()) + ".jpg";
    }
}
