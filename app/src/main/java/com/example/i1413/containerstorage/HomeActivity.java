package com.example.i1413.containerstorage;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Intent intent;
            switch (item.getItemId()) {

                case R.id.navigation_home:
                    return true;

                case R.id.navigation_record:
                    intent = new Intent(HomeActivity.this,RecordActivity.class);
                    startActivity(intent);
                    return true;

                case R.id.navigation_settings:
                    intent = new Intent(HomeActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    return true;
            }

            return false;
        }

    };

    public static void main(String[] args) {
    }

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        for(int y=0; y<2; y++){
            for(int x=0; x<2; x++){
                int num = y*2 + x+1;
                loadData(num);
            }
        }

        Resources res = getResources();
        LinearLayout[] linearlayout = new LinearLayout[4];

        for(int n=1;n<=4;n++){
            int llId = res.getIdentifier("LinearLayout"+n,"id",getPackageName());
            linearlayout[n-1] = (LinearLayout)findViewById(llId);
        }

        for(int n=0;n<4;n++) {

            linearlayout[n].setClickable(true);
            linearlayout[n].setTag(String.valueOf(n+1));
            linearlayout[n].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = Integer.parseInt(v.getTag().toString());
                    Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
                    intent.putExtra("number",tag);

                    startActivity(intent);
                }
            });
        }
    }

    public void loadData(int Vnum){

        Resources res = getResources();
        ImageView view;
        TextView names;
        TextView limits;

        int ImageId;

        int viewId = res.getIdentifier("imageView"+Vnum,"id",getPackageName());
        int nameId = res.getIdentifier("item"+Vnum,"id",getPackageName());
        int limitId = res.getIdentifier("deadLine"+Vnum,"id",getPackageName());

        view = (ImageView)findViewById(viewId);
        names = (TextView)findViewById(nameId);
        limits = (TextView)findViewById(limitId);

        ImageId = res.getIdentifier("icon" + ItemData.getImageFile(Vnum - 1), "drawable", getPackageName());

        view.setImageResource(ImageId);
        names.setText(ItemData.getName(Vnum-1));
        limits.setText(ItemData.getLimit(Vnum-1));

    }

}