package kouroshcorps.testing;


import android.app.Activity;
import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Objects;

public class TextFile {

    private static TextFile instance;
    private static final String fileName = "data.txt";
    private Activity main;
    private ArrayList<List> lists = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();


    private TextFile(Activity main){
        this.main= main;
        load();
    }


    public static TextFile getInstance(Activity main){
        if (instance == null){
            instance = new TextFile(main);
        }
        return instance;
    }


    public void save(){

        //save titles on to the main file
        try{
            OutputStreamWriter out = new OutputStreamWriter(main.openFileOutput(fileName,0));
            for (String title: titles){
                out.write(title+"\n");
            }
            out.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }


        //Save content of each list into each file
        int count = 0;
        for (List list: lists){
            try {
                OutputStreamWriter out = new OutputStreamWriter(main.openFileOutput(count+".txt",0));
                for (Item item:list.items){
                    if (item.purchased) {
                        out.write("-"+item.getName() + "\n");
                    }
                    else{
                        out.write(item.getName() + "\n");
                    }

                }
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            count ++;
        }
    }

    public void reset(){
        try {
            OutputStreamWriter out = new OutputStreamWriter(main.openFileOutput(fileName,0));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void load() {
        try{

            // set titles
            BufferedReader in =new BufferedReader(new InputStreamReader(main.openFileInput(fileName)));
            String line;
            titles = new ArrayList<String>();
            while ((line = in.readLine()) !=null) {
                titles.add(line);
            }
            in.close();



            // set lists
            lists = new ArrayList<List>();
            for (int i=0;i< titles.size();i++){
                in = new BufferedReader(new InputStreamReader(main.openFileInput(i+".txt")));


                ArrayList<Item> items = new ArrayList<>();
                while((line=in.readLine())!= null){
                    if (line.charAt(0)=='-'){
                        items.add(new Item(line.substring(1),true));
                    }
                    else{
                        items.add(new Item(line, false));
                    }
                }


                lists.add(new List(titles.get(i),items));
                in.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addList(String title,List list){
        titles.add(title);
        lists.add(list);
        save();
    }

    public void deleteList(int pos){
        titles.remove(pos);
        lists.remove(pos);
        save();
    }

    public void addItem(int listPos,Item item){
        lists.get(listPos).addItem(item);
        save();

    }
    public void deleteItem(int listPos,int itemPos){
        lists.get(listPos).deleteItem(itemPos);
        save();
    }

    public ArrayList<String> getTitles(){
        return titles;
    }
    public List getList(int listPos){
        return lists.get(listPos);
    }

}
