package com.example.d308vacationplanner2.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308vacationplanner2.R;
import com.example.d308vacationplanner2.entities.Excursion;

import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionHolder> {

    private List<Excursion> mExcursion;
    private final Context context;
    private final LayoutInflater mInflater;

    class ExcursionHolder extends RecyclerView.ViewHolder {


        private final TextView excursionItemView;
        private final TextView excursionItemView2;

        private ExcursionHolder(View itemView) {
            super(itemView);
            excursionItemView = itemView.findViewById(R.id.textView3);
            excursionItemView2 = itemView.findViewById(R.id.textView4);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Excursion current = mExcursion.get(position);
                    Intent intent = new Intent(context, ExcursionDetails.class);
                    intent.putExtra("id", current.getExcursionID());
                    intent.putExtra("name", current.getExcursionName());
                    intent.putExtra("price", current.getPrice());
                    intent.putExtra("prodID", current.getVacationID());
                    context.startActivity(intent);
                }
            });
        }
    }

    public ExcursionAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public ExcursionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.excursion_list_item, parent, false);
        return new ExcursionHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionHolder holder, int position) {
        if(mExcursion!=null){
            Excursion current=mExcursion.get(position);
            String name=current.getExcursionName();
            int prodID= current.getVacationID();
            holder.excursionItemView.setText(name);
            holder.excursionItemView2.setText(Integer.toString(prodID));
        }
        else{
            holder.excursionItemView.setText("No excursion name");
            holder.excursionItemView2.setText("No vacation id");
        }
    }

    public void setParts(List<Excursion> excursion){
        mExcursion=excursion;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (mExcursion == null) ? 0 : mExcursion.size();
    }
}