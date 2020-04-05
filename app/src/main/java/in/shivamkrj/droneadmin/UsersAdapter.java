package in.shivamkrj.droneadmin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersViewHolder> {

    ArrayList<UsersData> usersList;
    LayoutInflater inflater;
    ViewClickInterface viewClickInterface;

    public UsersAdapter(Context context,ArrayList<UsersData> usersList, ViewClickInterface viewClickInterface) {
        this.usersList = usersList;
        this.viewClickInterface = viewClickInterface;
        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View output=inflater.inflate(R.layout.users_view,viewGroup,false);
        return new UsersViewHolder(output);
    }

    @Override
    public void onBindViewHolder(@NonNull final UsersViewHolder usersViewHolder, int i) {
        UsersData usersData = usersList.get(usersViewHolder.getAdapterPosition());
        usersViewHolder.altitudeTextView.setText(usersData.altitude);
        usersViewHolder.latitudeTextView.setText(usersData.latitude);
        usersViewHolder.longitudeTextView.setText(usersData.longitude);
        usersViewHolder.usernameTextView.setText(usersData.username);
        usersViewHolder.timeTextView.setText(usersData.time);
        usersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewClickInterface.onViewClick(v,usersViewHolder.getAdapterPosition());
            }
        });
        usersViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                viewClickInterface.onLongClick(v,usersViewHolder.getAdapterPosition());
                return true;
            }
        });
    }



    @Override
    public int getItemCount() {
        return usersList.size();
    }
}
