package tindre.love.quim.quimtindre;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.io.IOException;

import tindre.love.quim.quimtindre.model.Felicitacio;
import tindre.love.quim.quimtindre.utils.FirebaseAdapter;
import tindre.love.quim.quimtindre.utils.ImatgesUtils;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL = 32;
    private DatabaseReference mDatabase;
    private FirebaseAdapter<Felicitacio> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (checkWritePermission(this)){
            init();
        }


    }

    private void init() {
        //add the view via xml or programmatically
        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("felicitacions");
        mDatabase.push().setValue(new Felicitacio("TEST"));


        //choose your favorite adapter
        adapter = new FirebaseAdapter<Felicitacio>(mDatabase,Felicitacio.class,R.layout.tinder,this) {
            @Override
            protected void populateView(View v, Felicitacio model) {
                TextView descripcioView = (TextView) v.findViewById(R.id.descripcio);
                final ImageView imageView = (ImageView) v.findViewById(R.id.image);
                descripcioView.setText(model.text);
                model.path = model.text+".jpg";
                try {
                    ImatgesUtils.getImage(model, new ImatgesUtils.SetDrawableCallback() {
                        @Override
                        public void giveMeMyDrawable(Bitmap bitmap) {
                            imageView.setImageBitmap(bitmap);
                            adapter.notifyDataSetChanged();

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };


        flingContainer.setAdapter(adapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                adapter.removeFirst();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(getApplicationContext(), "Left!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(getApplicationContext(), "Right!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                adapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
            }

            @Override
            public void onScroll(float v) {

            }
        });
    }

    public static Boolean checkWritePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_WRITE_EXTERNAL
            );
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_WRITE_EXTERNAL &&
                grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            init();
        }
    }

}
