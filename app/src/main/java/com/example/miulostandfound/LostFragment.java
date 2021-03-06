package com.example.miulostandfound;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * A simple {@link Fragment} subclass.
 */
public class LostFragment extends Fragment  implements View.OnClickListener{
    private RecyclerView mRecyclerView;
    private LostAdapter mAdapter;
    FloatingActionButton fab;
    private DatabaseReference mDatabaseRef;
    private List<imageData> mUploads;
    int backButtonCount=0;

    public LostFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view =  inflater.inflate(R.layout.fragment_lost, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mUploads = new ArrayList<>();
        fab =view.findViewById(R.id.fab);
        fab=(FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Image");
        Log.i("Tag",mUploads.toString());

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    imageData upload = postSnapshot.getValue(imageData.class);

                    if(upload.isFound==false){
                        mUploads.add(upload);}
                }

                LayoutInflater inflater = LayoutInflater.from(container.getContext());
                mAdapter = new LostAdapter( mUploads);

                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(LostFragment.this, databaseError.getMessage(), LENGTH_SHORT).show();
//                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
        return view;
    }


    @Override
    public void onClick(View v) {
        //do what you want to do when button is clicked
        switch (v.getId()) {
            case R.id.fab:
                ((MainActivity)getActivity()).onclickfab();

                break;

        }
    }

}
