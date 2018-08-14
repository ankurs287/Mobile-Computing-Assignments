package com.ankur.mcapp1.details;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ankur.mcapp1.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "DetailsActivity";

    @BindView(R.id.tv_full_name)
    TextView tvFullName;
    @BindView(R.id.tv_rollnumber)
    TextView tvRollnumber;
    @BindView(R.id.tv_branch)
    TextView tvBranch;
    @BindView(R.id.tv_course_1)
    TextView tvCourse1;
    @BindView(R.id.tv_course_2)
    TextView tvCourse2;
    @BindView(R.id.tv_course_3)
    TextView tvCourse3;
    @BindView(R.id.tv_course_4)
    TextView tvCourse4;

    private boolean paused = false;
    private boolean stopped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        setTitle(getString(R.string.registration_details));

        String fullName = getIntent().getStringExtra(getString(R.string.full_name));
        String rollNumber = getIntent().getStringExtra(getString(R.string.roll_number));
        String branch = getIntent().getStringExtra(getString(R.string.branch));
        String firstCourse = getIntent().getStringExtra(getString(R.string.first_course));
        String secondCourse = getIntent().getStringExtra(getString(R.string.second_course));
        String thirdCourse = getIntent().getStringExtra(getString(R.string.third_course));
        String fourthCourse = getIntent().getStringExtra(getString(R.string.fourth_course));

        tvFullName.setText(fullName);
        tvRollnumber.setText(rollNumber);
        tvBranch.setText(branch);
        tvCourse1.setText(firstCourse);
        tvCourse2.setText(secondCourse);
        tvCourse3.setText(thirdCourse);
        tvCourse4.setText(fourthCourse);

        if (stopped) {
            showActivityTrasition(getString(R.string.on_stop), getString(R.string.on_create));
        } else if (paused) {
            showActivityTrasition(getString(R.string.on_pause), getString(R.string.on_create));
        } else {
            showActivityTrasition(getString(R.string.activity_launched),
                    getString(R.string.on_create));
        }
        paused = false;
        stopped = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (paused) {
            showActivityTrasition(getString(R.string.on_restart), getString(R.string.on_start));
        } else {
            showActivityTrasition(getString(R.string.on_create), getString(R.string.on_start));
        }
        paused = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paused) {
            showActivityTrasition(getString(R.string.on_pause), getString(R.string.on_resume));
        } else {
            showActivityTrasition(getString(R.string.on_start), getString(R.string.on_resume));
        }
        paused = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        showActivityTrasition(getString(R.string.on_resume), getString(R.string.on_pause));
        paused = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        showActivityTrasition(getString(R.string.on_pause), getString(R.string.on_stop));
        stopped = true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showActivityTrasition(getString(R.string.on_stop), getString(R.string.on_restart));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showActivityTrasition(getString(R.string.on_stop), getString(R.string.on_destroy));
    }

    private void showActivityTrasition(String state1, String state2) {
        Log.i(TAG, "State of activity " + TAG + " changed from " + state1 + " to " + state2);
        Toast.makeText(this,
                "State of activity " + TAG + " changed from " + state1 + " to " + state2,
                Toast.LENGTH_SHORT).show();
    }
}
