package com.example.bitmlabexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton favBtn;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button saveTripBtn;
    private EditText tripDescription,tripName,startDateET,endDateET;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private String description, name,startDate,endDate;
    private SharepreferenceHandlerC handlerC;

    private List<AddTour> addTourList;
    private AddTourAdapter addTourAdapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handlerC = new SharepreferenceHandlerC(MainActivity.this);

        init();

        readValue();
    }

    private void init() {

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        favBtn= findViewById(R.id.fab);

        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopupDialog();
            }
        });

        addTourList= new ArrayList<>();
        recyclerView = findViewById(R.id.tourRecyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void createPopupDialog(){
        dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.add_trip_layout,null);

        saveTripBtn= view.findViewById(R.id.saveTripButtonId);
        tripDescription= view.findViewById(R.id.tripDescriptionId);
        tripDescription.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        tripName = view.findViewById(R.id.tripNameId);
        startDateET= view.findViewById(R.id.tripStartDateId);
        endDateET= view.findViewById(R.id.tripEndDateId);



        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        saveTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                description= tripDescription.getText().toString();
                name= tripName.getText().toString();
                startDate= startDateET.getText().toString();
                endDate= endDateET.getText().toString();

                saveTripInfo(description,name,startDate,endDate);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();

                    }
                },1000);
            }
        });

    }

    private void saveTripInfo(String description, String name, String startDate, String endDate) {

        String abc = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference objectRef= databaseReference.child("tourmate").child("expense").child(abc);
//        Map<String,Object> objectMap= new HashMap<>();
//        objectMap.put("amount",4000);
//        objectMap.put("type","Dinner");
//        objectMap.put("date",654646466);
//        objectMap.put("time","7:40pm");

        AddTour addTour= new AddTour(name,description,startDate,endDate);

       // String expenseId= objectRef.push().getKey();
       // expense.setExpenseId(expenseId);

        // objectRef.setValue(expense);


        objectRef.child(name).setValue(addTour);

    }

    private void readValue(){

        String ab = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference readDataRef= databaseReference.child("tourmate").child("expense").child(ab);


        String value= handlerC.getUserId();



        readDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                addTourList.clear();

                if (dataSnapshot.exists()){



                   for (DataSnapshot addTourData:  dataSnapshot.getChildren()){

                       AddTour addTourModel= addTourData.getValue(AddTour.class);

                       addTourList.add(addTourModel);

                   }

                   addTourAdapter = new AddTourAdapter(MainActivity.this,addTourList);
                   recyclerView.setAdapter(addTourAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void deleteData(String i){

        String ab = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference deleteDataRef= databaseReference.child("tourmate").child("expense").child(ab).child(i);

        deleteDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot delete : dataSnapshot.getChildren()){
                    delete.getRef().setValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void updateData(String tripName, String tripDes, String tripStartDate, String tripEndDate){



        updatePopupDialog(tripName,tripDes,tripStartDate,tripEndDate);





    }

    public void updatePopupDialog(final String triName, String tripDes, String tripStartDate, String tripEndDate){

        dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.add_trip_layout,null);

        saveTripBtn= view.findViewById(R.id.saveTripButtonId);
        tripDescription= view.findViewById(R.id.tripDescriptionId);
        tripDescription.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        tripName = view.findViewById(R.id.tripNameId);
        startDateET= view.findViewById(R.id.tripStartDateId);
        endDateET= view.findViewById(R.id.tripEndDateId);

        //
        tripName.setText(triName);
        tripDescription.setText(tripDes);
        startDateET.setText(tripStartDate);
        endDateET.setText(tripEndDate);


        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        saveTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                description= tripDescription.getText().toString();
                name= tripName.getText().toString();
                startDate= startDateET.getText().toString();
                endDate= endDateET.getText().toString();

                updateTripInfo(triName,description,name,startDate,endDate);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();

                    }
                },1000);
            }
        });

    }

    private void updateTripInfo(String triName, String description, String name, String startDate, String endDate) {

        String userId= firebaseAuth.getCurrentUser().getUid();
        DatabaseReference updateDataRef= databaseReference.child("tourmate").child("expense").child(userId).child(triName);


        AddTour addTour= new AddTour(triName,description,startDate,endDate);

        // String expenseId= objectRef.push().getKey();
        // expense.setExpenseId(expenseId);

        // objectRef.setValue(expense);


        updateDataRef.setValue(addTour);
    }
}
