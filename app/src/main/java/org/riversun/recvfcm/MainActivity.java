/*
 * Copyright 2016-2017 Tom Misawa, riversun.org@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 *  INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package org.riversun.recvfcm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.w3c.dom.Text;

/**
 * Empty activity.<br>
 * Need to see logcat.
 *
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class MainActivity extends AppCompatActivity {

    ProgressDialog mProgressDialog;
    Button mBtnEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logg.d();
        setContentView(R.layout.activity_main);
        //
        new GetToken().execute();
        //
        mBtnEmail = (Button) findViewById(R.id.btn_email);

        // attach setOnClickListener to button
        // with Intent object define in it
        mBtnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( FcmTokenRegistrationService.MY_TOKEN == ""){
                    return;
                }
                String emailsend = "";
                String emailsubject = " FCM TOKEN ";
                String emailbody = " Your FCM token is : " + FcmTokenRegistrationService.MY_TOKEN;
                // define Intent object
                // with action attribute as ACTION_SEND
                Intent intent = new Intent(Intent.ACTION_SEND);
                // add three fiels to intent using putExtra function
                intent.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{emailsend});
                intent.putExtra(Intent.EXTRA_SUBJECT, emailsubject);
                intent.putExtra(Intent.EXTRA_TEXT, emailbody);
                // set type of intent
                intent.setType("message/rfc822");
                // startActivity with intent with chooser
                // as Email client using createChooser function
                startActivity(Intent
                        .createChooser(intent,
                                "Choose an Email client :"));
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        Logg.d();
        sendFcmRegistrationToken();

    }


    void setFCMTOKEN(){
        ((TextView) findViewById(R.id.tv_token)).setText(FcmTokenRegistrationService.MY_TOKEN);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logg.d();
    }

    private void sendFcmRegistrationToken() {
        Intent intent = new Intent(this, FcmTokenRegistrationService.class);
        startService(intent);
    }


    public class GetToken extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = mProgressDialog.show(MainActivity.this, "", "Please wait...");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (FcmTokenRegistrationService.MY_TOKEN == "") {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ((TextView) findViewById(R.id.tv_token)).setText(FcmTokenRegistrationService.MY_TOKEN);
            mProgressDialog.dismiss();
        }

    }

    private void checkGoogleApiAvailability() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (resultCode == ConnectionResult.SUCCESS) {
                Logg.d("GoogleApi is available");
            } else {
                apiAvailability.getErrorDialog(this, resultCode, 1).show();
            }
        }
    }
}
