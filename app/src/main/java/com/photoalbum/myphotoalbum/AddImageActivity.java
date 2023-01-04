package com.photoalbum.myphotoalbum;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddImageActivity extends AppCompatActivity {
    private  ImageView imageViewAddImage;
    private EditText editTextTitle , editTextDescription;
    private Button buttonSave;
    ActivityResultLauncher<Intent> activityResultLauncherForSelectImage;
private Bitmap selectedImage;
private Bitmap ScaledImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Add Image");
        setContentView(R.layout.activity_add_image);

        //register activity
        registerActivityForSelectImage();

      editTextDescription= findViewById(R.id.editTextDescription);
   editTextTitle= findViewById(R.id.editTextTitle);
       buttonSave= findViewById(R.id.save);
        imageViewAddImage=findViewById(R.id.imageViewselect);



        imageViewAddImage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

             if(ContextCompat.checkSelfPermission(AddImageActivity.this
                     , Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
             {
                 ActivityCompat.requestPermissions(AddImageActivity.this
                         ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
             }
             else {
                 Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                 activityResultLauncherForSelectImage.launch(intent);

             }
           }

       });
       buttonSave.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (selectedImage == null){
                   Toast.makeText(AddImageActivity.this,"Please Select an Image",Toast.LENGTH_SHORT).show();
               }else {

               String title = editTextTitle.getText().toString();
               String description = editTextDescription.getText().toString();
               ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
              ScaledImage = maxSmall(selectedImage,300);
               ScaledImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);
               byte[] image =outputStream.toByteArray();
               Intent intent = new Intent();
                    intent.putExtra("title",title);
                    intent.putExtra("description",description);
                    intent.putExtra("image",image);
                   setResult(RESULT_OK,intent );
                   finish();



               }

           }
       });
    }

    public void registerActivityForSelectImage() {
        activityResultLauncherForSelectImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        Intent data = result.getData();
                        if (resultCode == RESULT_OK && data !=null){
                            try {
                                selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                                imageViewAddImage.setImageBitmap(selectedImage);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults [0] == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncherForSelectImage.launch(intent);
        }
        }
public  Bitmap maxSmall(Bitmap image , int maxSize){
    int width = image.getWidth();
    int height = image.getHeight();
    float ration = (float)  width/ (float) height;
    if (ration >1){
        width = maxSize;
        height= (int )(width/ration);
    }else
    {height= maxSize;
    width = (int) (height*ration);

    }return Bitmap.createScaledBitmap(image,width,height,true);
}
}