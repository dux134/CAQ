package in.app.currentaffairsquiz;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

public class CurrentAffairAdapter extends RecyclerView.Adapter<CurrentAffairAdapter.ViewHolder> {
    private CurrentAffairAdapter.RecyclerClickListener listener;
    private ArrayList<CurrentAffairModel> list;
    private Context context;

    public CurrentAffairAdapter(RecyclerClickListener listener, ArrayList<CurrentAffairModel> list, Context context) {
        this.listener = listener;
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_current_affair_quiz,parent,false);
        ViewHolder holder = new ViewHolder(view, listener);
        return  holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.question.setText(list.get(position).getQuestions());
        holder.date.setText(list.get(position).getDate());
        holder.title.setText(list.get(position).getTitle());

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color = generator.getColor(list.get(position).getDate());
        //int color = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(list.get(position).getDate().substring(0,2), color); // radius in px

        holder.imageView.setImageDrawable(drawable);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface RecyclerClickListener {
        void onClick(View view, int adapterPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title,date,question;
        private ImageView imageView;
        private RecyclerClickListener lv;

        public ViewHolder(View itemView, RecyclerClickListener listener) {
            super(itemView);
            title = itemView.findViewById(R.id.item_current_affair_title);
            date = itemView.findViewById(R.id.item_current_affair_date);
            question = itemView.findViewById(R.id.item_current_affair_question);
            imageView = itemView.findViewById(R.id.item_current_affair_image);

            lv = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            lv.onClick(view,getAdapterPosition());
        }
    }
}
