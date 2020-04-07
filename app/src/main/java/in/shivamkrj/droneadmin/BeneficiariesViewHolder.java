package in.shivamkrj.droneadmin;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BeneficiariesViewHolder extends RecyclerView.ViewHolder {

    View item;
    TextView name,rootName,address;


    public BeneficiariesViewHolder(@NonNull View itemView) {
        super(itemView);

        this.item = itemView;
        name = item.findViewById(R.id.ngo_name);
        rootName = item.findViewById(R.id.user_name);
        address = item.findViewById(R.id.detail_ngo);

    }
}
