package in.app.currentaffairsquiz;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class AnswerKeyAdapter extends RecyclerView.Adapter<AnswerKeyAdapter.ViewHolder> {
    private ArrayList<QuizActivityModel> list;
    private Context context;

    public AnswerKeyAdapter(ArrayList<QuizActivityModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AnswerKeyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_answer_key, parent, false);
        AnswerKeyAdapter.ViewHolder holder = new AnswerKeyAdapter.ViewHolder(v);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerKeyAdapter.ViewHolder holder, int position) {
        int no = position + 1;
        holder.question.setText(no+". "+list.get(position).getQuestion());
        holder.optionA.setText(" A. "+list.get(position).getA());
        holder.optionB.setText(" B. "+list.get(position).getB());
        holder.optionC.setText(" C. "+list.get(position).getC());
        holder.optionD.setText(" D. "+list.get(position).getD());

        String selectedAnswer = list.get(position).getAnswer();
        if(selectedAnswer.equalsIgnoreCase("A"))
            holder.optionA.setChecked(true);
        else if(selectedAnswer.equalsIgnoreCase("B"))
            holder.optionB.setChecked(true);
        else if(selectedAnswer.equalsIgnoreCase("C"))
            holder.optionC.setChecked(true);
        else if(selectedAnswer.equalsIgnoreCase("D"))
            holder.optionD.setChecked(true);


        if(selectedAnswer.equalsIgnoreCase(list.get(position).getSelectedAnswer())) {
            holder.resutSubmited.setText("Correct : your answer - " + list.get(position).getSelectedAnswer());
            holder.resutSubmited.setTextColor(Color.GREEN);
        } else if(list.get(position).getSelectedAnswer().equalsIgnoreCase("N")) {
            holder.resutSubmited.setText("Not attempted");
            holder.resutSubmited.setTextColor(Color.GRAY);
        } else if(!selectedAnswer.equalsIgnoreCase(list.get(position).getSelectedAnswer())) {
            holder.resutSubmited.setText("Wrong : your answer - " + list.get(position).getSelectedAnswer());
            holder.resutSubmited.setTextColor(Color.RED);
        }


        holder.optionA.setEnabled(false);
        holder.optionB.setEnabled(false);
        holder.optionC.setEnabled(false);
        holder.optionD.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView question,resutSubmited;
        private RadioButton optionA,optionB,optionC,optionD;
        public ViewHolder(View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.result_question);
            resutSubmited = itemView.findViewById(R.id.result_answer_marked);
            optionA = itemView.findViewById(R.id.result_radioButtonA);
            optionB = itemView.findViewById(R.id.result_radioButtonB);
            optionC = itemView.findViewById(R.id.result_radioButtonC);
            optionD = itemView.findViewById(R.id.result_radioButtonD);
        }
    }
}

