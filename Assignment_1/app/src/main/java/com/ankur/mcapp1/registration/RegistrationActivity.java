package com.ankur.mcapp1.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.ankur.mcapp1.R;
import com.ankur.mcapp1.details.DetailsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";

    @BindView(R.id.et_full_name)
    EditText etFullName;
    @BindView(R.id.et_rollnumber)
    EditText etRollNumber;
    @BindView(R.id.et_branch)
    EditText etBranch;
    @BindView(R.id.et_course_1)
    EditText etCourse1;
    @BindView(R.id.et_course_2)
    EditText etCourse2;
    @BindView(R.id.et_course_3)
    EditText etCourse3;
    @BindView(R.id.et_course_4)
    EditText etCourse4;

    private boolean paused = false;
    private boolean stopped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        setTitle(getString(R.string.registration_form));

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

    @OnClick(R.id.btn_submit)
    public void onBtnSubmitClicked() {

        String fullName = etFullName.getText().toString().trim();
        String rollNumber = etRollNumber.getText().toString().trim();
        String branch = etBranch.getText().toString().trim();
        String firstCourse = etCourse1.getText().toString().trim();
        String secondCourse = etCourse2.getText().toString().trim();
        String thirdCourse = etCourse3.getText().toString().trim();
        String fourthCourse = etCourse4.getText().toString().trim();

        if (fullName.isEmpty() || rollNumber.isEmpty() || branch.isEmpty() || firstCourse.isEmpty()
                || secondCourse.isEmpty() || thirdCourse.isEmpty() || fourthCourse.isEmpty()) {
            Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(RegistrationActivity.this, DetailsActivity.class);
        intent.putExtra(getString(R.string.full_name), fullName);
        intent.putExtra(getString(R.string.roll_number), rollNumber);
        intent.putExtra(getString(R.string.branch), branch);
        intent.putExtra(getString(R.string.first_course), firstCourse);
        intent.putExtra(getString(R.string.second_course), secondCourse);
        intent.putExtra(getString(R.string.third_course), thirdCourse);
        intent.putExtra(getString(R.string.fourth_course), fourthCourse);
        startActivity(intent);
    }

    @OnClick(R.id.btn_clear)
    public void onBtnClearClicked() {
        etFullName.setText("");
        etRollNumber.setText("");
        etBranch.setText("");
        etCourse1.setText("");
        etCourse2.setText("");
        etCourse3.setText("");
        etCourse4.setText("");
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
