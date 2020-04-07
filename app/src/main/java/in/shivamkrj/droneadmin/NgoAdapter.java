package in.shivamkrj.droneadmin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class NgoAdapter extends RecyclerView.Adapter<NgoViewHolder> {

    ArrayList<NgoData> ngoDataArrayList;
    LayoutInflater layoutInflater;
    ViewClickInterface viewClickInterface;
    boolean isNgo = true;

    public NgoAdapter(Context context,ArrayList<NgoData> ngoDataArrayList,ViewClickInterface viewClickInterface){
        this.ngoDataArrayList=ngoDataArrayList;
        this.viewClickInterface=viewClickInterface;
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @NonNull
    @Override
    public NgoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View output = layoutInflater.inflate(R.layout.ngo_view,viewGroup,false);
        return new NgoViewHolder(output);
    }

    @Override
    public void onBindViewHolder(@NonNull final NgoViewHolder ngoViewHolder, int i) {
        final NgoData ngoData = ngoDataArrayList.get(ngoViewHolder.getAdapterPosition());
        ngoViewHolder.ngoName.setText(ngoData.ngoName);
        ngoViewHolder.userName.setText(ngoData.userName);
        ngoData.phoneNUmber="+91"+ngoData.phoneNUmber.substring(ngoData.phoneNUmber.length()-10);
        ngoViewHolder.phoneNumber.setText(ngoData.phoneNUmber);
        ngoViewHolder.detail.setText(ngoData.detail);
        ngoViewHolder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewClickInterface.onViewClick(ngoViewHolder.item,ngoViewHolder.getAdapterPosition());
            }
        });
        ngoViewHolder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                viewClickInterface.onLongClick(v,ngoViewHolder.getAdapterPosition());
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return ngoDataArrayList.size();
    }
}
