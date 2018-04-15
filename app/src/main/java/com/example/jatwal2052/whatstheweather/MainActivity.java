package com.example.jatwal2052.whatstheweather;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;
    Button button;

    public void click(View view){
        String result = editText.getText().toString();

        String result1;
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);
        DownloadTask downloadTask = new DownloadTask();

        try {
            result1 = downloadTask.execute("http://api.openweathermap.org/data/2.5/weather?q=" + URLEncoder.encode(result,"UTF-8") +"&appid=c4bd953175a9a8695d4026c61e1eecf3").get();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Enter a valid city", Toast.LENGTH_SHORT).show();
        }

    }
    public class DownloadTask extends AsyncTask<String ,Void ,String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";
            URL url;
            HttpURLConnection httpURLConnection = null;

            try {

                url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();
                Log.i("url", params[0]);

                while (data != -1) {
                    char s = (char) data;
                    result += s;
                    data = reader.read();
                }
                return result;

            } catch (IOException e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(),"enter a valid city",Toast.LENGTH_SHORT).show();


            }
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String result;
            String resultf;
            try {
                JSONObject jsonObject = new JSONObject(s);
                result = jsonObject.getString("weather");
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    resultf = jsonObject1.getString("main") +jsonObject1.getString("description");
                    if(resultf != ""){
                         textView.setText(resultf);
                    }

                }
            } catch (Exception e) {

                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"enter a valid city",Toast.LENGTH_SHORT).show();

            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView2);
        button = (Button) findViewById(R.id.button);


        //Log.i("result",result);

    }

}
