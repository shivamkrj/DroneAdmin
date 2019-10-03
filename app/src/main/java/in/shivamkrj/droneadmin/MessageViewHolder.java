package in.shivamkrj.droneadmin;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class MessageViewHolder extends RecyclerView.ViewHolder{

    View item;
    TextView textView;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
        item = itemView;
        textView = item.findViewById(R.id.textView);
    }

}
