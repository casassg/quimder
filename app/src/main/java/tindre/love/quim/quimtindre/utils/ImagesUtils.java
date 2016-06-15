package tindre.love.quim.quimtindre.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import tindre.love.quim.quimtindre.model.GreetingCard;

public class ImagesUtils {

    public static final String TAG = ImagesUtils.class.getSimpleName();
    private final static String GREETING_CARDS = "greetingCards";

    public interface SetDrawableCallback {
        void giveMeMyDrawable(Bitmap bitmap);
    }

    public static void getImage(final GreetingCard greetingCard, final SetDrawableCallback callback, final Context c) throws IOException {
        String key = greetingCard.getAuthor();
        getImage(callback, c, key);
    }

    public static void getImage(final SetDrawableCallback callback, Context c, String key) {
        File file = new File(c.getFilesDir(), key +".jpg");
        String imageFile = file.getAbsolutePath();
        if (hasImage(imageFile)) {
            Bitmap bm = getBitmap(imageFile);
            if (bm==null) {
                downloadImage(callback, key, imageFile);
            }else {
                callback.giveMeMyDrawable(bm);
            }
        }
        else {

            downloadImage(callback, key, imageFile);
        }
    }

    private static void downloadImage(final SetDrawableCallback callback, String key, String imageFile) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child(GREETING_CARDS).child(key + ".jpg");

        final File localFile = new File(imageFile);

        Log.i(TAG, "Downloading to " + localFile.getAbsolutePath());
        storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                if (hasImage(localFile.getAbsolutePath())) {
                    Bitmap bm = getBitmap(localFile.getAbsolutePath());
                    callback.giveMeMyDrawable(bm);
                } else {
                    Log.e(TAG, "WUUUT? THE IMAGE JUST DISAPPERARED");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Loading image failed", e);
            }
        });
    }


    private static boolean hasImage(String path) {
        File image = new File(path);
        return image.exists();
    }

    private static Bitmap getBitmap(String path) {
        InputStream in;
        try {
            final int IMAGE_MAX_SIZE = 120000; // 1.2MP
            in = new FileInputStream(new File(path));

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();

            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }

            Bitmap b;
            in = new FileInputStream(new File(path));
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);
                if (b==null){
                    return null;
                }

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            }
            else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();
            return b;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

}
