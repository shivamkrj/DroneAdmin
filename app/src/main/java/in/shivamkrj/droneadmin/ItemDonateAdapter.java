package in.shivamkrj.droneadmin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ItemDonateAdapter extends RecyclerView.Adapter<ItemDonateViewHolder> {

    ArrayList<itemData> arrayList;
    LayoutInflater layoutInflater;
    ViewClickInterface viewClickInterface;

    public ItemDonateAdapter(Context context,ArrayList<itemData> arrayList,ViewClickInterface viewClickInterface){
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.arrayList = arrayList;
        this.viewClickInterface = viewClickInterface;
    }




    @NonNull
    @Override
    public ItemDonateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ItemDonateViewHolder(layoutInflater.inflate(R.layout.textview_layout,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemDonateViewHolder itemDonateViewHolder, int i) {
        String  value = arrayList.get(itemDonateViewHolder.getAdapterPosition()).data;
        itemDonateViewHolder.textView.setText(value);
        itemDonateViewHolder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                viewClickInterface.onLongClick(v,itemDonateViewHolder.getAdapterPosition());
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
