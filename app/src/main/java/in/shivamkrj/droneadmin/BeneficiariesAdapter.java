package in.shivamkrj.droneadmin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class BeneficiariesAdapter extends RecyclerView.Adapter<BeneficiariesViewHolder> {

    ArrayList<BeneficiariesData> list;
    LayoutInflater layoutInflater;
    ViewClickInterface viewClickInterface;


    public BeneficiariesAdapter(Context context, ArrayList<BeneficiariesData> list, ViewClickInterface viewClickInterface){
        this.list=list;
        this.viewClickInterface=viewClickInterface;
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }




    @NonNull
    @Override
    public BeneficiariesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View output = layoutInflater.inflate(R.layout.beneficiaries_view,viewGroup,false);
        return new BeneficiariesViewHolder(output);
    }

    @Override
    public void onBindViewHolder(@NonNull final BeneficiariesViewHolder beneficiariesViewHolder, int i) {
        final BeneficiariesData data = list.get(beneficiariesViewHolder.getAdapterPosition());
        beneficiariesViewHolder.name.setText(data.Name);
        beneficiariesViewHolder.rootName.setText(data.RootName);
        beneficiariesViewHolder.address.setText(data.Address);

        beneficiariesViewHolder.item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                viewClickInterface.onLongClick(v,beneficiariesViewHolder.getAdapterPosition());
                return true;
            }
        });

    }
    @Override
    public int getItemCount() {
        return list.size();
    }


}
