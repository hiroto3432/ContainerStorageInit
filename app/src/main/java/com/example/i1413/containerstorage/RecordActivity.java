package com.example.i1413.containerstorage;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by i1413 on 2017/11/07.
 */

public class RecordActivity extends AppCompatActivity {

    public static ArrayList<Integer> imNum = new ArrayList<>();
    public static ArrayList<String> imName = new ArrayList<>();

    RecordActivity(){
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener//navigation view
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;

            switch (item.getItemId()) {

                case R.id.navigation_home:
                    intent = new Intent(RecordActivity.this,HomeActivity.class);
                    startActivity(intent);
                    return true;

                case R.id.navigation_record:
                    return true;

                case R.id.navigation_settings:
                    intent = new Intent(RecordActivity.this,SettingsActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }

    };

    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        //追加データ取得
        imNum = ItemData.imNum;
        imName = ItemData.imName;

        loadItemData();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //追加ボタン処理
        Button addbutton = (Button)findViewById(R.id.addButton);
        addbutton.setClickable(true);
        addbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordActivity.this, RegistActivity.class);
                startActivity(intent);
            }
        });
    }

    //記録済みデータの読み込み
    private void loadItemData(){

        LinearLayout itemList = (LinearLayout) findViewById(R.id.ItemList);
        itemList.removeAllViewsInLayout(); //初期化

        for(int n=0;n<imNum.size();n++){
            LinearLayout item = new LinearLayout(this);

            ImageView imView = new ImageView(this);
            Resources res = getResources() ;

            int Imid = res.getIdentifier("icon" + imNum.get(n), "drawable", getPackageName());
            imView.setImageResource(Imid);
            item.addView(imView); //画像の追加

            TextView txView = new TextView(this);
            txView.setText(imName.get(n));
            item.addView(txView); //テキストの追加

            itemList.addView(item); //アイテムをレイアウトに追加
        }
    }

}
