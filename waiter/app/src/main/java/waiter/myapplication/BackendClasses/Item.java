package waiter.myapplication.BackendClasses;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.Base64;

import waiter.myapplication.ItemType;

public class Item {
    private int id;
    private double price;
    private String name;
    private String displayName;
    private String encodedImage;
    private Bitmap image;
    private ItemType itemType;

    public Item(){
        this.id=-1;
        this.name=null;
    }

    public Item(int id, String name, double price){
        this.name = name;
        displayName = "";
        for(int i=0; i<name.length(); i++){
            if(i==0){
                displayName += name.substring(0,1).toUpperCase();
            }
            else if(name.charAt(i) == '_'){
                displayName += " ";
            }
            else if(i !=0 && name.charAt(i-1) == 95){
                displayName += name.substring(i, i+1).toUpperCase();
            }
            else{
                displayName+= name.charAt(i);
            }
        }
        this.id = id;
        this.price = price;
    }

    public Item(int id, String name, double price, String encodedImage){
        this(id, name, price);
        this.encodedImage = encodedImage;
        byte[] imageBytes = Base64.getDecoder().decode(encodedImage);
        //java
//        try{
//            image = ImageIO.read(new ByteArrayInputStream(imageBytes));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        //android
        image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        image = Bitmap.createScaledBitmap(image, image.getWidth(), image.getHeight(),false);

    }

    public ItemType getItemType(){return itemType;}

    public ItemType setItemType(ItemType type){return this.itemType = type;}

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return displayName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getEncodedImage() {
        return encodedImage;
    }

    public void setEncodedImage(String encodedImage) {
        this.encodedImage = encodedImage;
    }

    @Override
    public boolean equals(Object object){
        Item item = (Item) object;
        if(this.getId() == (item).getId()
                && this.getPrice() == item.getPrice()
                && this.getName().equals(item.getName())){
            return true;
        }
        return false;
    }


    @Override
    public String toString(){
        return "ID: " + id + " Name: " + name + " Price: " + price;
    }

}


