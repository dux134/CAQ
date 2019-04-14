package in.app.currentaffairsquiz;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

import static android.support.constraint.Constraints.TAG;

public class Dashboard extends AppCompatActivity {
    public ViewPager viewpager;
    private ArrayList<SliderDataModel> sliderImagesUrlList = new ArrayList<>();;
    private SliderViewPager sliderViewPager;
    private FirebaseFirestore db ;

    private CircleIndicator indicator;
    private TextView month1,month2,month3;

    private RecyclerView currentAffairsRecyclerView1,currentAffairsRecyclerView2,currentAffairsRecyclerView3;
    private RecyclerView.Adapter currentAffairsAdapter1,currentAffairsAdapter2,currentAffairsAdapter3;
    private ArrayList<CurrentAffairModel> currentAffairsList1 = new ArrayList<>();
    private ArrayList<CurrentAffairModel> currentAffairsList2 = new ArrayList<>();
    private ArrayList<CurrentAffairModel> currentAffairsList3 = new ArrayList<>();

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 1000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 7000;
    private final String[] monthList = {"January","February","March","April","May","June","July","August","September","October","November","December"};

    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        loadImageInSlider();
        setTextviewMonth();
        loadQuizListFirebase();

        currentAffairsRecyclerView1 = findViewById(R.id.current_affairs_recycler);
        currentAffairsRecyclerView1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        currentAffairsAdapter1 = new CurrentAffairAdapter(new CurrentAffairAdapter.RecyclerClickListener() {
            @Override
            public void onClick(View view, int adapterPosition) {
                QuizIntroduction.month  = month1.getText().toString().toLowerCase();
                QuizIntroduction.model = currentAffairsList1.get(adapterPosition);
                startActivity(new Intent(Dashboard.this,QuizIntroduction.class));
            }
        },currentAffairsList1,Dashboard.this);
        currentAffairsRecyclerView1.setAdapter(currentAffairsAdapter1);
        currentAffairsRecyclerView1.setItemAnimator(new DefaultItemAnimator());

        currentAffairsRecyclerView2 = findViewById(R.id.current_affairs_recycler2);
        currentAffairsRecyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        currentAffairsAdapter2 = new CurrentAffairAdapter(new CurrentAffairAdapter.RecyclerClickListener() {
            @Override
            public void onClick(View view, int adapterPosition) {
                QuizIntroduction.month  = month2.getText().toString().toLowerCase();
                QuizIntroduction.model = currentAffairsList2.get(adapterPosition);
                startActivity(new Intent(Dashboard.this,QuizIntroduction.class));
            }
        },currentAffairsList2,Dashboard.this);
        currentAffairsRecyclerView2.setAdapter(currentAffairsAdapter2);
        currentAffairsRecyclerView2.setItemAnimator(new DefaultItemAnimator());

        currentAffairsRecyclerView3 = findViewById(R.id.current_affairs_recycler3);
        currentAffairsRecyclerView3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        currentAffairsAdapter3 = new CurrentAffairAdapter(new CurrentAffairAdapter.RecyclerClickListener() {
            @Override
            public void onClick(View view, int adapterPosition) {
                QuizIntroduction.month  = month3.getText().toString().toLowerCase();
                QuizIntroduction.model = currentAffairsList3.get(adapterPosition);
                startActivity(new Intent(Dashboard.this,QuizIntroduction.class));
            }
        },currentAffairsList3,Dashboard.this);
        currentAffairsRecyclerView3.setAdapter(currentAffairsAdapter3);
        currentAffairsRecyclerView3.setItemAnimator(new DefaultItemAnimator());

        PublisherAdView ad1,ad2,ad3;
        ad1 = findViewById(R.id.adView3);
        ad2 = findViewById(R.id.adView4);
        ad3 = findViewById(R.id.adView5);

        MobileAds.initialize(this,
                "ca-app-pub-6172703407759908~4635447455");


