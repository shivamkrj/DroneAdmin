package in.shivamkrj.droneadmin;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class NgoViewHolder extends RecyclerView.ViewHolder {

    View item;
    TextView ngoName,userName,phoneNumber,detail;
    ImageView callButton;

    public NgoViewHolder(@NonNull View itemView) {
        super(itemView);
        this.item = itemView;
        ngoName = item.findViewById(R.id.ngo_name);
        userName = item.findViewById(R.id.user_name);
        phoneNumber = item.findViewById(R.id.phone_number);
        detail = item.findViewById(R.id.detail_ngo);
        callButton  = item.findViewById(R.id.call_ngo);
    }
}
