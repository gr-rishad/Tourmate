package com.example.bitmlabexam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AddTourAdapter extends RecyclerView.Adapter<AddTourAdapter.MyViewHolder> {

    private Context context;
    private List<AddTour> addTourList;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

     String tourBudgetQuantity;
       String tourExpenseQuantity;


    public AddTourAdapter(Context context, List<AddTour> addTourList) {
        this.context = context;
        this.addTourList = addTourList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view= inflater.inflate(R.layout.recycler_tour_information_item_design,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final AddTour addTour = addTourList.get(position);

        holder.tourNameTV.setText(addTour.getTourName());
        holder.descriptionTV.setText(addTour.getTourDescription());

        holder.deleteTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nm= addTour.getTourName();

                ((MainActivity)context).deleteData(nm);
            }
        });

        holder.memoriesTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  tripName= addTour.getTourName();
                String tripDes= addTour.getTourDescription();
                String tripStartDate= addTour.getStartDate();
                String tripEndDate= addTour.getEndDate();
                ((MainActivity)context).updateData(tripName,tripDes,tripStartDate,tripEndDate);
            }
        });

        holder.detailsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String tripName = addTour.getTourName();

                String userId = firebaseAuth.getCurrentUser().getUid();


                Intent intent;

                intent = new Intent(context, TourDetailsActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("tripName",tripName);
                bundle.putString("userId",userId);

                System.out.println(",,,,,,,,,,,,,,,,,,,,,,,,,"+userId);


                intent.putExtras(bundle);
                context.startActivity(intent);


                // DatabaseReference dataRef= databaseReference.child("tourmate").child("expense").child(userId).child(tripName).child("Exp");
                // DatabaseReference dataRef2= databaseReference.child("tourmate").child("expense").child(userId).child(tripName).child("Exp2");

//
//               // dataRef2.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        if (dataSnapshot.exists()){
//                           // tourBudgetQuantity= dataSnapshot.child("tourBudgetQuantity").getValue().toString();
//                            tourExpenseQuantity= dataSnapshot.child("tourExpenseQuantity").getValue().toString();
//
//
//                Intent intent;
//
//                intent= new Intent(context,TourDetailsActivity.class);
//
//                Bundle bundle= new Bundle();
//
//                //  bundle.putString("tourBudgetQuantity",tourBudgetQuantity);
//                bundle.putString("tourExpenseQuantity",tourExpenseQuantity);
//                // bundle.putString("tripName",tripName);
//
//
//                intent.putExtras(bundle);
//                context.startActivity(intent);
////
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//
//
//
//            }
//        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return addTourList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tourNameTV,descriptionTV,detailsTV,memoriesTV,deleteTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tourNameTV= itemView.findViewById(R.id.tourNameId);
            descriptionTV= itemView.findViewById(R.id.tourDescriptionId);
            detailsTV= itemView.findViewById(R.id.tourDetailsId);
            memoriesTV= itemView.findViewById(R.id.tourMemoriesId);
            deleteTV= itemView.findViewById(R.id.tourDeleteId);

            detailsTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "details click", Toast.LENGTH_SHORT).show();
                }
            });

//            memoriesTV.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context, "details Click", Toast.LENGTH_SHORT).show();
//                }
//            });
//            deleteTV.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                  //  Toast.makeText(context, "delete click", Toast.LENGTH_SHORT).show();
//
//
//
//                    ((MainActivity)context).deleteData(addTourList.get(getAdapterPosition()));
//                }
//            });
        }
    }
}
