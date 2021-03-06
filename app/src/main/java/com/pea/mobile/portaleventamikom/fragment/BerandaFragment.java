package com.pea.mobile.portaleventamikom.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pea.mobile.portaleventamikom.R;
import com.pea.mobile.portaleventamikom.adapter.EventAdapter;
import com.pea.mobile.portaleventamikom.model.EventModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BerandaFragment extends Fragment {


    /**
    * Firebase
    * */

    FirebaseAuth fAuth;

    RecyclerView rcyEventBeranda;
    EventModel eventModel;
    List<EventModel> eventList;
    EventAdapter eventAdapter;


    public BerandaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewBeranda = inflater.inflate(R.layout.fragment_beranda, container, false);
        setHasOptionsMenu(true);

        fAuth = FirebaseAuth.getInstance();

        rcyEventBeranda = viewBeranda.findViewById(R.id.rcyBeranda);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        rcyEventBeranda.setLayoutManager(linearLayoutManager);

        eventList = new ArrayList<>();
        loadEvent();
        return viewBeranda;
    }

    private void loadEvent() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Event");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    EventModel eventModel = ds.getValue(EventModel.class);
                    eventList.add(eventModel);
                    eventAdapter = new EventAdapter(getActivity(), eventList);
                    rcyEventBeranda.setAdapter(eventAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), ""+ databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
