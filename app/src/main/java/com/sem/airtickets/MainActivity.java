package com.sem.airtickets;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;



public class MainActivity extends ActionBarActivity implements TextWatcher {

    private String TAG ="Deb";
    TextView outPutText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AutoCompleteTextView autoCity1;
        AutoCompleteTextView autoCity2;
        final String[] mCities = { "Balashiha", "Moscow", "Samara", "Tula", "Samaly"};

        outPutText = (TextView) findViewById(R.id.textView1);

        autoCity1 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        autoCity2 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
        autoCity1.addTextChangedListener(this);
        autoCity1.setAdapter(new ArrayAdapter(this,
                android.R.layout.simple_dropdown_item_1line, mCities));
        autoCity2.addTextChangedListener(this);
        autoCity2.setAdapter(new ArrayAdapter(this,
                android.R.layout.simple_dropdown_item_1line, mCities));




        Button buttonChoose = (Button) findViewById(R.id.button1);
        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                AsyncConn taskConnSS = new AsyncConn();
                taskConnSS.execute();
            }
        });
          //  StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
          //          .detectNetwork()   // or .detectAll() for all detectable problems
          //          .penaltyLog()
          //          .build());
    }

    private class AsyncConn extends AsyncTask<String, String, String> {

        private String res;

        @Override
        protected String doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            {String NAMESPACE = "http://www.tais.ru/";
                String URL = "http://search.biletix.ru/bitrix/components/travelshop/ibe.soap/travelshop_booking.php";
                String SOAP_ACTION_START = "http://www.tais.ru/StartSessionInput";
                String METHOD_NAME_START = "StartSessionInput";

                String login = "[test]||semidevco";
                String password = "semidevco";
                String hash = "3e437035fe53bad04f51c4d0c056f4eb";
                String disable_hash = "N";

                //Инициализация soap запроса  и добавление параметров
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_START);
                //здесь добавляются параметры
                request.addProperty("login", login);
                request.addProperty("password", password);
                request.addProperty("hash", hash);
                request.addProperty("disable_hash", disable_hash);
                //декларация версии soap запроса
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);

                try {
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                    //здесь вызывается вебсервис
                    androidHttpTransport.call(SOAP_ACTION_START, envelope);
                    //получение soap результата в конверт
                    SoapObject result = (SoapObject) envelope.bodyIn;

                    //получение параметра и изменение текстового поля

                    res = result.toString();

                } catch (Exception e) {
                    e.printStackTrace();
                }}
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "onPostExecute");
            outPutText.setText(res);
        }


    }


    //метод по обращению к вебсервису



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

