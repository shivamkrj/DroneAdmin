package in.shivamkrj.droneadmin;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ItemDonateViewHolder extends RecyclerView.ViewHolder {
    View item;
    TextView textView;
    public ItemDonateViewHolder(@NonNull View itemView) {
        super(itemView);
        this.item = itemView;
        this.textView = itemView.findViewById(R.id.textView);
    }
}
