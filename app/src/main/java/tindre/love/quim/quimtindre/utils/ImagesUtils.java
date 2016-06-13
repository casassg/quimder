package tindre.love.quim.quimtindre.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import tindre.love.quim.quimtindre.R;
import tindre.love.quim.quimtindre.model.GreetingCard;

public class ImagesUtils {

    public static final String TAG = ImagesUtils.class.getSimpleName();

    public interface SetDrawableCallback {
        void giveMeMyDrawable(Bitmap bitmap);
    }

    public static void getImage(final GreetingCard greetingCard, final SetDrawableCallback callback) throws IOException {
        if (hasImage(greetingCard)) {
            Log.d(TAG, "YAY THERE'S AN IMAGE");
            Bitmap bm = getBitmap(greetingCard);
            callback.giveMeMyDrawable(bm);
        }
        else {
            Log.d(TAG, "No image yet...");

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            String absolutePathImage = getAbsolutePathImage(greetingCard);

            Log.d(TAG, "No image yet..." + absolutePathImage);
            File localFile = new File(absolutePathImage);

            Log.d(TAG, "No image yet..." + localFile.getAbsolutePath());
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bm = getBitmap(greetingCard);
                    callback.giveMeMyDrawable(bm);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    private static void saveToLocal(File localFile, GreetingCard card) {
        File to = new File(getAbsolutePathImage(card));
        Log.d(TAG, "Renaming to: " + to.getAbsolutePath());
        if (!localFile.renameTo(to)) {
            Log.e(TAG, "Could not rename file");
        }
    }

    public static String getAbsolutePathImage(GreetingCard card) {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir, card.getAuthor() + ".jpg");
        return image.getAbsolutePath();
    }

    public static void setImage(GreetingCard card, ImageView imageView, boolean needsToBeTinted, boolean small) {
        setImage(imageView, needsToBeTinted, card, small);
    }

    public static void setImage(ImageView imageView, boolean needsToBeTinted, GreetingCard card, boolean small) {
        String path = getAbsolutePathImage(card);
        if (hasImage(path)) {
            Bitmap bm = getBitmap(card);
            imageView.setImageBitmap(bm);
            if (needsToBeTinted)
                imageView.setColorFilter(R.color.our_primary_material_dark, PorterDuff.Mode.OVERLAY);

        } else {
            imageView.setImageResource(R.mipmap.quimder_logo_4);
        }
    }

    private static Bitmap getBitmap(GreetingCard card) {
        BitmapFactory.Options ops = new BitmapFactory.Options();
        ops.inSampleSize = 1;
        return BitmapFactory.decodeFile(getAbsolutePathImage(card), ops);
    }

    public static boolean hasImage(String path) {
        File image = new File(path);
        return image.exists();
    }

    public static boolean hasImage(GreetingCard mItem) {
        String path = getAbsolutePathImage(mItem);
        return hasImage(path);
    }

    public static Bitmap getBitmap(String path) {
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

    public static String saveImageCard(GreetingCard greetingCard, String photoPath) {
        if (photoPath != null) {
            File f = new File(photoPath);
            if (f.exists()) {
                String name = greetingCard.getAuthor() + ".jpg";
                return renameFile(name, photoPath);
            }
        }
        return "";
    }

    public static String renameFile(String name, String path) {
        File from = new File(path);
        File directory = from.getParentFile();
        File to = new File(directory, name);
        if (!from.renameTo(to)) {
            Log.e(TAG, "Could not rename file");
        }
        return to.getAbsolutePath();
    }
}
