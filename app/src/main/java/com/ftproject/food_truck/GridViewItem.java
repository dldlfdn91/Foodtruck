package com.ftproject.food_truck;

import android.widget.ImageView;

public class GridViewItem {
    String name;
    String price;
    String imgURL;

    public GridViewItem(String name, String price, String imgURL) {
        this.name = name;
        this.price = price;
        this.imgURL =imgURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImgURL(){
        return imgURL;
    }

    public void setImgURL(String imgURL){
        this.imgURL=imgURL;
    }
}
