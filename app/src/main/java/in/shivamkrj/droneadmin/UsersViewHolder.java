package in.shivamkrj.droneadmin;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class UsersViewHolder extends RecyclerView.ViewHolder {

    View item;
    TextView usernameTextView;
    TextView latitudeTextView;
    TextView longitudeTextView;
    TextView altitudeTextView;
    TextView timeTextView;

    public UsersViewHolder(@NonNull View itemView) {
        super(itemView);
        item = itemView;
        latitudeTextView = item.findViewById(R.id.latitude);
        longitudeTextView = item.findViewById(R.id.Longitude);
        altitudeTextView = item.findViewById(R.id.Altitude);
        usernameTextView = item.findViewById(R.id.user_name);
        timeTextView = item.findViewById(R.id.time);
    }
}
