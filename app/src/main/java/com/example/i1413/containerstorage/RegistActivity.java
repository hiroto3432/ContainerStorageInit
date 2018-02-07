package com.example.i1413.containerstorage;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by i1413 on 2018/01/09.
 */

public class RegistActivity extends AppCompatActivity implements parameters{


    public void onCreate(Bundle savedInstanteState){

        super.onCreate(savedInstanteState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_regist);

        final int imNum = loadImageData();

        for(int n=0;n<imNum;n++) {

            Resources res = getResources();
            int imId = res.getIdentifier(String.valueOf(imageshead + n), "drawable", getPackageName());

            try {
                ImageButton imButton = (ImageButton) findViewById(imId);
                imButton.setClickable(true);
                imButton.setTag(n);

                //アイコンタップ処理
                imButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int iNum = Integer.parseInt(v.getTag().toString());
                        ImageView iView = (ImageView) findViewById(R.id.registImage);

                        Resources res = getResources();
                        int resId = res.getIdentifier("icon" + iNum, "drawable", getPackageName());

                        iView.setImageResource(resId);
                        iView.setTag(imageshead + iNum);

                    }
                });
            }
            catch(Exception e){
                Log.d("卍",e.toString());
                e.printStackTrace();
            }
        }

        //戻るボタンタップ処理
        Button backbutton = (Button)findViewById(R.id.backButton);
        backbutton.setClickable(true);
        backbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });

        //登録ボタンタップ処理
        Button regbutton = (Button)findViewById(R.id.registButton);
        regbutton.setClickable(true);
        regbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                TextView ename = (TextView)findViewById(R.id.editText);
                ImageView iView = (ImageView)findViewById(R.id.registImage);

                int resId = (Integer)iView.getTag() - imageshead;
                String name = ename.getText().toString();

                registData(resId,name);

                Intent intent = new Intent(RegistActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });

    }

    //Jsonデータに追加
     private void registData(int imId, String name){
         ItemData item = new ItemData();
         item.recodeData(this,imId,name);
     }


    //登録データ読み込み
    private int loadImageData(){

        Resources res = getResources();

        int n=0;
        final int head = 100;
        final int row = 5; //一列に表示するアイコン数

        while(true){

            if(n>head){
                break;
            }
            try{

                TableLayout tlayout = (TableLayout)findViewById(R.id.ImagesTable);

                int Imid = res.getIdentifier("icon" + n, "drawable", getPackageName());

                if(Imid == 0) break; //データがない場合はbreak

                //登録するimButton
                ImageButton imButton = new ImageButton(this);
                imButton.setImageResource(Imid);
                imButton.setId(imageshead + n);

                if(n%row == 0){ //一列に表示する最大値が格納されている場合は列を追加し、その列に追加していく

                    TableRow trow = new TableRow(this);
                    trow.setId(head + n/row);
                    tlayout.addView(trow);

                }

                TableRow a_row = (TableRow)findViewById(head + n/row);

                a_row.addView(imButton); //viewの追加
                n++;

            }catch(Exception e){
                break;
            }
        }
        return n;
    }

}
