package com.example.d308vacationplanner2.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d308vacationplanner2.R;
import com.example.d308vacationplanner2.database.Repository;
import com.example.d308vacationplanner2.entities.Excursion;
import com.example.d308vacationplanner2.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {

    String name;

    String excursionDate;

    int vacationID;

    Excursion currentExcursion;
    Double price;
    int excursionID;
    EditText editName;
    EditText editPrice;
    EditText editNote;
    TextView editDate;
    Repository repository;

    DatePickerDialog.OnDateSetListener startDate;

    final Calendar myCalendarStart = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);
        /*
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
         */
        repository = new Repository(getApplication());
        editName = findViewById(R.id.nametext);
        name = getIntent().getStringExtra("name");
        editName.setText(name);
        excursionID = getIntent().getIntExtra("id", -1);
        vacationID = getIntent().getIntExtra("vacid", -1);
        excursionDate = getIntent().getStringExtra("ExcursionDate");
        editDate = findViewById(R.id.date);
        editDate.setText(excursionDate);
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

     /*
        ArrayList<Integer> productIdList = new ArrayList<>();
        for (Vacation product : productArrayList) {
            productIdList.add(product.getVacationID());
        }
        ArrayAdapter<Integer> productIdAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, productIdList);
*/
        startDate = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, monthOfYear);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart();
            }
        };
        Spinner spinner = findViewById(R.id.spinner);
        ArrayList<Vacation> productArrayList = new ArrayList<>();
        productArrayList.addAll(repository.getmAllVacations());
        ArrayAdapter<Vacation> productAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productArrayList);
        spinner.setAdapter(productAdapter);
        spinner.setSelection(0);


        editDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Date date;
                //get value from other screen,but I'm going to hard code it right now
                String info = editDate.getText().toString();
                if (info.equals("")) info = "05/01/25";
                try {
                    myCalendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(ExcursionDetails.this, startDate, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabelStart() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_partdetails, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        if (item.getItemId() == R.id.partsave) {
            Excursion part;
            if (excursionID == -1) {
                if (repository.getAllParts().size() == 0)
                    excursionID = 1;
                else
                    excursionID = repository.getAllParts().get(repository.getAllParts().size() - 1).getExcursionID() + 1;
                part = new Excursion(excursionID, editName.getText().toString(), excursionID, editDate.getText().toString());
                repository.insert(part);
                this.finish();
            } else {
                part = new Excursion(excursionID, editName.getText().toString(), excursionID, editDate.getText().toString());
                repository.update(part);
                this.finish();
            }
            return true;
        }

        //if the user selects the menu option Delete Excursion...
        if (item.getItemId() == R.id.excursiondelete) {
            for (Excursion excursion : repository.getAllParts()) {
                if (excursion.getExcursionID() == excursionID) currentExcursion = excursion;
            }
            repository.delete(currentExcursion);
            Toast.makeText(ExcursionDetails.this, currentExcursion.getExcursionName() + " was deleted", Toast.LENGTH_LONG).show();
            ExcursionDetails.this.finish();
        }

        if (item.getItemId() == R.id.notify) {
            String dateFromScreen = editDate.getText().toString();
            String myFormat = "MM/dd/yy"; // In which you need to put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                Long trigger = myDate.getTime();
                Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
                intent.putExtra("key", "message I want to see");
                PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            } catch (Exception e) {

            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateLabelStart();
    }
}

