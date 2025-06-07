package com.example.d308vacationplanner2.UI;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308vacationplanner2.R;
import com.example.d308vacationplanner2.entities.Vacation;

import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {
    private List<Vacation> mVacation;
    private final Context context;

    private final LayoutInflater mInflater;

    public VacationAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context=context;
    }

    public class VacationViewHolder extends RecyclerView.ViewHolder {
        private final TextView vacationItemView;
        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            vacationItemView=itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    final Vacation current=mVacation.get(position);
                    Intent intent=new Intent(context,VacationDetails.class);
                    intent.putExtra("id", current.getVacationID());
                    intent.putExtra("name", current.getVacationName());
                    intent.putExtra("price", current.getPrice());
                    intent.putExtra("hotel", current.getHotel());
                    context.startActivity(intent);
                }
            });
        }
    }
    @NonNull
    @Override
    public VacationAdapter.VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=mInflater.inflate(R.layout.vacation_item_list, parent, false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationAdapter.VacationViewHolder holder, int position) {
        if(mVacation!=null) {
            Vacation current=mVacation.get(position);
            String name=current.getVacationName();
            holder.vacationItemView.setText(name);
        }
        else{
            holder.vacationItemView.setText("no product name");
        }
    }

    @Override
    public int getItemCount() {
        if(mVacation!=null) {
            return mVacation.size();
        }
        else return 0;
    }


    public void setVacation(List<Vacation> vacation) {
        mVacation=vacation;
        notifyDataSetChanged();
    }

}
