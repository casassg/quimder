package tindre.love.quim.quimtindre.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import tindre.love.quim.quimtindre.model.Felicitacio;

/**
 * Created by casassg on 12/06/16.
 *
 * @author casassg
 */
public class ImatgesUtils {

    public static final String TAG = ImatgesUtils.class.getSimpleName();
    public interface SetDrawableCallback{
        public void giveMeMyDrawable(Bitmap bitmap);

    }

    public static void getImage(final Felicitacio felicitacio, final SetDrawableCallback callback) throws IOException {
        if (hasImage(felicitacio)){
            Log.d(TAG, "YAY THERES AN IMAGE");
            Bitmap bm = getBitmap(felicitacio);
            callback.giveMeMyDrawable(bm);
        } else {
            Log.d(TAG,"No image yet...");
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child("felicitacions/2016-03-20 04.20.34 1.jpg");
            String absolutePathImage = getAbsolutePathImage(felicitacio);
            Log.d(TAG,"No image yet..."+absolutePathImage);
            File localFile = new File(absolutePathImage);
            localFile.createNewFile();
            Log.d(TAG,"No image yet..."+localFile.getAbsolutePath());
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bm = getBitmap(felicitacio);
                    callback.giveMeMyDrawable(bm);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {

                }
            });
        }
    }

    private static void saveToLocal(File localFile, Felicitacio felicitacio) {
        File to = new File(getAbsolutePathImage(felicitacio));
        Log.d(TAG, "Renaming to: "+to.getAbsolutePath());
        if (!localFile.renameTo(to)) {
            Log.e(TAG, "Could not rename file");
        }
    }

    public static String getAbsolutePathImage(Felicitacio recepta) {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir,recepta.path);
        return image.getAbsolutePath();
    }

//    public static void setImage(Felicitacio recepta, ImageView imageView, boolean needsToBeTinted, boolean small) {
//        String path = getAbsolutePathImage(recepta);
//        setImage(imageView, needsToBeTinted, path, small);
//
//    }

//    public static void setImage(ImageView imageView, boolean needsToBeTinted, String path, boolean small) {
//        if (hasImage(path)) {
//            Bitmap bm = getBitmap(path, small);
//            imageView.setImageBitmap(bm);
//            if (needsToBeTinted)
//                imageView.setColorFilter(R.color.primary_material_dark, PorterDuff.Mode.OVERLAY);
//
//        } else {
////            imageView.setImageResource(R.drawable.dummy);
//        }
//    }

    private static Bitmap getBitmap(Felicitacio felicitacio) {
        BitmapFactory.Options ops = new BitmapFactory.Options();
        ops.inSampleSize = 1;
        return BitmapFactory.decodeFile(getAbsolutePathImage(felicitacio), ops);
    }

    public static boolean hasImage(String path) {
        File image = new File(path);
        return image.exists();
    }


//    public static Bitmap getBitmap(String path) {
//
//        InputStream in = null;
//        try {
//            final int IMAGE_MAX_SIZE = 120000; // 1.2MP
//            in = new FileInputStream(new File(path));
//
//            // Decode image size
//            BitmapFactory.Options o = new BitmapFactory.Options();
//            o.inJustDecodeBounds = true;
//            BitmapFactory.decodeStream(in, null, o);
//            in.close();
//
//
//            int scale = 1;
//            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
//                    IMAGE_MAX_SIZE) {
//                scale++;
//            }
//
//            Bitmap b = null;
//            in = new FileInputStream(new File(path));
//            if (scale > 1) {
//                scale--;
//                // scale to max possible inSampleSize that still yields an image
//                // larger than target
//                o = new BitmapFactory.Options();
//                o.inSampleSize = scale;
//                b = BitmapFactory.decodeStream(in, null, o);
//
//                // resize to desired dimensions
//                int height = b.getHeight();
//                int width = b.getWidth();
//
//                double y = Math.sqrt(IMAGE_MAX_SIZE
//                        / (((double) width) / height));
//                double x = (y / height) * width;
//
//                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
//                        (int) y, true);
//                b.recycle();
//                b = scaledBitmap;
//
//                System.gc();
//            } else {
//                b = BitmapFactory.decodeStream(in);
//            }
//            in.close();
//            return b;
//        } catch (IOException e) {
//            Log.e(TAG, e.getMessage(), e);
//            return null;
//        }
//    }
//
//    public static String saveImageRecepta(Felicitacio felicitacio, String photoPath) {
//        if (photoPath != null) {
//            File f = new File(photoPath);
//            if (f.exists()) {
//                String name = felicitacio.path;
//                return renameFile(name, photoPath);
//            }
//        }
//        return "";
////    }
//
//    public static String renameFile(String name, String path) {
//        File from = new File(path);
//        File directory = from.getParentFile();
//        File to = new File(directory, name);
//        if (!from.renameTo(to)) {
//            Log.e(TAG, "Could not rename file");
//        }
//        return to.getAbsolutePath();
//    }

    public static boolean hasImage(Felicitacio mItem) {
        String path = getAbsolutePathImage(mItem);
        return hasImage(path);
    }
}
