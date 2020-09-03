package co.domi.camaraygaleria;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ModalDialog.OnOkListener{

    private Button openCameraBtn;
    private Button openGalleyBtn;
    private Button downloadImage;

    private ImageView mainImage;
    public static final int PERMISSIONS_CALLBACK = 11;
    public static final int CAMERA_CALLBACK = 12;
    public static final int GALLERY_CALLBACK = 13;

    private File file;
    private ModalDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openCameraBtn = findViewById(R.id.openCameraBtn);
        openGalleyBtn = findViewById(R.id.openGalleyBtn);
        downloadImage = findViewById(R.id.downloadImage);
        mainImage = findViewById(R.id.mainImage);


        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
        }, PERMISSIONS_CALLBACK);


        openCameraBtn.setOnClickListener(this);
        openGalleyBtn.setOnClickListener(this);
        downloadImage.setOnClickListener(this);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSIONS_CALLBACK){
            boolean allGrant = true;
            for(int i=0 ; i<grantResults.length; i++){
                if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                    allGrant=false;
                    break;
                }
            }
            if(allGrant){
                Toast.makeText(this, "Todos los permisos concedidos", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Alerta!, no todos los permisos fueron concedidos", Toast.LENGTH_LONG).show();
            }

        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.openCameraBtn:
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file = new File( getExternalFilesDir(null) + "/photo.png" );
                Log.e(">>>>",""+file);
                Uri uri = FileProvider.getUriForFile(this, getPackageName(), file);
                i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(i, CAMERA_CALLBACK);
                break;

            case R.id.openGalleyBtn:
                Intent j = new Intent(Intent.ACTION_GET_CONTENT);
                j.setType("image/*");
                startActivityForResult(j, GALLERY_CALLBACK);
                break;

            case R.id.downloadImage:
                //Abrir modal
                dialog = ModalDialog.newInstance();
                dialog.setListener(this);
                dialog.show(getSupportFragmentManager(), "urlDialog");
                break;

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_CALLBACK && resultCode == RESULT_OK){
            Bitmap image = BitmapFactory.decodeFile(file.getPath());
            Bitmap thumbnail = Bitmap.createScaledBitmap(
                    image, image.getWidth()/14, image.getHeight()/14, true
            );
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap rotatedBitmap = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), matrix, true);
            mainImage.setImageBitmap(rotatedBitmap);
        }
        else if(requestCode == GALLERY_CALLBACK && resultCode == RESULT_OK){
            Uri uri = data.getData();
            Log.e(">>>",uri+"");
            String path = UtilDomi.getPath(this, uri);
            Log.e(">>>",path+"");
            Bitmap image = BitmapFactory.decodeFile(path);
            mainImage.setImageBitmap(image);
        }
    }

    @Override
    public void onOk(String url) {
        dialog.dismiss();
        Glide.with(this).load(url).fitCenter().into(mainImage);
    }
}