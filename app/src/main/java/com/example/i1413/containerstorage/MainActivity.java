package com.example.i1413.containerstorage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ItemData item = new ItemData();
        item.initialize(); //アイテムデータの初期化

        item.getItemListData(this); //アイテムリストの読み込み
        item.getStockListData(this); //ストックデータの読み込み

/*デバッグ用
        item.setData(4,"チョコレート","12/31",2,"12.3℃","45%","6枚");//アイテムの初期化
        item.deleteRecode(this);//記録データ全消去
        item.deleteRecode(this,n);//n番目の記録データ消去
*/

    }

    public boolean onTouchEvent(MotionEvent event){

        Intent intent = new Intent(MainActivity.this,HomeActivity.class);

        try {
            startActivity(intent);
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
