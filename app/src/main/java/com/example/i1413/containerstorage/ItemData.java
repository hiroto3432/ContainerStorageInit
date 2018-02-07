package com.example.i1413.containerstorage;


import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ItemData extends AppCompatActivity {

    public static String name[] = new String[4];
    public static String limit[] = new String[4];
    public static int imgFile[] = new int[4];
    public static String temp[] = new String[4];
    public static String humid[] = new String[4];
    public static String amount[] = new String[4];

    public static void initialize(){
        for(int n=0;n<name.length;n++){
            name[n] = "no item";
            limit[n] = "no limit";
            imgFile[n] = 0;
            temp[n] = "no temp";
            humid[n] = "no humid";
            amount[n] = "no amount";
        }
    }
    public static void initialize(int num){
            int n = num-1;
            name[n] = "no item";
            limit[n] = "no limit";
            imgFile[n] = 0;
            temp[n] = "no temp";
            humid[n] = "no humid";
            amount[n] = "no amount";
    }

    public static void setData(String[] _name,String[] _limit,int[] _imgFile,String[] _temp,String[] _humid,String[] _amount){

        for(int n=0;n<name.length;n++){
            name[n] = _name[n];
            limit[n] = _limit[n];
            imgFile[n] = _imgFile[n];
            temp[n] = _temp[n];
            humid[n] = _humid[n];
            amount[n] = _amount[n];
        }

    }
    public static void setData(int n,String _name,String _limit,int _imgFile, String _temp,String _humid,String _amount){
        int num = n - 1;
        name[num] = _name;
        limit[num] = _limit;
        imgFile[num] = _imgFile;
        temp[num] = _temp;
        humid[num] = _humid;
        amount[num] = _amount;
    }

    public static void registData(int n,String _name,String _limit,int _imgFile){
        int num = n - 1;
        name[num] = _name;
        limit[num] = _limit;
        imgFile[num] = _imgFile;
    }


    public void setName(){

    }
    public void setLimit(){

    }
    public void setImageFile(){

    }
    public void setTemp(){

    }
    public void setHumid(){

    }
    public void setAmount(){

    }


    public static String getName(int n){
        return name[n];
    }
    public static String getLimit(int n){
        return limit[n];
    }
    public static int getImageFile(int n){
        return imgFile[n];
    }
    public static String getTemp(int n){ return temp[n]; }
    public static String getHumid(int n){ return humid[n];}
    public static String getAmount(int n){ return amount[n];}

    public static ArrayList<Integer> imNum = new ArrayList<>();
    public static ArrayList<String> imName = new ArrayList<>();


    //イメージデータが空かどうか
    public static boolean emptyItem(int n){
        if(getImageFile(n - 1) == 0) return true;
        else return false;
    }

    //アイテムデータ読み込み
    public static void getItemListData(Context context){

        String text = getJsonData(context,"ItemList.json");

        try {
            JSONObject json = new JSONObject(text);
            JSONArray datas = json.getJSONArray("scrolllist");

            for(int n=0;n<datas.length();n++){
                JSONObject data = datas.getJSONObject(n);

                imNum.add(data.getInt("imid"));
                imName.add(data.getString("name"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //ストック済みデータ読み込み
    public static void getStockListData(Context context){
        String text = getJsonData(context,"StockList.json");

        try {
            JSONObject json = new JSONObject(text);
            JSONArray datas = json.getJSONArray("stocklist");

            for(int n=0;n<datas.length();n++){

                JSONObject data = datas.getJSONObject(n);

                int id = data.getInt("id");

                String _name = data.getString("name");
                if(_name.equals("null")) {
                    initialize(id);
                    continue;
                }
                String _limit = data.getString("limit");
                int _imgNum = data.getInt("imNum");
                registData(id , _name , _limit , _imgNum); //データ登録

            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    //JSON読み込み処理
    private static String getJsonData(Context context,String filename){

        FileInputStream is = null;
        BufferedReader br = null;
        String text = "";

        try {
            try {
                is = context.openFileInput(filename);
                br = new BufferedReader(new InputStreamReader(is));

                String str;
                while ((str = br.readLine()) != null) {
                    text += str + "\n";
                }
            }
            finally {
                if (is != null) is.close();
                if (br != null) br.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return text;
    }

    //記録データ追加
    public static void recodeData(Context context,int imId,String name){
        imNum.add(imId);
        imName.add(name);
        updataItemList(context);
    }

    //ストック済みデータ書き込み
    public static void updataItemList(Context context){

        String text = "";
        text += "{\n";
        text += " \"scrolllist\":[ \n";
        for(int n=0;n<imNum.size();n++){
            text += "\t { \"id\":\""+(n+1)+"\", \"imid\":\""+imNum.get(n)+"\", \"name\":\""+imName.get(n)+"\" }";
            if(n!=3) text += ",";
            text += "\n";
        }
        text += " ]\n";
        text += "}\n";

        updataJSON(context,"ItemList.json",text);
    }

    //記録データ書き込み
    public static void updataStockList(Context context){

        String text = "";
        text += "{\n";
        text += " \"stocklist\":[ \n";
        for(int n=0;n<4;n++){
            text += "\t { \"id\":\""+(n+1)+"\", \"name\":\""+name[n]+"\", \"limit\":\""+limit[n]+"\", \"imNum\":\""+imgFile[n]+"\" }";
            if(n!=3) text += ",";
            text += "\n";
        }
        text += " ]\n";
        text += "}\n";

        updataJSON(context,"StockList.json",text);
    }

    //JSON書き込み処理
    private static void updataJSON(Context context,String filename,String text){

        try {
            FileOutputStream fileOutputStream = context.openFileOutput(filename, MODE_PRIVATE);
            fileOutputStream.write(text.getBytes());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //アイテムデータ消去(デバッグ用)
    public static void deleteRecode(Context context){

        imNum.clear();
        imName.clear();

        updataItemList(context);

    }

    //n番目のアイテムデータ消去(デバッグ用)
    public static void deleteRecode(Context context,int n){

        imNum.remove(n);
        imName.remove(n);

        updataItemList(context);

    }
}
