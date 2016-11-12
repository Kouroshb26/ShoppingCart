package kouroshcorps.testing;

import java.util.ArrayList;

/**
 * Created by kourosh on 2016-03-28.
 */
public class List {
    protected ArrayList<Item> items;
    private String title;

    public List(String title, ArrayList<Item> items){
        this.title = title;
        this.items = items;
    }

    public ArrayList<String> getItemsNames(){
        ArrayList<String> itemNames = new ArrayList<>();
        for(Item item:items){
            itemNames.add(item.getName());
        }
        return itemNames;
    }


    public String getTitle(){
        return title;
    }

    public void addItem(Item item){
        items.add(item);
    }
    public void deleteItem(int itemPos){
        items.remove(itemPos);
    }

}
