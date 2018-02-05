package com.example.i1413.containerstorage;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by i1413 on 2017/12/05.
 */

public class DetailActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        int num = intent.getIntExtra("number",0);
        loadData(num);


        Button back = (Button)findViewById(R.id.backButton);
        back.setClickable(true);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        Button d_r = (Button)findViewById(R.id.d_rButton);
        d_r.setClickable(true);
        d_r.setTag(String.valueOf(num));
        d_r.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                int tag = Integer.parseInt(v.getTag().toString());
                if(!ItemData.emptyItem(tag)){
                    ItemData.initialize(tag);
                    loadData(tag);
                }
                else{
                    Intent intent = new Intent(DetailActivity.this,StockActivity.class);
                    intent.putExtra("number",tag);
                    startActivity(intent);
                }
            }
        });

    }

    private void loadData(int Vnum) {

        Resources res = getResources();
        ImageView view;
        TextView names;
        TextView limits;
        TextView temps;
        TextView humids;
        TextView amounts;

        int ImageId;
        view = (ImageView) findViewById(R.id.iconImage);
        names = (TextView)findViewById(R.id.nameView);
        limits = (TextView)findViewById(R.id.limitView);
        temps = (TextView)findViewById(R.id.tempView);
        humids = (TextView)findViewById(R.id.humidView);
        amounts = (TextView)findViewById(R.id.amountView);

        ImageId = res.getIdentifier("icon" + ItemData.getImageFile(Vnum - 1), "drawable", getPackageName());
        view.setImageResource(ImageId);
        names.setText(ItemData.getName(Vnum-1));
        limits.setText(ItemData.getLimit(Vnum-1));
        temps.setText(ItemData.getTemp(Vnum-1));
        humids.setText(ItemData.getHumid(Vnum-1));
        amounts.setText(ItemData.getAmount(Vnum-1));

        Button d_r = (Button)findViewById(R.id.d_rButton);
        if(ItemData.emptyItem(Vnum)) d_r.setText("stock");
        else d_r.setText("delete");
    }

}
