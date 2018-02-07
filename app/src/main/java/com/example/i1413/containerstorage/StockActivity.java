package com.example.i1413.containerstorage;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by i1413 on 2017/12/19.
 */

public class StockActivity extends AppCompatActivity implements parameters{

    public static ArrayList<Integer> imNum = new ArrayList<>();
    public static ArrayList<String> imName = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_stock);


        final int ims = loadImageData();

        for(int n=0;n<ims;n++) {

            Resources res = getResources();
            int ibuttonId = res.getIdentifier(String.valueOf(imageshead + n), "drawable", getPackageName());

            try {
                ImageButton imButton = (ImageButton) findViewById(ibuttonId);

                imButton.setClickable(true);
                imButton.setTag(n);

                imButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int iNum = Integer.parseInt(v.getTag().toString());

                        Resources res = getResources();
                        int resId = res.getIdentifier("icon" + iNum, "drawable", getPackageName());

                        ImageView iView = (ImageView) findViewById(R.id.stockImage);

                        iView.setImageResource(resId);
                        iView.setTag(imageshead + iNum);
                    }
                });
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        imNum = ItemData.imNum;
        imName = ItemData.imName;

        loadItemData();

        for(int n=0;n<imNum.size();n++) {

            Resources res = getResources();
            int linearId = res.getIdentifier(String.valueOf(itemshead + n), "drawable", getPackageName());

            try {
                LinearLayout linear = (LinearLayout) findViewById(linearId);

                linear.setClickable(true);
                linear.setTag(n);

                linear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int lNum = Integer.parseInt(v.getTag().toString());
                        int imgId = ItemData.imNum.get(lNum);
                        String name = ItemData.imName.get(lNum);

                        TextView nameView = (TextView)findViewById(R.id.nameEdit);
                        nameView.setText(name);


                        Resources res = getResources();
                        int resId = res.getIdentifier("icon" + imgId, "drawable", getPackageName());

                        ImageView iView = (ImageView) findViewById(R.id.stockImage);
                        iView.setImageResource(resId);
                        iView.setTag(imageshead + imgId);
                    }
                });

            }
            catch(Exception e){
                e.printStackTrace();
            }
        }



        Intent intent = getIntent();
        int num = intent.getIntExtra("number", 0);

        Button cancel = (Button) findViewById(R.id.cancelButton);
        cancel.setTag(String.valueOf(num));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    int tag = Integer.parseInt(v.getTag().toString());

                    Intent intent = new Intent(StockActivity.this, DetailActivity.class);
                    intent.putExtra("number",tag);
                    startActivity(intent);

            }
        });

        Button ok = (Button)findViewById(R.id.okButton);
        ok.setTag(String.valueOf(num));
        ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                int tag = Integer.parseInt(v.getTag().toString());

                EditText name = (EditText)findViewById(R.id.nameEdit);
                EditText limit = (EditText)findViewById(R.id.limitEdit);

                ImageView selectImage = (ImageView) findViewById(R.id.stockImage);

                int sim = (Integer)(selectImage.getTag()) - imageshead;

                String sname = name.getText().toString();
                String slimit = limit.getText().toString();

                ItemData.registData(tag,sname,slimit,sim);
                stockData();

                Intent intent = new Intent(StockActivity.this, DetailActivity.class);
                intent.putExtra("number",tag);
                startActivity(intent);
            }
        });

    }

    private void stockData(){
        ItemData item = new ItemData();
        item.updataStockList(this);
    }

    private int loadImageData() {

        Resources res = getResources();

        int n = 0;

        while (true) {
            if (n > 100) {
                break;
            }
            try {

                TableLayout tlayout = (TableLayout) findViewById(R.id.IconTable);

                int Imid = res.getIdentifier("icon" + n, "drawable", getPackageName());

                if (Imid == 0) break;

                ImageButton imButton = new ImageButton(this);
                imButton.setImageResource(Imid);
                imButton.setId(imageshead + n);

                if (n % row == 0) {

                    TableRow trow = new TableRow(this);
                    trow.setId(tableshead + n / row);
                    tlayout.addView(trow);
                }

                TableRow a_row = (TableRow) findViewById(tableshead + n / row);

                a_row.addView(imButton);
                n++;

            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        return n;
    }

    private void loadItemData(){

        LinearLayout itemList = (LinearLayout) findViewById(R.id.registedDataList);
        itemList.removeAllViewsInLayout(); //初期化


        for(int n=0;n<imNum.size();n++){
            LinearLayout item = new LinearLayout(this);
            item.setId(itemshead + n);

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


