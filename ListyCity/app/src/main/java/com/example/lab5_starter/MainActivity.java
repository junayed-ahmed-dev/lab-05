package com.example.lab5_starter;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CityDialogFragment.CityDialogListener {

    private Button addCityButton;
    private Button deleteCityButton;
    private ListView cityListView;

    private ArrayList<City> cityArrayList;
    private ArrayAdapter<City> cityArrayAdapter;

    private FirebaseFirestore db;
    private CollectionReference citiesRef;

    // Track selected and click timing
    private City selectedCity = null;
    private long lastClickTime = 0;
    private int lastClickedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- Initialize Views ---
        addCityButton = findViewById(R.id.buttonAddCity);
        deleteCityButton = findViewById(R.id.buttonDeleteCity);
        cityListView = findViewById(R.id.listviewCities);

        // --- Setup Adapter ---
        cityArrayList = new ArrayList<>();
        cityArrayAdapter = new CityArrayAdapter(this, cityArrayList);
        cityListView.setAdapter(cityArrayAdapter);

        // --- Firestore Setup ---
        db = FirebaseFirestore.getInstance();
        citiesRef = db.collection("cities");

        // --- Firestore listener to keep data synced ---
        citiesRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Firestore", "Listen failed.", error);
                return;
            }

            if (value != null) {
                cityArrayList.clear();
                for (QueryDocumentSnapshot snapshot : value) {
                    String name = snapshot.getString("name");
                    String province = snapshot.getString("province");
                    cityArrayList.add(new City(name, province));
                }
                cityArrayAdapter.notifyDataSetChanged();
            }
        });

        // --- Add City Button ---
        addCityButton.setOnClickListener(view -> {
            CityDialogFragment cityDialogFragment = new CityDialogFragment();
            cityDialogFragment.show(getSupportFragmentManager(), "Add City");
        });

        // --- Handle single tap (select) & double tap (edit) ---
        cityListView.setOnItemClickListener((adapterView, view, position, id) -> {
            City city = cityArrayList.get(position);
            long currentTime = System.currentTimeMillis();

            if (lastClickTime != 0 && (currentTime - lastClickTime) < 400 && lastClickedPosition == position) {
                // ✅ Double tap detected -> open edit dialog
                CityDialogFragment cityDialogFragment = CityDialogFragment.newInstance(city);
                cityDialogFragment.show(getSupportFragmentManager(), "Edit City");
                lastClickTime = 0;
            } else {
                // ✅ Single tap -> select city for deletion
                selectedCity = city;
                lastClickedPosition = position;
                lastClickTime = currentTime;
                cityListView.setItemChecked(position, true);
                Toast.makeText(this, city.getName() + " selected for deletion", Toast.LENGTH_SHORT).show();
            }
        });

        // --- Delete Selected City Button ---
        deleteCityButton.setOnClickListener(view -> {
            if (selectedCity == null) {
                new AlertDialog.Builder(this)
                        .setTitle("No City Selected")
                        .setMessage("Please select a city from the list to delete.")
                        .setPositiveButton("OK", null)
                        .show();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Delete City")
                        .setMessage("Are you sure you want to delete " + selectedCity.getName() + "?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            citiesRef.document(selectedCity.getName())
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("Firestore", "City deleted: " + selectedCity.getName());
                                        cityArrayList.remove(selectedCity);
                                        cityArrayAdapter.notifyDataSetChanged();
                                        Toast.makeText(this, "City deleted successfully", Toast.LENGTH_SHORT).show();
                                        selectedCity = null;
                                    })
                                    .addOnFailureListener(e ->
                                            Log.e("Firestore", "Error deleting city", e));
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        // --- Optional: Long press delete alternative ---
        cityListView.setOnItemLongClickListener((adapterView, view, position, id) -> {
            City cityToDelete = cityArrayList.get(position);
            new AlertDialog.Builder(this)
                    .setTitle("Delete City")
                    .setMessage("Delete " + cityToDelete.getName() + "?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        citiesRef.document(cityToDelete.getName())
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    cityArrayList.remove(position);
                                    cityArrayAdapter.notifyDataSetChanged();
                                    Toast.makeText(this, "City deleted successfully", Toast.LENGTH_SHORT).show();
                                    Log.d("Firestore", "City deleted via long press");
                                })
                                .addOnFailureListener(e -> Log.e("Firestore", "Error deleting city", e));
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });
    }

    // --- Dialog Callbacks ---
    @Override
    public void addCity(City city) {
        cityArrayList.add(city);
        cityArrayAdapter.notifyDataSetChanged();

        DocumentReference docRef = citiesRef.document(city.getName());
        docRef.set(city)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "City added"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error adding city", e));
    }

    @Override
    public void updateCity(City city, String title, String province) {
        city.setName(title);
        city.setProvince(province);
        cityArrayAdapter.notifyDataSetChanged();

        DocumentReference docRef = citiesRef.document(city.getName());
        docRef.set(city)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "City updated"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error updating city", e));
    }

    // --- For testing only ---
    public void addDummyData() {
        City m1 = new City("Edmonton", "AB");
        City m2 = new City("Vancouver", "BC");
        cityArrayList.add(m1);
        cityArrayList.add(m2);
        cityArrayAdapter.notifyDataSetChanged();
    }
}
