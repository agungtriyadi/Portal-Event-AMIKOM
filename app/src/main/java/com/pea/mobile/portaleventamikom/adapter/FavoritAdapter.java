package com.pea.mobile.portaleventamikom.adapter;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.pea.mobile.portaleventamikom.R;
import com.pea.mobile.portaleventamikom.model.FavoritModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoritAdapter extends RecyclerView.Adapter<FavoritAdapter.favoritHolder> {
    Context ctx;
    List<FavoritModel> favoritModelList;

    class favoritHolder extends RecyclerView.ViewHolder {
        //firebase user
        FirebaseAuth uAuth;
        FirebaseUser user;
        //firebase database
        FirebaseDatabase db;
        DatabaseReference drFavorit;
        ImageView eImageView, unFav;
        TextView txtEJudul, txtEDeskripsi;
        String userID, eventID;

        public favoritHolder(@NonNull View itemView) {
            super(itemView);
            eImageView = itemView.findViewById(R.id.imgFavoritView);
            txtEJudul = itemView.findViewById(R.id.txtFavoritViewJudul);
            txtEDeskripsi= itemView.findViewById(R.id.txtFavoritViewDeskripsi);

            unFav = itemView.findViewById(R.id.imgUnFav);

            uAuth = FirebaseAuth.getInstance();
            user = uAuth.getCurrentUser();
            db = FirebaseDatabase.getInstance();
            //get from USer
            userID = user.getUid();
            drFavorit = db.getReference("User").child(userID.toString().trim()).child("Favorite");

        }
    }
    public FavoritAdapter(Context ctx, List<FavoritModel> favoritModelList) {
        this.ctx = ctx;
        this.favoritModelList = favoritModelList;
    }

    @NonNull
    @Override
    public favoritHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.item_event_favorit,parent, false);
        return new favoritHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final favoritHolder holder, int position) {
        final String uId = holder.userID;

        //event
        String eJudul = favoritModelList.get(position).geteJudulFav();
        String eDescr = favoritModelList.get(position).geteDesFav();
        String eImage = favoritModelList.get(position).geteImagePoster();
        final String fid = favoritModelList.get(position).getfId();

        holder.txtEJudul.setText(eJudul);
        holder.txtEDeskripsi.setText(eDescr);
        holder.txtEDeskripsi.setMovementMethod(new ScrollingMovementMethod());
        try {
            Picasso.get().load(eImage).into(holder.eImageView);
        } catch (Exception e) { }

        holder.unFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fId = fid.trim();
                deleteFavorite(uId, fId);
                Toast.makeText(v.getContext(),"Menghapus dari Favorit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteFavorite(String uId, String fId) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("User").child(uId).child("Favorite").child(fId);
        dbRef.setValue(null);
    }

    @Override
    public int getItemCount() {
        return favoritModelList.size();
    }
}
