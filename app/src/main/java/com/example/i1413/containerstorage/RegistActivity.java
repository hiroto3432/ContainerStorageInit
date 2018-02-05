package com.example.i1413.containerstorage;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by i1413 on 2018/01/09.
 */

public class RegistActivity extends AppCompatActivity {


    public void onCreate(Bundle savedInstanteState){

        super.onCreate(savedInstanteState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_regist);

        int imNum = loadImageData();

        for(int n=0;n<imNum;n++) {

            Resources res = getResources();
            int imId = res.getIdentifier("icon" + n, "drawable", getPackageName());

            ImageButton imButton = (ImageButton) findViewById(imId);
            imButton.setClickable(true);
            imButton.setTag(imId);

            //アイコンタップ処理
            imButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int tag = Integer.parseInt(v.getTag().toString());
                    ImageView iView = (ImageView)findViewById(R.id.registImage);
                    iView.setImageResource(tag);

                }
            });
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

                registData();

                Intent intent = new Intent(RegistActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });

    }

    //Jsonデータに追加
    private void registData(){

        InputStream is = null;
        BufferedReader br = null;
        String text = "";

        try {
            try {
                is = this.getAssets().open("ItemList.json");
                br = new BufferedReader(new InputStreamReader(is));

                String str;
                while ((str = br.readLine()) != null) {
                    text += str + "\n";
                }
            } finally {
                if (is != null) is.close();
                if (br != null) br.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            JSONObject json = new JSONObject(text);
            JSONArray datas = json.getJSONArray("scrolllist");

            JSONObject ad = new JSONObject();
            ad.put("id","20");
            ad.put("imid","0");
            ad.put("name","add");

            datas.put(ad);

        }catch(Exception e){
            e.printStackTrace();
        }

        /* 卍卍卍 ここにとうろくしょりをかく 卍卍卍 */
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
                imButton.setId(Imid);

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
