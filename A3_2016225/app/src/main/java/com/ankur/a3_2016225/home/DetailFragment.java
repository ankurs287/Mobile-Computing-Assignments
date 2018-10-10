package com.ankur.a3_2016225.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ankur.a3_2016225.R;
import com.ankur.a3_2016225.SQLiteDBHelper;
import com.ankur.a3_2016225.model.Question;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DetailFragment extends Fragment {

    @BindView(R.id.tb_detail)
    Toolbar tbDetail;
    @BindView(R.id.tv_question_body)
    TextView tvQuestionBody;
    @BindView(R.id.rb_true)
    RadioButton rbTrue;
    @BindView(R.id.rb_false)
    RadioButton rbFalse;
    Unbinder unbinder;
    @BindView(R.id.rg_option)
    RadioGroup rgOption;

    private SQLiteDBHelper dbHelper = null;
    private Question question;

    public static DetailFragment newInstance(int qId) {
        Bundle args = new Bundle();
        args.putInt(SQLiteDBHelper.KEY_QUESTION_ID, qId);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        Bundle bundle = getArguments();
        if (bundle != null) {
            int qId = bundle.getInt(SQLiteDBHelper.KEY_QUESTION_ID);
            dbHelper = new SQLiteDBHelper(getContext());
            question = dbHelper.getQuestion(qId);
            tvQuestionBody.setText(question.getQuestionBody());
            tbDetail.setTitle("Q" + qId);
            int sa = question.getSelectedAnswer();
            if (sa == 1) {
                rbTrue.setChecked(true);
            } else if (sa == 0) {
                rbFalse.setChecked(true);
            }
        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_save)
    public void onBtnSaveClicked() {
        if (rgOption.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getContext(), R.string.please_select_an_option,
                    Toast.LENGTH_SHORT).show();
        } else {
            int selectedId = rgOption.getCheckedRadioButtonId();
            question.setSelectedAnswer((selectedId == R.id.rb_true) ? 1 : 0);
            dbHelper.updateQuestion(question);
            Toast.makeText(getContext(), R.string.answer_saved, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_submit)
    public void onBtnSubmitClicked() {
//        if (rgOption.getCheckedRadioButtonId() == -1) {
//        } else {
//            int selectedId = rgOption.getCheckedRadioButtonId();
//            question.setSelectedAnswer((selectedId == R.id.rb_true) ? 1 : 0);
//            dbHelper.updateQuestion(question);
//        }
        Toast.makeText(getContext(), R.string.submit_answer_message, Toast.LENGTH_SHORT).show();
        dbHelper.exportToCsv();
    }
}
