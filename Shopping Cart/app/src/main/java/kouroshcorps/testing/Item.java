package kouroshcorps.testing;

import java.util.Date;

/**
 * Created by kourosh on 2016-03-28.
 */
public class Item {
    private String name;
    protected boolean purchased = false;


    public Item(String name,boolean purchased){
        this.name = name;
        this.purchased = purchased;
    }
    public String getName(){
        return name;
    }
}
