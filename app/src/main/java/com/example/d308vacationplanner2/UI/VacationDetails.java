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

import androidx.appcompat.app.AppCompatActivity;
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

    String hotel;
    double price;
    int productID;
    EditText editName;
    EditText editPrice;

    EditText editHotel;

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
        editHotel = findViewById(R.id.hotelz);
        editDate = findViewById(R.id.startvacationdate);
        editStartVacaDate = findViewById(R.id.startvacationdate);
        editEndVacaDate = findViewById(R.id.endvacationdate);
        productID = getIntent().getIntExtra("id", -1);
        name = getIntent().getStringExtra("name");
        hotel = getIntent().getStringExtra("hotel");
        price = getIntent().getDoubleExtra("price", 0.0);
        editName.setText(name);
        editHotel.setText(hotel);
        editPrice.setText(Double.toString(price));
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        // Load existing vacation details
        if (productID != -1) {
            repository = new Repository(getApplication());
            List<Vacation> vacations = repository.getmAllVacations();
            for (Vacation v : vacations) {
                if (v.getVacationID() == productID) {
                    currentVacation = v;
                    editStartVacaDate.setText(v.getStartDate());
                    editEndVacaDate.setText(v.getEndDate());
                    break;
                }
            }
        }

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
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, monthOfYear);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart();
            }
        };

        endVacaDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendarEnd.set(Calendar.YEAR, year);
                myCalendarEnd.set(Calendar.MONTH, monthOfYear);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelEnd();
            }
        };

        editStartVacaDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editStartVacaDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    private void updateLabelEnd() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editEndVacaDate.setText(sdf.format(myCalendarEnd.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacationdetails, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.partsave) {
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            String startDateString = sdf.format(myCalendarStart.getTime());
            String endDateString = sdf.format(myCalendarEnd.getTime());
            try {
                Date startDate = sdf.parse(startDateString);
                Date endDate = sdf.parse(endDateString);
                if (endDate.before(startDate)) {
                    Toast.makeText(this, "End date must be AFTER start date", Toast.LENGTH_LONG).show();
                } else {
                    Vacation vacation;

                    if (productID == -1) {
                        if (repository.getmAllVacations().size() == 0) productID = 1;
                        else productID = repository.getmAllVacations().get(repository.getmAllVacations().size() - 1).getVacationID() + 1;

                        // ADD HOTEL NOTE TO CONSTRUCTOR
                        vacation = new Vacation(productID, editName.getText().toString(),
                                Double.parseDouble(editPrice.getText().toString()),
                                editHotel.getText().toString(),
                                startDateString,
                                endDateString);


                        repository.insert(vacation);
                        currentVacation = vacation;
                        this.finish();
                    } else {
                        // ADD HOTEL NOTE TO CONSTRUCTOR
                        vacation = new Vacation(productID, editName.getText().toString(),
                                Double.parseDouble(editPrice.getText().toString()),
                                editHotel.getText().toString(),
                                startDateString,
                                endDateString);

                        repository.update(vacation);
                        currentVacation = vacation;
                        this.finish();
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
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
            sendIntent.putExtra(Intent.EXTRA_TEXT, "These are the vacation details: vacationID: " +
                    (currentVacation != null ? currentVacation.getVacationID() : "") +
                    ", the name of our vacation: " + editName.getText().toString() +
                    ", price: $" + Double.parseDouble(editPrice.getText().toString()) +
                    ", this is our start date: " + editStartVacaDate.getText().toString() +
                    ", this is our end date: " + editEndVacaDate.getText().toString() +
                    ", associated excursions: " + excursionDetails + " Let us know what you think!");
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
            return true;
        }

        if (item.getItemId() == R.id.notify) {
            String dateFromScreen = editDate.getText().toString();
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (myDate != null) {
                Long trigger = myDate.getTime();
                Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
                intent.putExtra("key","message I want to see");
                PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            }
            return true;
        }
        return true;
    }
}