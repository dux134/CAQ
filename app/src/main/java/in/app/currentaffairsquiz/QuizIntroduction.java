package in.app.currentaffairsquiz;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuizIntroduction extends AppCompatActivity {
    private TextView time,questions,title,date;
    public static CurrentAffairModel model;
    public static String month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_introduction);

        loadQuestionsFromFirebase();

        time = findViewById(R.id.intro_time_limit);
        questions = findViewById(R.id.intro_questions);
        title = findViewById(R.id.intro_title);
        date = findViewById(R.id.intro_date);

        title.setText(model.getTitle());
        date.setText(model.getDate());
        questions.setText(model.getQuestions());
        time.setText(model.getTime()+" Minutes");

        CardView startWithAutoSubmit = findViewById(R.id.intro_autosubmit);
        startWithAutoSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Quiz.timer = 1;
                Quiz.minutes = model.getTime();
                startActivity(new Intent(QuizIntroduction.this,Quiz.class));
                finish();
            }
        });
        CardView startWithoutAutoSubmit = findViewById(R.id.intro_not_autosubmit);
        startWithoutAutoSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Quiz.timer = 0;
                Quiz.minutes = model.getTime();
                startActivity(new Intent(QuizIntroduction.this,Quiz.class));
                finish();
            }
        });

        ImageView back = findViewById(R.id.intro_back_image);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        AdView ad = findViewById(R.id.adView2);
        AdRequest adRequest3 = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
//                .addTestDevice("478CF15AB391B1B05694E4D2BADE6ACE")  // My Galaxy M20 test phone
                .build();
        ad.loadAd(adRequest3);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void loadQuestionsFromFirebase() {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        Quiz.list.clear();
        String d = model.getDate().substring(0,2);
//        Toast.makeText(getApplicationContext(),d +" "+month,Toast.LENGTH_LONG).show();
        mDatabase.child("quiz").child("quiz_questions").child(month).child(d).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    QuizActivityModel question = (QuizActivityModel) postSnapshot.getValue(QuizActivityModel.class);
                    Quiz.list.add(question);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
