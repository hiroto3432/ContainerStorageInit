package com.example.i1413.containerstorage;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;

/**
 * Created by i1413 on 2017/12/19.
 */

public class StockActivity extends AppCompatActivity {

    public static ArrayList<Integer> imNum = new ArrayList<>();
    public static ArrayList<String> imName = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_stock);


        final int ims = loadImageData();
        final int imshead = 900;

        for(int n=0;n<ims;n++) {

            Resources res = getResources();
            int imId = res.getIdentifier("icon" + n, "drawable", getPackageName());
            int ibuttonId = res.getIdentifier(String.valueOf(imshead + n), "drawable", getPackageName());

            try {
                ImageButton imButton = (ImageButton) findViewById(ibuttonId);

                imButton.setClickable(true);

                imButton.setTag(R.integer.imResources,imId);
                imButton.setTag(R.integer.imButtons,n);

                imButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int resId = Integer.parseInt(v.getTag(R.integer.imResources).toString());
                        int iNum =  Integer.parseInt(v.getTag(R.integer.imButtons).toString());

                        ImageView iView = (ImageView) findViewById(R.id.stockImage);

                        iView.setImageResource(resId);
                        iView.setTag(imshead + iNum);
                    }
                });
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        imNum = RecordActivity.imNum;
        imName = RecordActivity.imName;

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

                int sim = (Integer)(selectImage.getTag()) - imshead;

                String sname = name.getText().toString();
                String slimit = limit.getText().toString();

                ItemData.registData(tag,sname,slimit,sim);

                Intent intent = new Intent(StockActivity.this, DetailActivity.class);
                intent.putExtra("number",tag);
                startActivity(intent);
            }
        });

    }

    private int loadImageData() {

        Resources res = getResources();

        int n = 0;
        final int head = 100;
        final int imshead = 900;
        final int row = 5;

        while (true) {
            if (n > head) {
                break;
            }
            try {

                TableLayout tlayout = (TableLayout) findViewById(R.id.IconTable);

                int Imid = res.getIdentifier("icon" + n, "drawable", getPackageName());

                if (Imid == 0) break;

                ImageButton imButton = new ImageButton(this);
                imButton.setImageResource(Imid);
                imButton.setId(imshead + n);

                if (n % row == 0) {

                    TableRow trow = new TableRow(this);
                    trow.setId(head + n / row);
                    tlayout.addView(trow);
                }

                TableRow a_row = (TableRow) findViewById(head + n / row);

                a_row.addView(imButton);
                n++;

            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        return n;
    }

}


