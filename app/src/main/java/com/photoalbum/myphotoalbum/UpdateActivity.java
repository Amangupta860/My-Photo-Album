package com.photoalbum.myphotoalbum;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UpdateActivity extends AppCompatActivity {
   private ImageView imageUpdateSelect;
    private EditText editTextUpdateTitle , editTextUpdateDescription;
    private Button buttonUpdateSave;
    private String title,description;
    private int id;
    private byte[] image;
    ActivityResultLauncher<Intent> activityResultLauncherForSelectImage;
    private Bitmap selectedImage;
    private Bitmap ScaledImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle("Update Image");
        setContentView(R.layout.activity_update);

        registerActivityForSelectImage();

        editTextUpdateDescription= findViewById(R.id.editTextUpdateDescription);
        editTextUpdateTitle= findViewById(R.id.editTextUpdateTitle);
        buttonUpdateSave= findViewById(R.id.UpdateSave);
        imageUpdateSelect=findViewById(R.id.imageViewUpdateSelect);



        id = getIntent().getIntExtra("id",-1);
        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
      image = getIntent().getByteArrayExtra("image");
      editTextUpdateDescription.setText(description);
      editTextUpdateTitle.setText(title);
      imageUpdateSelect.setImageBitmap(BitmapFactory.decodeByteArray(image,0,image.length));


        imageUpdateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncherForSelectImage.launch(intent);




            }
        });
        buttonUpdateSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();

            }
        });
    }
    public void updateData(){
        if (id == -1 ){
            Toast.makeText(UpdateActivity.this,"There is a problem",Toast.LENGTH_SHORT).show();
        }
        else{
            String UpdateTitle = editTextUpdateTitle.getText().toString();
            String UpdateDescription = editTextUpdateDescription.getText().toString();
            Intent intent = new Intent();
            intent.putExtra(" UpdateTitle", UpdateTitle);
            intent.putExtra("id",id);
            intent.putExtra("UpdateDescription",UpdateDescription);

            if (selectedImage == null){
                intent.putExtra("image",image);

            }else {


                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ScaledImage = maxSmall(selectedImage,300);
                ScaledImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);
                byte[] image =outputStream.toByteArray();
                intent.putExtra("image",image);




            }
            setResult(RESULT_OK,intent );
            finish();
        }


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
                                imageUpdateSelect.setImageBitmap(selectedImage);

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

