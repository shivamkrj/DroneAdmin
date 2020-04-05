package in.shivamkrj.droneadmin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    ArrayList<Message> items = new ArrayList<>();
    LayoutInflater layoutInflater;

    public MessageAdapter(Context context,ArrayList<Message> items) {
        layoutInflater =(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View output=layoutInflater.inflate(R.layout.view_chat,viewGroup,false);
        return new MessageViewHolder(output);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int i) {
        Message message = items.get(messageViewHolder.getAdapterPosition());
        messageViewHolder.textView.setText(message.text);
        if(message.isAdmin){
            messageViewHolder.textView.setBackgroundColor(messageViewHolder.item.getResources().getColor(R.color.adminMessage));
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            params.setMargins(10,10,200,10);
//            messageViewHolder.textView.setLayoutParams(params);
        }else {
            messageViewHolder.textView.setBackgroundColor(messageViewHolder.item.getResources().getColor(R.color.userMessage));
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            params.setMargins(200,10,10,10);
//            messageViewHolder.textView.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
