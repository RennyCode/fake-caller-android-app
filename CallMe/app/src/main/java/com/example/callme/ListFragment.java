package com.example.callme;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.callme.Objects.Contact;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    private ListView l_view;
    private AppCompatImageView main_IMG_background;
    private DatabaseReference ref;
    private FirebaseDatabase db;
    private ArrayAdapter<String> adapt;
    private ArrayList<String> contactStrings = new ArrayList<>();
    private int previousPosition = -1;
    private int selectedItemPosition;
    private String selectedID = "";
    private String uid;
    private String[] list;
    public List<Contact> contacts;

    private AdapterView.OnItemSelectedListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        uid = getArguments().getString("UID");
        findViews(view);
        LoadContactList();
        return view;
    }

    private String[] makeList() {
        contactStrings = new ArrayList<>();
        for (int i = 0; i < contacts.size(); i++)
            contactStrings.add(contacts.get(i).getName() + " : " + contacts.get(i).getNumber());
        return contactStrings.toArray(new String[contactStrings.size()]);
    }

    private void setList(String[] list) {
        adapt = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        l_view.setAdapter(adapt);
        l_view.setOnItemClickListener((parent, view1, position, id) -> {
            selectedItemPosition = position;
            Log.e("msg", String.valueOf(position));
            l_view.clearChoices();
            l_view.requestLayout();
            if (previousPosition != -1) {
                View previousView = l_view.getChildAt(previousPosition - l_view.getFirstVisiblePosition());
                if (previousView != null) {
                    previousView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                }
            }
            l_view.setItemChecked(position, true);
            view1.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue_200));
            previousPosition = position;
        });
        selectedItemPosition = -1;
    }

    private void LoadContactList() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contacts = new ArrayList<>();
                for (DataSnapshot contactSnapshot: dataSnapshot.getChildren()){
                    String name = contactSnapshot.child("name").getValue(String.class);
                    String number = contactSnapshot.child("number").getValue(String.class);
                    Contact contact = new Contact(name, number);
                    contacts.add(contact);
                    if(contact != null) {
                        Log.e("msg", contact.getName());
                    } else
                        Log.e("msg", "got null contact!");
                }
                list = makeList();
                setList(list);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Canceled", "Failed to load data.", error.toException());
            }
        });
    }


    private void findViews(View view) {
        l_view = view.findViewById(R.id.l_view);
        main_IMG_background = view.findViewById(R.id.main_IMG_background);
        db = FirebaseDatabase.getInstance("https://callme-3f33e-default-rtdb.europe-west1.firebasedatabase.app");
        ref = db.getReference().child( "users/"+ uid +"/contacts");
        contacts = new ArrayList<>();
    }
    public Contact getSelected() {
        Log.e("msg", String.valueOf(selectedItemPosition));
        return (selectedItemPosition == -1)? null : contacts.get(selectedItemPosition);
    }
    public void deleteSelected(String uid) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int currentIndex = 0;
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    Log.e("msg", String.valueOf(previousPosition));
                    if (currentIndex == previousPosition) {
                        selectedID = itemSnapshot.getKey();
                        break;
                    }
                    currentIndex++;
                }
                if(selectedID.compareTo("") != 0)
                    ref.child(selectedID).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // An error occurred while retrieving the data
                // Handle the error appropriately
            }
        });

    }

}