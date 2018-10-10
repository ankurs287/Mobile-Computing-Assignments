package com.ankur.a3_2016225.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ankur.a3_2016225.R;
import com.ankur.a3_2016225.SQLiteDBHelper;
import com.ankur.a3_2016225.model.Question;
import com.ankur.a3_2016225.utils.RecyclerItemClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ListFragment extends Fragment {

    private static final String TAG = ListFragment.class.getName();

    @BindView(R.id.rv_qlist)
    RecyclerView rvQlist;
    Unbinder unbinder;

    private QuestionAdapter mQuestionAdapter;
    private SQLiteDBHelper dbHelper = null;
    private ArrayList<Question> mQuestionArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rvQlist.setLayoutManager(llm);
        rvQlist.setHasFixedSize(true);
        mQuestionAdapter = new QuestionAdapter();
        rvQlist.setAdapter(mQuestionAdapter);

        dbHelper = new SQLiteDBHelper(getContext());
        mQuestionArrayList = dbHelper.getAllQuestions();
        mQuestionAdapter.setData(mQuestionArrayList);

        rvQlist.addOnItemTouchListener(new RecyclerItemClickListener(getContext(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View childView, int position) {
                        DetailFragment detailFragment = DetailFragment.newInstance(
                                mQuestionArrayList.get(position).getqId());

                        if (getActivity() instanceof HomeActivity) {
                            ((HomeActivity) getActivity()).addFragment(detailFragment);
                        }
                    }

                    @Override
                    public void onItemLongPress(View childView, int position) {

                    }
                }));

        return rootView;
    }

    @OnClick(R.id.btn_submit)
    public void onBtnSubmitClicked() {
        Toast.makeText(getContext(), R.string.submit_answer_message, Toast.LENGTH_SHORT).show();
        dbHelper.exportToCsv();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
