package com.example.d308vacationplanner2.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d308vacationplanner2.R;


public class MainActivity extends AppCompatActivity {
    public static int numAlert;
    private Button loginButton;
    private static final String TAG = "SearchLog";
    private SearchView searchView;
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> dataList;

    private final String correctUsername = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button button=findViewById(R.id.button);
        searchView = findViewById(R.id.search_view);
        listView = findViewById(R.id.list_view);

        dataList = new ArrayList<>();
        // Type "Enter" "Jamie" or "WGU"
        dataList.add("Hawaii - Maui");
        dataList.add("Thailand - Bangkok");
        dataList.add("Florida - Miami");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, VacationList.class);
                intent.putExtra("test", "Information sent");
                startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "User submitted query: " + query);
                return false; // Let the SearchView perform default action (if any)
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "User typing: " + newText);
                adapter.getFilter().filter(newText);  // Filter list
                return false; // We handled it (like filtering)
            }
        });
    }
}
