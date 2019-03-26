package com.example.youci;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.xml.sax.*;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.view.View;
import android.widget.TextView;

import java.io.InputStreamReader;

public class query extends AppCompatActivity {
    public final static String iCiBaURL1 = "http://dict-co.iciba.com/api/dictionary.php?w=";
    public final static String iCiBaURL2 = "&key=ECBCD519B1ADA0D4C412A3EAB736A407";
    private EditText inputWord;
    Review re = new Review();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query);
        re.dataBaseHelper = new DataBaseHelper(this);
    }

    public void btnQuery(View view){
        new getInfoOnline().execute();
    }

    public void btnReview(View view){
        startActivity(new Intent(this, Review.class));
        finish();
    }

    public void btnAddmyword(View view){
        inputWord = findViewById(R.id.inputWord);
        String word = inputWord.getText().toString().trim();
        re.dataBaseHelper.insertData(word);
    }


     class getInfoOnline extends AsyncTask<String, Void, InputStream>{
        public InputStream doInBackground(String... params){
            InputStream tempInput = null;
            inputWord = findViewById(R.id.inputWord);
            String word = inputWord.getText().toString().trim();
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
             inputWord = findViewById(R.id.inputWord);
             String word = inputWord.getText().toString().trim();
             WordValue wordValue=null;
             try{
                 //String tempUrl=query.iCiBaURL1+tempWord+query.iCiBaURL2;
                 //ystem.out.println(tempUrl);
                    //从网络获得输入流
                 if(tempInput!=null){
                     System.out.println("here");
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

             View Word = ((TextView)findViewById(R.id.word));
             ((TextView) Word).setText(wordValue.getWord());

             View Interpret = ((TextView)findViewById(R.id.Interpret));
             ((TextView) Interpret).setText(wordValue.getInterpret());

             View Santance = ((TextView)findViewById(R.id.sentances));
             ((TextView) Santance).setText("  "+wordValue.getSentOrig());

             View psE = ((TextView)findViewById(R.id.psE));
             ((TextView) psE).setText("/"+wordValue.getPsE()+"/");

         }
        }
     }








