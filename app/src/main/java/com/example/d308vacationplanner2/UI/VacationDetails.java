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
import java.time.LocalDate;
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
    static int notificationID;

    String hotel;
    double price;
    int productID;

    int numExcursions;
    int vacationID;
    EditText editName;
    EditText editPrice;

    EditText editHotel;

    Repository repository;
    Vacation currentVacation;
    TextView editStartVacaDate;
    TextView editEndVacaDate;
    TextView editDate;

    String setStartDate;
    String setEndDate;

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
        setStartDate = getIntent().getStringExtra("startdate");
        setEndDate = getIntent().getStringExtra("enddate");
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

        if (setStartDate != null) {
            try {
                Date startDate = sdf.parse(setStartDate);
                Date endDate = sdf.parse(setEndDate);
                myCalendarStart.setTime(startDate);
                myCalendarEnd.setTime(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

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

    private void scheduleAlarm(AlarmManager alarmManager, long triggerTime, String message, int notificationId) {
        Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
        intent.putExtra("key", message);
        intent.putExtra("notification_id", notificationId);
        PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, notificationId, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, sender);
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
                        else
                            productID = repository.getmAllVacations().get(repository.getmAllVacations().size() - 1).getVacationID() + 1;

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
        if (item.getItemId() == R.id.vacationdelete) {
            for (Vacation vac : repository.getmAllVacations()) {
                if (vac.getVacationID() == vacationID) currentVacation = vac;
            }
            numExcursions = 0;
            for (Excursion excursion : repository.getAllParts()) {
                if (excursion.getExcursionID() == productID) {
                    ++numExcursions;
                }
            }
            //if the vacation has any associated excursions, prevent deletion of the vacation, otherwise delete it
            if (numExcursions == 0) {
                repository.delete(currentVacation);
                Toast.makeText(VacationDetails.this, currentVacation.getVacationName() + " was deleted", Toast.LENGTH_LONG).show();
                VacationDetails.this.finish();
            } else {
                Toast.makeText(VacationDetails.this, "Can't delete a vacation with excursions", Toast.LENGTH_LONG).show();
            }
        }

        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion p : repository.getAllParts()) {
            if (p.getVacationID() == productID) filteredExcursions.add(p);
        }
        StringBuilder excursionDetails = new StringBuilder();
        for (Excursion p : filteredExcursions) {
            excursionDetails.append("Excursion Name: ")
                    .append(p.getExcursionName())

                    // .append(", Price: $")
                    //.append(p.getPrice())


                    .append("\n");
        }

        if (item.getItemId() == R.id.share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "These are the vacation details, vacation name: " +
                    editName.getText().toString() +
                    ", this is our start date: " + editStartVacaDate.getText().toString() +
                    ", this is our end date: " + editEndVacaDate.getText().toString() +
                    ", associated excursions: " + excursionDetails + " Let us know what you think!");
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
            return true;
        }


        if (item.getItemId() == R.id.notify) {
            String startdate = editStartVacaDate.getText().toString();
            String enddate = editEndVacaDate.getText().toString();
            String dateFromScreen = editDate.getText().toString();
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            Date myStartDate = null;
            Date myEndDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
                myStartDate = sdf.parse(startdate);
                myEndDate = sdf.parse(enddate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                // Schedule start date notification
                scheduleAlarm(alarmManager, myStartDate.getTime(), "Vacation Start: " + name, notificationID++);

                // Schedule end date notification
                scheduleAlarm(alarmManager, myEndDate.getTime(), "Vacation End: " + name, notificationID++);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

                //
            /*
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
*/