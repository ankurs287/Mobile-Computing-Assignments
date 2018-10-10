package com.ankur.a3_2016225;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.ankur.a3_2016225.model.Question;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SQLiteDBHelper extends SQLiteOpenHelper {

    private String TAG = SQLiteDBHelper.class.getName();

    private Context mContext;

    public static final String DATA_FILE = "questions_data.json";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Quiz";
    public static final String TABLE_NAME = "questions";
    public static final String KEY_QUESTION_ID = "q_id";
    public static final String KEY_QUESTION_BODY = "q_body";
    public static final String KEY_ANSWER = "answer";
    public static final String KEY_SELECTED_OPTION = "selected_option";

    public ProgressDialog mProgressDialog;

    //Create Table Query
    private static final String SQL_CREATE_TABLE_QUESTIONS =
            "CREATE TABLE " + TABLE_NAME + " (" + KEY_QUESTION_ID
                    + "  INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_QUESTION_BODY + " TEXT, " + KEY_ANSWER + "  INTEGER, "
                    + KEY_SELECTED_OPTION + " INTEGER DEFAULT -1);";

    public SQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        mProgressDialog = new ProgressDialog(mContext);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
        db.execSQL(SQL_CREATE_TABLE_QUESTIONS);
        populateTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d(TAG, "onUpgrade");
    }

    private void populateTable(SQLiteDatabase db) {
        // Gets the data repository in write mode

        try {
            InputStream is = mContext.getAssets().open(DATA_FILE);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            JSONObject jsonObject = new JSONObject(new String(buffer, "UTF-8"));


            JSONArray questionsArray = jsonObject.getJSONArray("questions");
            for (int i = 0; i < questionsArray.length(); i++) {
                JSONObject questionObject = questionsArray.getJSONObject(i);

                Question question = new Question();
                question.setQuestionBody(questionObject.getString("body"));
                question.setAnswer(questionObject.getBoolean("answer"));

                ContentValues question_details = new ContentValues();
                question_details.put(KEY_QUESTION_BODY, question.getQuestionBody());
                question_details.put(KEY_ANSWER, (question.getAnswer()) ? 1 : 0);

                db.insert(TABLE_NAME, null, question_details);
            }

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            db.close();
        }
    }

    public Question getQuestion(int q_id) {

        Question question = new Question();
        SQLiteDatabase db = this.getReadableDatabase();
        //specify the columns to be fetched
        String[] columns = {KEY_QUESTION_ID, KEY_QUESTION_BODY, KEY_ANSWER, KEY_SELECTED_OPTION};
        //Select condition
        String selection = KEY_QUESTION_ID + " = ?";
        //Arguments for selection
        String[] selectionArgs = {String.valueOf(q_id)};


        Cursor cursor = db.query(TABLE_NAME, columns, selection,
                selectionArgs, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            question.setqId(cursor.getInt(0));
            question.setQuestionBody(cursor.getString(1));
            question.setAnswer(cursor.getInt(2) == 1);
            question.setSelectedAnswer(cursor.getInt(3));
            cursor.close();
        }
        return question;
    }

    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questionsList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //if TABLE has rows
        if (cursor.moveToFirst()) {
            //Loop through the table rows
            do {
                Question question = new Question();
                question.setqId(cursor.getInt(0));
                question.setQuestionBody(cursor.getString(1));
                question.setAnswer(cursor.getInt(2) == 1);
                question.setSelectedAnswer(cursor.getInt(3));
                Log.d(TAG, question.toString());

                //Add movie details to list
                questionsList.add(question);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return questionsList;
    }

    public void updateQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        String questionIds[] = {String.valueOf(question.getqId())};

        ContentValues questionDetails = new ContentValues();
        questionDetails.put(KEY_QUESTION_BODY, question.getQuestionBody());
        questionDetails.put(KEY_ANSWER, question.getAnswer());
        questionDetails.put(KEY_SELECTED_OPTION, question.getSelectedAnswer());
        db.update(TABLE_NAME, questionDetails, KEY_QUESTION_ID + " = ?", questionIds);
    }

    public void exportToCsv() {

        try {
            String selectQuery = "SELECT * FROM " + TABLE_NAME;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            int rowcount = 0;
            int colcount = 0;
            File sdCardDir = mContext.getCacheDir();
            String filename = "questions.csv";

            File saveFile = new File(sdCardDir, filename);
            FileWriter fw = new FileWriter(saveFile);

            BufferedWriter bw = new BufferedWriter(fw);
            rowcount = cursor.getCount();
            colcount = cursor.getColumnCount();
            if (rowcount > 0) {
                cursor.moveToFirst();

                for (int i = 0; i < colcount; i++) {
                    if (i != colcount - 1) {

                        bw.write(cursor.getColumnName(i) + ",");

                    } else {

                        bw.write(cursor.getColumnName(i));

                    }
                }
                bw.newLine();

                for (int i = 0; i < rowcount; i++) {
                    cursor.moveToPosition(i);

                    for (int j = 0; j < colcount; j++) {
                        if (j == 2) {
                            bw.write("\"" + ((cursor.getInt(j) == 1) ? "True" : "False") + "\""
                                    + ",");
                        } else if (j == 3) {
                            bw.write("\"" + ((cursor.getInt(j) == -1) ? "Unattempted"
                                    : (cursor.getInt(j) == 1) ? "True" : "False") + "\"");
                        } else {
                            bw.write("\"" + cursor.getString(j) + "\",");
                        }
                    }
                    bw.newLine();
                }
                bw.flush();
                cursor.close();

                ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(
                        Context.CONNECTIVITY_SERVICE);
                assert connMgr != null;
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    // Available
                    Log.d(TAG, "Internet Available");
                    new UploadFileTask(mContext).execute(saveFile);
//                    new UploadFileToServer().execute(saveFile);
                } else {
                    Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "No Internet Connection");
                }
            }
        } catch (Exception ignored) {
            Log.d("Async", ignored.getMessage());
        }
    }

    static class UploadFileTask extends AsyncTask<File, Integer, String> {

        private static final String SERVER_URL = "http://192.168.60.146/d.php";
        private String result;
        private ProgressDialog mPDialog;
        private Context mContext;
        private int totalsize;

        UploadFileTask(Context context) {
            mContext = context;
            mPDialog = new ProgressDialog(context);

            mPDialog.setMessage("Uploading.. Please Wait");
//            mPDialog.setIndeterminate(true);
            mPDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mPDialog.setCancelable(true);
            // reference to instance to use inside listener
            final UploadFileTask me = this;
            mPDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    me.cancel(true);
                }
            });

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPDialog.setProgress(0);
            mPDialog.setMax(100);
            mPDialog.show();
        }

        @Override
        protected String doInBackground(File... files) {
            Log.d("Async", "doInBackground");
            try {
                File selectedFile = files[0];
                totalsize = (int) selectedFile.length();

                int serverResponseCode = 0;

                HttpURLConnection connection;
                DataOutputStream dataOutputStream;

                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                String boundary = "*****";
                connection.setRequestProperty("Content-Type",
                        "multipart/form-data;boundary=" + boundary);
//                connection.setRequestProperty("uploaded_file", selectedFilePath);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                String twoHyphens = "--";
                String lineEnd = "\r\n";
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes(
                        "Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                                + "questions.csv" + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                int bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                int maxBufferSize = 10;
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                byte[] buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                Log.d("Async", totalsize + " ts");
                int tb = bytesRead;
                while (bytesRead > 0) {
                    //write the bytes read from inputstream
                    Log.d("Async",
                            "br: " + bytesRead + "tb: " + tb + " progress: "
                                    + ((float) tb / totalsize) * 100);
                    dataOutputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    tb += bytesRead;
                    publishProgress(((int) tb / totalsize) * 100);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.i("Async",
                        "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if (serverResponseCode == 200) {
                    result = "File Upload completed.";
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();


            } catch (FileNotFoundException e) {
                result = "File Not Found";
            } catch (MalformedURLException e) {
                result = "URL error!";
            } catch (IOException e) {
                result = "Cannot Read/Write File! " + e.getMessage();
            } catch (Exception e) {

            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
//            mPDialog.incrementProgressBy(progress[0]);
            mPDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String message) {
            mPDialog.dismiss();
            Log.d("Async", message);
            if (result.equals("File Upload completed.")) {
                Toast.makeText(mContext, "File uploaded to the server successfully",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContext, "Failed to upload file to the server",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}