        PublisherAdRequest request = new PublisherAdRequest.Builder().build();
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
//                .addTestDevice("478CF15AB391B1B05694E4D2BADE6ACE")  // My Galaxy M20 test phone
        ad1.loadAd(request);

        PublisherAdRequest adRequest2 = new PublisherAdRequest.Builder().build();
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build(); // All emulators
//                .addTestDevice("478CF15AB391B1B05694E4D2BADE6ACE")  // My Galaxy M20 test phone
        ad2.loadAd(adRequest2);

        PublisherAdRequest adRequest3 = new PublisherAdRequest.Builder().build();
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();  // All emulators
//                .addTestDevice("478CF15AB391B1B05694E4D2BADE6ACE").build();  // My Galaxy M20 test phone
        ad3.loadAd(adRequest3);
    }

    private void setTextviewMonth() {

        month1 = findViewById(R.id.month1);
        month2 = findViewById(R.id.month2);
        month3 = findViewById(R.id.month3);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);

        if(month == 0){
            month1.setText(monthList[month]+" "+year);
            month2.setText(monthList[11]+" "+(year-1));
            month3.setText(monthList[10]+" "+(year-1));
        } else if (month == 1) {
            month1.setText(monthList[month]+" "+year);
            month2.setText(monthList[month-1]+" "+(year));
            month3.setText(monthList[11]+" "+(year-1));
        } else {
            month1.setText(monthList[month]+" "+year);
            month2.setText(monthList[month-1]+" "+(year));
            month3.setText(monthList[month-2]+" "+(year));
        }
        month1.setText(monthList[month]+" "+year);

    }

    private void loadImageInSlider() {


        viewpager = (ViewPager) findViewById(R.id.viewpager);
        sliderViewPager = new SliderViewPager(viewpager,Dashboard.this, sliderImagesUrlList);
        viewpager.setAdapter(sliderViewPager);

        indicator = (CircleIndicator)
                findViewById(R.id.indicator);

        viewpager.setCurrentItem(0);
        indicator.setViewPager(viewpager);


        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == sliderImagesUrlList.size()) {
                    currentPage = 0;
                }
                viewpager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        loadListFromFirestore();

    }

    private void loadListFromFirestore() {
        final FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        db.collection("image_slider")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen error", e);
                            return;
                        }


                        assert querySnapshot != null;
                        int count = 0;
                        for (DocumentChange change : querySnapshot.getDocumentChanges()) {
                            if (change.getType() == DocumentChange.Type.ADDED) {

                                String url = change.getDocument().get("image_url").toString();
                                String link = change.getDocument().get("link").toString();
                                Integer priority = Integer.valueOf(change.getDocument().get("priority").toString());

                                sliderImagesUrlList.add(new SliderDataModel(url,link));
                                sliderViewPager.notifyDataSetChanged();
                                indicator.setViewPager(viewpager);
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d(TAG, "Data fetched from " + source);
                        }
                    }
                });
    }

    private void loadQuizListFirebase() {
//
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

//        FirebaseDatabase.getInstance().goOffline();
        currentAffairsList1.clear();
        currentAffairsList2.clear();
        currentAffairsList3.clear();

        mDatabase.child("quiz").child("details").child(month1.getText().toString().toLowerCase()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    CurrentAffairModel question = postSnapshot.getValue(CurrentAffairModel.class);
                    currentAffairsList1.add(question);
                    currentAffairsAdapter1.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("quiz").child("details").child(month2.getText().toString().toLowerCase()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    CurrentAffairModel question = postSnapshot.getValue(CurrentAffairModel.class);
                    currentAffairsList2.add(question);
                    currentAffairsAdapter2.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("quiz").child("details").child(month3.getText().toString().toLowerCase()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    CurrentAffairModel question = postSnapshot.getValue(CurrentAffairModel.class);
                    currentAffairsList3.add(question);
                    currentAffairsAdapter3.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
