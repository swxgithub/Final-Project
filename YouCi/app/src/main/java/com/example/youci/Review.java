package com.example.youci;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.xml.sax.*;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.database.Cursor;
import android.widget.Toast;

import java.io.InputStreamReader;


public class Review extends AppCompatActivity {
    public final static String iCiBaURL1 = "http://dict-co.iciba.com/api/dictionary.php?w=";
    public final static String iCiBaURL2 = "&key=ECBCD519B1ADA0D4C412A3EAB736A407";
    public  DataBaseHelper dataBaseHelper;
    int w = 0;
    public String[] str = new String[100];


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review);
        dataBaseHelper = new DataBaseHelper(this);
        w = 0;
        tostr();
        new getword().execute();
        if(str[0]==null){
            Toast.makeText(this,"Lexicon is empty.",Toast.LENGTH_LONG).show();
        }
    }


    public void btnQueryR(View view){
        startActivity(new Intent(this, query.class));
        finish();
    }

    public void tostr(){
        Cursor cursor = dataBaseHelper.readData();
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        while (cursor.moveToNext()){
            String s = cursor.getString(1);
            str[i] = s.valueOf(s);
            i = i + 1;
            System.out.println(s);
        }
        new getword().execute();
    }

    public void btnNext(View view){
        if(str[w]==""){
            Toast.makeText(this,"Lexicon is empty",Toast.LENGTH_LONG).show();
        }
        new getword().execute();
        w = w+1;
        System.out.println(str[0]);
    }

    public void btnDelete(View view) {
        dataBaseHelper.Delete(str[w]);
        tostr();
        new getword().execute();

    }

    public void btnReviewR(View view) {
        finish();
        startActivity(new Intent(this, Review.class));


    }


    class getword extends AsyncTask<String, Void, InputStream>{
        public InputStream doInBackground(String... params){
            InputStream tempInput = null;
            String word = str[w];
            if (word == null){
                w = 0;
                word = str[w];
                if(word == null){
                    str[w] ="";
                    word = str[w];
                }
            }
            URL url = null;
            HttpURLConnection connection = null;
            try {
                String urlStr = iCiBaURL1+word+iCiBaURL2 ;
                url = new URL(urlStr);
                connection = (HttpURLConnection) url.openConnection();     //别忘了强制类型转换
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(10000);
                connection.setRequestMethod("GET");
                connection.connect();
                tempInput = connection.getInputStream();
                return tempInput;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public void onPostExecute(InputStream tempInput){
            System.out.println(tempInput);
            String word = str[w];
            if(word == null){
                w = 0;
                word = str[w];
                if(word == null){
                    str[w] ="";
                    word = str[w];
                }
            }
            WordValue wordValue=null;

            try{
                //从网络获得输入流
                if(tempInput!=null){
                    XMLParser xmlParser=new XMLParser();
                    InputStreamReader reader=new InputStreamReader(tempInput,"utf-8");        //最终目的获得一个InputSource对象用于传入形参
                    JinShanContentHandler contentHandler=new JinShanContentHandler();
                    xmlParser.parseJinShanXml(contentHandler, new InputSource(reader));
                    wordValue=contentHandler.getWordValue();
                    wordValue.setWord(word);
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            View Word = ((TextView)findViewById(R.id.wordR));
            ((TextView) Word).setText(wordValue.getWord());

            View Interpret = ((TextView)findViewById(R.id.InterpretR));
            ((TextView) Interpret).setText(wordValue.getInterpret());

            View Santance = ((TextView)findViewById(R.id.sentancesR));
            ((TextView) Santance).setText("   "+wordValue.getSentOrig());

            View PsE = ((TextView)findViewById(R.id.psER));
            ((TextView) PsE).setText(wordValue.getPsE());

        }
    }
}

