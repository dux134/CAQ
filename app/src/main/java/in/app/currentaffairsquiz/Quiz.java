package in.app.currentaffairsquiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Quiz extends AppCompatActivity {
    public static ArrayList<QuizActivityModel> list = new ArrayList<>();

    private AdView ad1,ad2;
    private TextView time,attempted,question;
    private RadioButton optionA,optionB,optionC,optionD;
    private int pos;

    public static int timer = 1;
    public static int minutes;
    private InterstitialAd interstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        ImageView back = findViewById(R.id.quiz_activity_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        time = findViewById(R.id.current_textview);
        attempted = findViewById(R.id.attempted_textView);
        question = findViewById(R.id.question);
        optionA = findViewById(R.id.radioButtonA);
        optionB = findViewById(R.id.radioButtonB);
        optionC = findViewById(R.id.radioButtonC);
        optionD = findViewById(R.id.radioButtonD);

        pos = 0;
        int no = pos + 1;
        question.setText(no+". "+list.get(pos).getQuestion());
        optionA.setText(" A. "+list.get(pos).getA());
        optionB.setText(" B. "+list.get(pos).getB());
        optionC.setText(" C. "+list.get(pos).getC());
        optionD.setText(" D. "+list.get(pos).getD());

        final int timeLimit = minutes *60*1000;
        CountDownTimer newtimer = new CountDownTimer(timeLimit, 1000) {

            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished/1000;
                long minute = seconds/60;
                long second = seconds - (minute*60);
                if(second < 10)
                    time.setText("Auto submit in : " + minute+":0"+second);
                else
                    time.setText("Auto submit in : " + minute+":"+second);


                if((minutes*60)/20>seconds)
                    time.setTextColor(Color.RED);
            }
            public void onFinish() {
                if(timer == 1)
                    submitAction();
            }
        };
        newtimer.start();
        attempted.setText("Attemped : 0/"+list.size());

        optionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (optionA.isChecked()) {
                    optionA.setChecked(true);
                    optionB.setChecked(false);
                    optionC.setChecked(false);
                    optionD.setChecked(false);
                }else
                    optionA.setChecked(false);
            }
        });
        optionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (optionB.isChecked()) {
                    optionA.setChecked(false);
                    optionB.setChecked(true);
                    optionC.setChecked(false);
                    optionD.setChecked(false);
                }else
                    optionB.setChecked(false);
            }
        });
        optionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (optionC.isChecked()) {
                    optionA.setChecked(false);
                    optionB.setChecked(false);
                    optionC.setChecked(true);
                    optionD.setChecked(false);
                }else
                    optionC.setChecked(false);
            }
        });
        optionD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (optionD.isChecked()) {
                    optionA.setChecked(false);
                    optionB.setChecked(false);
                    optionC.setChecked(false);
                    optionD.setChecked(true);
                } else
                    optionD.setChecked(false);
            }
        });


//        MobileAds.initialize(this,"ca-app-pub-3940256099942544/6300978111");

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-6172703407759908/7383869458");
        interstitialAd.loadAd(new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("478CF15AB391B1B05694E4D2BADE6ACE")
                .build());
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                submitAction();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Toast.makeText(getApplicationContext(),"loaded",Toast.LENGTH_LONG).show();
            }
        });

        CardView submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timer == 1)
                    timer = 0;

                final AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(Quiz.this, R.style.Theme_AppCompat_DayNight_Dialog);
                builder.setCancelable(false);
                builder
                        .setMessage("Are you sure you want to submit?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                showInterstitial();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        CardView previous = findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pos>0) {
                    updateAnswer(pos);
                    pos--;
                    int no = pos + 1;
                    question.setText(no+". "+list.get(pos).getQuestion());
                    optionA.setText(" A. "+list.get(pos).getA());
                    optionB.setText(" B. "+list.get(pos).getB());
                    optionC.setText(" C. "+list.get(pos).getC());
                    optionD.setText(" D. "+list.get(pos).getD());
                    updateRadiobutton(list.get(pos).getSelectedAnswer());
                    attempted.setText("Attemped : "+noOfAttemptedQuestion()+"/"+list.size());
                }
            }
        });
        CardView next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pos<(list.size()-1)) {
                    updateAnswer(pos);
                    pos++;
                    int no = pos + 1;
                    question.setText(no+". "+list.get(pos).getQuestion());
                    optionA.setText(" A. "+list.get(pos).getA());
                    optionB.setText(" B. "+list.get(pos).getB());
                    optionC.setText(" C. "+list.get(pos).getC());
                    optionD.setText(" D. "+list.get(pos).getD());
                    updateRadiobutton(list.get(pos).getSelectedAnswer());

                    attempted.setText("Attemped : "+noOfAttemptedQuestion()+"/"+list.size());
                }
            }
        });

        ad1 = findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
//                .addTestDevice("478CF15AB391B1B05694E4D2BADE6ACE")  // My Galaxy M20 test phone
                .build();
        ad1.loadAd(request);
    }

    private void submitAction() {
        updateAnswer(pos);

        startActivity(new Intent(Quiz.this,QuizResult.class));
        finish();
    }

    private void updateRadiobutton(String selectedAnswer) {
        if(selectedAnswer.equals("A"))
            optionA.setChecked(true);
        if(selectedAnswer.equals("B"))
            optionB.setChecked(true);
        if(selectedAnswer.equals("C"))
            optionC.setChecked(true);
        if(selectedAnswer.equals("D"))
            optionD.setChecked(true);
    }

    private void updateAnswer(int pos) {
        list.get(pos).setSelectedAnswer(getAnswerSelected()+"");
        resetRadioButton();
    }

    private char getAnswerSelected() {
        if(optionA.isChecked())
            return 'A';
        if(optionB.isChecked())
            return 'B';
        if(optionC.isChecked())
            return 'C';
        if (optionD.isChecked())
            return 'D';
        return 'N';
    }

    private void resetRadioButton() {
        optionA.setChecked(false);
        optionB.setChecked(false);
        optionC.setChecked(false);
        optionD.setChecked(false);
    }

    private String noOfAttemptedQuestion() {
        int no = 0;
        for (QuizActivityModel lst:list) {
            if(!lst.getSelectedAnswer().equals("N"))
                no++;
        }
        return no+"";
    }



    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(Quiz.this, R.style.Theme_AppCompat_DayNight_Dialog);
        builder.setCancelable(false);
        builder
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Quiz.super.onBackPressed();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and restart the game.
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            Toast.makeText(this, "Well done!", Toast.LENGTH_SHORT).show();
            submitAction();
        }
    }
}
