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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308vacationplanner2.R;
import com.example.d308vacationplanner2.database.Repository;
import com.example.d308vacationplanner2.entities.Excursion;
import com.example.d308vacationplanner2.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationDetails extends AppCompatActivity {

    String name;
    double price;
    int productID;
    EditText editName;
    EditText editPrice;

    EditText editNote;
    Repository repository;

    Vacation currentVacation;

    TextView editStartVacaDate;
    TextView editEndVacaDate;

    TextView editDate;

    int numParts;

    DatePickerDialog.OnDateSetListener startVacaDate;
    DatePickerDialog.OnDateSetListener endVacaDate;
    final Calendar myCalendarStart = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        editName = findViewById(R.id.name);
        editPrice = findViewById(R.id.price);
        editNote = findViewById(R.id.hoteltext);
        editDate = findViewById(R.id.startvacationdate);
        editStartVacaDate = findViewById(R.id.startvacationdate);
        editEndVacaDate = findViewById(R.id.endvacationdate);
        productID = getIntent().getIntExtra("id", -1);
        name = getIntent().getStringExtra("name");
        price = getIntent().getDoubleExtra("price", 0.0);
        editName.setText(name);
        editPrice.setText(Double.toString(price));
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                startActivity(intent);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.partrecyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredParts = new ArrayList<>();
        for (Excursion p : repository.getAllParts()) {
            if (p.getVacationID() == productID) filteredParts.add(p);
        }
        excursionAdapter.setParts(filteredParts);

        startVacaDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, monthOfYear);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                updateLabelStart();
            }

        };

        endVacaDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendarEnd.set(Calendar.YEAR, year);
                myCalendarEnd.set(Calendar.MONTH, monthOfYear);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                updateLabelEnd();
            }

        };

        editStartVacaDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Date date;
                //get value from other screen,but I'm going to hard code it right now
                String info = editStartVacaDate.getText().toString();
                if (info.equals("")) info = "02/10/24";
                try {
                    myCalendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, startVacaDate, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editEndVacaDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Date date;
                //get value from other screen,but I'm going to hard code it right now
                String info = editEndVacaDate.getText().toString();
                if (info.equals("")) info = "02/10/24";
                try {
                    myCalendarEnd.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(VacationDetails.this, endVacaDate, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMinDate(myCalendarStart.getTimeInMillis());

                datePickerDialog.show();
            }
        });
    }

    private void updateLabelStart() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editStartVacaDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    private void updateLabelEnd() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editEndVacaDate.setText(sdf.format(myCalendarEnd.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacationdetails, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.partsave) {
            Vacation vacation;
            if (productID == -1) {
                if (repository.getmAllVacations().size() == 0) productID = 1;
                else
                    productID = repository.getmAllVacations().get(repository.getmAllVacations().size() - 1).getVacationID() + 1;
                vacation = new Vacation(productID, editName.getText().toString(), Double.parseDouble(editPrice.getText().toString()));
                repository.insert(vacation);
                this.finish();
            } else {
                vacation = new Vacation(productID, editName.getText().toString(), Double.parseDouble(editPrice.getText().toString()));
                repository.update(vacation);
                this.finish();
            }
        }
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion p : repository.getAllParts()) {
            if(p.getVacationID() == productID) filteredExcursions.add(p);
        }
        StringBuilder excursionDetails = new StringBuilder();
        for (Excursion p : filteredExcursions) {
            excursionDetails.append("Excursion Name: ")
                    .append(p.getExcursionName())
                    .append(", Price: $")
                    .append(p.getPrice())
                    .append("\n");
        }
        if (item.getItemId() == R.id.share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            //sendIntent.putExtra(Intent.EXTRA_TEXT, editNote.getText().toString() + "EXTRA_TEXT");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "These are the vacation details: vacationID: " + currentVacation + ", the name of our vacation: " + editName.getText().toString() + ", price: $" +
                    Double.parseDouble(editPrice.getText().toString()) + ", the hotel name: " +
                    editNote.getText().toString() + ", this is our start date: " +
                    editStartVacaDate.getText().toString() + ", this is our end date: " +
                    editEndVacaDate.getText().toString() + ", associated excursions: " + excursionDetails
                    + " Let us know what you think!");

            sendIntent.putExtra(Intent.EXTRA_TITLE, editNote.getText().toString() + "EXTRA_TITLE");
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
            return true;
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
            Long trigger = myDate.getTime();
            Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
            intent.putExtra("key","message I want to see");
            PendingIntent sender=PendingIntent.getBroadcast(VacationDetails.this,++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger,sender);
            return true;
        }
        return true;
    }
}
/*
        if (item.getItemId() == R.id.productdelete) {
            for (Vacation prod : repository.getmAllVacations()) {
                if (prod.getVacationID() == productID) currentVacation = prod;
            }
            numParts = 0;
            for (Excursion part : repository.getAllParts()) {
                if (part.getVacationID() == productID) ++numParts;
            }
            if (numParts == 0) {
                repository.delete(currentVacation);
                Toast.makeText(VacationDetails.this, currentVacation.getVacationName() + " was deleted", Toast.LENGTH_LONG).show();
                VacationDetails.this.finish();
            } else {
                Toast.makeText(VacationDetails.this, "Can't delete a product with parts", Toast.LENGTH_LONG).show();

            }
        }
        return true;
    }
}
 */
