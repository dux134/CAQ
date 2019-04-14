package in.app.currentaffairsquiz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class QuizResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        TextView totalAttempted,totalUnattempted,totalCorrect,totalWrong,attemptedCorrect,attemptedWrong,excellent,score;

        ImageView back = findViewById(R.id.quiz_result_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        totalAttempted = findViewById(R.id.result_total_attempted);
        totalUnattempted = findViewById(R.id.result_total_unattempted);
        totalCorrect = findViewById(R.id.result_total_correct);
        totalWrong = findViewById(R.id.result_total_wrong);
        attemptedCorrect = findViewById(R.id.result_attempted_correct);
        attemptedWrong = findViewById(R.id.result_attempted_wrong);
        excellent = findViewById(R.id.result_excellent);
        score = findViewById(R.id.result_score);

        ArrayList<QuizActivityModel> list = Quiz.list;
        int total = list.size();
        int attempted = 0;
        int correct = 0;
        int unattempted = 0;

        for(QuizActivityModel l:list) {
            if(!l.getSelectedAnswer().equalsIgnoreCase("N"))
                attempted++;

            if(l.getSelectedAnswer().equalsIgnoreCase(l.getAnswer()))
                correct++;
        }
        unattempted = total - attempted;
        int wrong = attempted - correct;

        totalAttempted.setText(setTextview(attempted,total));
        totalUnattempted.setText(setTextview(unattempted,total));
        totalCorrect.setText(setTextview(correct,total));
        totalWrong.setText(setTextview(wrong,total));
        attemptedCorrect.setText(setTextview(correct,attempted));
        attemptedWrong.setText(setTextview(wrong,attempted));

        int realScore;
        if (correct == 0)
            realScore = 0;
        else
            realScore = correct*100/total;

        if(realScore > 80) {
            excellent.setText("Excellent");
            excellent.setTextColor(Color.GREEN);
        } else if (realScore > 60) {
            excellent.setTextColor(Color.BLUE);
            excellent.setText("Good! Well done");
        } else if (realScore > 35) {
            excellent.setTextColor(Color.DKGRAY);
            excellent.setText("Average");
        } else {
            excellent.setTextColor(Color.RED);
            excellent.setText("Below average");
        }

        score.setText("Your score is : "+realScore+"%");

        CardView answerKey = findViewById(R.id.result_answer_key);
        answerKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuizResult.this,AnswerKey.class));
            }
        });

        AdView ad1 = findViewById(R.id.adView6);
        AdRequest request = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
//                .addTestDevice("478CF15AB391B1B05694E4D2BADE6ACE")  // My Galaxy M20 test phone
                .build();
        ad1.loadAd(request);
    }

    private String setTextview(int x,int y) {
        if (x == y && x != 0)
            return x + "/" + y + "    " + (x * 100 / y) + "%";
        else if (x == 0)
            return x + "/" + y + "        0%";
        else
            return x + "/" + y + "      " + (x * 100 / y) + "%";
    }
}
