package com.example.bitmlabexam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class WalletFragment extends Fragment {

    private EditText burnedET,consumedET;
    private Button burnedBtn,consumedBtn;
    double calsBurned ;
    double calsConsumed ;
    TextView numberOfCals,balanceTk,expenseTK;
    ProgressBar pieChart;
    private SharepreferenceHandlerC handlerC;
    private Context thisContext;
    private  SharedPreferences.Editor editor;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference ;
    String getBudget, getExpense,tripName,userId;
    String tourBudgetQuantity,tourExpenseQuantity;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_wallet, container, false);

        this.thisContext=container.getContext();


        init(view);
        updateChart();
        return view;
    }

    private void init(View view) {

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Bundle bundle= getActivity().getIntent().getExtras();

        if (bundle != null){

             tripName= bundle.getString("tripName");
             userId= bundle.getString("userId");
        }

        handlerC = new SharepreferenceHandlerC(getContext());
        getData(tripName,userId);

        getExpenseData(tripName,userId);
        //showRatio();




        balanceTk= view.findViewById(R.id.balanceTkId);
        expenseTK= view.findViewById(R.id.expenseTkId);
        burnedET= view.findViewById(R.id.burnedETId);
        consumedET= view.findViewById(R.id.consumedETId);
        burnedBtn= view.findViewById(R.id.burnedButtonId);
        consumedBtn= view.findViewById(R.id.consumedButtonId);

        burnedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double ab = Double.parseDouble(burnedET.getText().toString());

            //  calsBurned= Integer.valueOf( handlerC.getBudget());

                //calsBurned= d.valueOf(getBudget);

                calsBurned= Double.parseDouble(tourBudgetQuantity);

                System.out.println("+++++++++++++++++++++++++++++++++++++++++++"+calsBurned);

                calsBurned= calsBurned+ab;

                System.out.println("+++++++++++++++++++++++++8888888888888888888888888+"+calsBurned);


                numberOfCals.setText(calsBurned + " / " + calsConsumed);

                // Calculate the slice size and update the pie chart:


                //double con= Double.parseDouble( handlerC.getExpense()) / Double.parseDouble(handlerC.getBudget());
            //    double con= Integer.valueOf( calsBurned) / Integer.valueOf(calsConsumed);

                //   double d = (double) calsBurned / (double) calsConsumed;
               // int progress = (int) (con * 100);
            //    pieChart.setProgress(progress);

                saveBudgetInfo(calsBurned);

              // handlerC.saveBudget(String.valueOf(calsBurned));
               // updateChart();
            }
        });

        consumedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the old value:
                double cc = Double.parseDouble(consumedET.getText().toString());

               // calsConsumed= Integer.valueOf(handlerC.getExpense());
                calsConsumed= Double.parseDouble(tourExpenseQuantity);

                System.out.println("+++++++++++++++++++++++++++++++++++++++++++"+calsConsumed);
                calsConsumed =calsConsumed+ cc;


             //   calsBurned= calsBurned+ab;
