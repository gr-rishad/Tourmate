package com.example.bitmlabexam;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class MemoriesFragment extends Fragment {

    private FloatingActionButton favBtn;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private FloatingActionButton addMemoriesFabBtn,cameraFabButton;
    private EditText tripDescription;
    private String desc;
    private Button saveMemoriesBtn;
    private ImageView tripImage;
    private Bitmap bitmapImage = null;
    private String downloadLink;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private  Uri selectedImage;
    @SuppressWarnings("VisibleForTests")
    private AddMemoriesModel addMemoriesModel;
    private int i;


    public MemoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_memories, container, false);
        init(view);
        return view;
    }

    private void init(View view) {

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();


        addMemoriesFabBtn= view.findViewById(R.id.addMemoriesFab);
        addMemoriesFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMemories();
            }
        });



    }

    private void openPicDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String[] items = {"From Camera","From Gallery"};
        builder.setTitle("Choose an action");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        openCamera();
                        break;
                    case 1:
                        openGallery();
                        break;
                }
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent,99);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,88);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == RESULT_OK){
            if(requestCode == 88){
                Bundle bundle = data.getExtras();
                bitmapImage = (Bitmap) bundle.get("data");
                tripImage.setImageBitmap(bitmapImage);
               // documentCancelIV.setVisibility(View.VISIBLE);
            }
            else if(requestCode == 99){
               // Uri uri = data.getData();
                 selectedImage = data.getData();
                tripImage.setImageURI(selectedImage);
                // documentIV.setImageBitmap(bitmapImage);
            }
        }
    }
    private void upLoadImage(){

        StorageReference storage2= storageReference.child("upload"+UUID.randomUUID());
        storage2.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful()){
                    Toast.makeText(getContext(), "Upload Successfully", Toast.LENGTH_SHORT).show();
                    AddMemories();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                downloadLink= taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();


            }
        });
    }
    private void createMemories(){

        dialogBuilder= new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.add_memories_layout,null);
        //
        tripImage= view.findViewById(R.id.imageId);
        cameraFabButton= view.findViewById(R.id.imageFabButtonId);
        tripDescription= view.findViewById(R.id.tourTripDescriptionId);
        saveMemoriesBtn= view.findViewById(R.id.saveMemoriesButtonId);

        saveMemoriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upLoadImage();
               // AddMemories();

            }
        });

        cameraFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPicDialog();
            }
        });



        dialogBuilder.setView(view);
       dialog= dialogBuilder.create();
        dialog.show();


    }


    private void AddMemories(){

         i=1;
        String abc = firebaseAuth.getCurrentUser().getUid();
        desc= tripDescription.getText().toString();

        final DatabaseReference objectRef= databaseReference.child("tourmate").child("expense").child(abc).child(TourDetailsActivity.tName);

        String key = objectRef.push().getKey();


        addMemoriesModel=new AddMemoriesModel(desc,downloadLink);

        objectRef.child("tourMemorie").child("memories").child(key).setValue(addMemoriesModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                i++;
                Toast.makeText(getContext(), "Add Successfully", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }



}
