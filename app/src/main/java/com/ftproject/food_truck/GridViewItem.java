package com.ftproject.food_truck;

import android.widget.ImageView;

/**
 * Created by JeongHwan on 2017-05-29.
 */
public class GridViewItem {
    String name;
    String price;
   // ImageView imgPhoto;
   // int imgPhoto;
    String imgURL;

    public GridViewItem(String name, String price,
                        //ImageView imgPhoto
                        //int imgPhoto
                        String imgURL) {

        this.name = name;
        this.price = price;
        //this.imgPhoto = imgPhoto;
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

    /*public /*ImageViewint getImgPhoto() {
        return imgPhoto;
    }*/
    public String getImgURL(){
        return imgURL;
    }

    /*public void setImgPhoto(/*ImageView imgPhoto int imgPhoto) {
        this.imgPhoto = imgPhoto;
    }*/

    public void setImgURL(String imgURL){
        this.imgURL=imgURL;
    }
}