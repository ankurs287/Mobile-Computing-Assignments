package com.ankur.a3_2016225.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ankur.a3_2016225.R;
import com.ankur.a3_2016225.model.Question;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private ArrayList<Question> mQuestions;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_question, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        Question question = mQuestions.get(i);
        holder.tvQid.setText("Q" + (i + 1));
        holder.tvQuestionBody.setText(question.getQuestionBody());
    }

    @Override
    public int getItemCount() {
        if (mQuestions == null) {
            return 0;
        } else {
            return mQuestions.size();
        }
    }

    public void setData(ArrayList<Question> questions) {
        mQuestions = questions;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_question_body)
        TextView tvQuestionBody;
        @BindView(R.id.tv_qid)
        TextView tvQid;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