//
                System.out.println("+++++++++++++++++++++++++8888888888888888888888888+"+calsConsumed);


                numberOfCals.setText(calsBurned + " / " + calsConsumed);

                // Calculate the slice size and update the pie chart:

             saveExpenseInfo(calsConsumed);
                //double con= Double.parseDouble( handlerC.getExpense()) / Double.parseDouble(handlerC.getBudget());
              //  double con= Integer.valueOf( calsBurned) / Integer.valueOf(calsConsumed);

                //   double d = (double) calsBurned / (double) calsConsumed;
             //   int progress = (int) (con * 100);
             //   pieChart.setProgress(progress);

             //   saveBudgetInfo(calsBurned,calsConsumed);

              //  handlerC.saveExpense(String.valueOf(calsConsumed));

                System.out.println(".----------------------------------------------------"+handlerC.getBudget());
              //  updateChart();
            }
        });

        numberOfCals = view.findViewById(R.id.numberCalories);
         pieChart = view.findViewById(R.id.stats_progressbar);

    }




    private void updateChart(){
        // Update the text in a center of the chart:


//       balanceTk.setText(handlerC.getBudget());
//       expenseTK.setText(handlerC.getExpense());
       balanceTk.setText(getBudget);
       expenseTK.setText(getExpense);

      //  numberOfCals.setText(handlerC.getExpense() + " / " + handlerC.getBudget());
        numberOfCals.setText(getExpense + " / " + getBudget);

        // Calculate the slice size and update the pie chart:


        //double con= Double.parseDouble( handlerC.getExpense()) / Double.parseDouble(handlerC.getBudget());
//        double con= Double.parseDouble( getExpense) / Double.parseDouble(getBudget);

     //   double d = (double) calsBurned / (double) calsConsumed;
       // int progress = (int) (con * 100);
        //pieChart.setProgress(progress);

     //  saveBudgetInfo(getBudget,getExpense);
    }

    private void saveBudgetInfo(double budget) {

        String userId= firebaseAuth.getCurrentUser().getUid();
        DatabaseReference dataRef= databaseReference.child("tourmate").child("expense").child(userId).child(tripName);

        TourExpenseModel tourExpenseModel= new TourExpenseModel("Budget",budget);



        dataRef.child("Exp").setValue(tourExpenseModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(thisContext, "OK", Toast.LENGTH_SHORT).show();
                   // updateChart();
                }else{
                    Toast.makeText(thisContext, ""+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveExpenseInfo(double expense) {

        String userId= firebaseAuth.getCurrentUser().getUid();
        DatabaseReference dataRef= databaseReference.child("tourmate").child("expense").child(userId).child(tripName);

        TourExpenseModel tourExpenseModel= new TourExpenseModel(expense,"Expense");



        dataRef.child("Exp2").setValue(tourExpenseModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(thisContext, "OK", Toast.LENGTH_SHORT).show();
                    // updateChart();
                }else{
                    Toast.makeText(thisContext, ""+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void getData(String tripName,String  userId){

       // String userId = getActivity().firebaseAuth.getCurrentUser().getUid();

        DatabaseReference dataRef= databaseReference.child("tourmate").child("expense").child(userId).child(tripName).child("Exp");


        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    tourBudgetQuantity= dataSnapshot.child("tourBudgetQuantity").getValue().toString();
                    balanceTk.setText(tourBudgetQuantity);

                  handlerC.saveBudget(tourBudgetQuantity);

                    System.out.println("/////////////----------//////////////"+handlerC.getExpense());
                    //numberOfCals.setText(Double.parseDouble(tourBudgetQuantity) + " / " + Double.parseDouble(tourExpenseQuantity));


                }else {

                    tourBudgetQuantity="0";
                    handlerC.saveBudget(tourBudgetQuantity);
                    //numberOfCals.setText(Double.parseDouble(tourBudgetQuantity) + " / " + Double.parseDouble(tourExpenseQuantity));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void getExpenseData(String tripName, String userId) {

        DatabaseReference dataRef= databaseReference.child("tourmate").child("expense").child(userId).child(tripName).child("Exp2");


        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    tourExpenseQuantity= dataSnapshot.child("tourExpenseQuantity").getValue().toString();
                    expenseTK.setText(tourExpenseQuantity);
                    handlerC.saveExpense(tourExpenseQuantity);

                   // numberOfCals.setText(Double.parseDouble(tourBudgetQuantity) + " / " + Double.parseDouble(tourExpenseQuantity));
                }else {
                    tourExpenseQuantity= "0";
                    handlerC.saveExpense(tourExpenseQuantity);


                  //  numberOfCals.setText(Double.parseDouble(tourBudgetQuantity) + " / " + Double.parseDouble(tourExpenseQuantity));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    private void showRatio(){
//        numberOfCals.setText(Double.parseDouble(handlerC.getExpense()) + " / " + Double.parseDouble(handlerC.getBudget()));
//    }

}
