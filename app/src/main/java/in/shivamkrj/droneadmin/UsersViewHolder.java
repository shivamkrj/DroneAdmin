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
    TextView addressTextView;
    TextView nameTextView;
    TextView itemsTextView;
    TextView timeTextView;

    public UsersViewHolder(@NonNull View itemView) {
        super(itemView);
        item = itemView;
        latitudeTextView = item.findViewById(R.id.latitude);
        longitudeTextView = item.findViewById(R.id.Longitude);
        addressTextView = item.findViewById(R.id.Altitude);
        usernameTextView = item.findViewById(R.id.user_name);
        timeTextView = item.findViewById(R.id.time);
        nameTextView = item.findViewById(R.id.name);
        itemsTextView = item.findViewById(R.id.items);

    }
}
