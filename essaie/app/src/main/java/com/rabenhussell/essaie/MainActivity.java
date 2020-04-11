package com.rabenhussell.essaie;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
     NfcAdapter nfcAdapter;
     TextView textTagContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textTagContent = (TextView) findViewById(R.id.textTagContent);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if(nfcAdapter != null){
            Toast.makeText(this,"NFC available", Toast.LENGTH_LONG).show();
           // finish();
           // return;
        }
       Toast.makeText(this,"NFC is available", Toast.LENGTH_LONG).show();
    }

    private NdefRecord createTextRecord(String message){
        try
        {
            byte[] language;
            language = Locale.getDefault().getLanguage().getBytes("UTF-8");

            final byte[] text = message.getBytes("UTF-8");
            final int languageSize = language.length;
            final int textLength = text.length;

            final ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + languageSize + textLength);

            payload.write((byte) (languageSize & 0x1F));
            payload.write(language, 0, languageSize);
            payload.write(text, 0, textLength);

            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());
        }
        catch (UnsupportedEncodingException e)
        {
            Log.e("createTextRecord", e.getMessage());
        }
        return null;
    }

    private NdefMessage createNdefMessage(String content) {

        NdefRecord ndefRecord = createTextRecord(content);

        NdefMessage msg = new NdefMessage(new NdefRecord[]{ndefRecord});

        return  msg;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Toast.makeText(this, "NfcIntent", Toast.LENGTH_LONG).show();

            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (parcelables != null & parcelables.length > 0) {
                readTextFromMessage((NdefMessage) parcelables[0]);
            } else {
                Toast.makeText(this, "No NDEF record found", Toast.LENGTH_LONG).show();
            }

        }
    }



    private void enableForegroundDispatchSystem(){

        Intent intent = new Intent(this,MainActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, intent,0);
        IntentFilter[] intentFilter = new IntentFilter[] {};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilter, null);
    }

    private NdefMessage getNdefFromMessageIntent(Intent intent) {
        NdefMessage ndefMessage= null;
        Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if(parcelables != null & parcelables.length >0){
            ndefMessage = (NdefMessage) parcelables[0];
        }
        return  ndefMessage;
    }
    @Override
    protected  void  onResume(){
        super.onResume();
        if(getIntent().hasExtra(NfcAdapter.EXTRA_TAG)){
            NdefMessage ndefMessage = this.getNdefFromMessageIntent(getIntent());
            if(ndefMessage.getRecords().length >0){
                NdefRecord ndefRecord = ndefMessage.getRecords()[0];
                String payload = new String(ndefRecord.getPayload());
                Toast.makeText(this, payload, Toast.LENGTH_SHORT).show();
            }
        }
        enableForegroundDispatchSystem();
    }

    private void readTextFromMessage(NdefMessage ndefMessage) {
        NdefRecord [] ndefRecords = ndefMessage.getRecords();
        if(ndefRecords != null & ndefRecords.length >0){
            NdefRecord ndefRecord = ndefRecords[0];
            String tagContent = getTexFromNdefRecord(ndefRecord);
            textTagContent.setText(tagContent);
        } else {
            Toast.makeText(this, "No NDEF record found", Toast.LENGTH_LONG).show();
        }
    }

    public String getTexFromNdefRecord(NdefRecord ndefRecord){
        String tagContent = null;
        try {
            byte [] payload = ndefRecord.getPayload();
            String textEnconding =((payload[0] & 128) == 0 ) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize+1, payload.length-languageSize-1, textEnconding);
        } catch (UnsupportedEncodingException e){

            Log.e("getTextFromNdefRecord", e.getMessage(), e);
        }
        return tagContent;
    }
}
