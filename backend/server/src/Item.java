import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Base64;

public class Item {
    private int id;
    private double price;
    private String name;
    private String encodedImage;
    private BufferedImage image;

    public Item(int id, String name, double price){
        this.name = name;
        this.id = id;
        this.price = price;
    }

    public Item(int id, String name, double price, String encodedImage){
        this(id, name, price);
        this.encodedImage = encodedImage;
        byte[] imageBytes = Base64.getDecoder().decode(encodedImage);
        //java
        try{
            image = ImageIO.read(new ByteArrayInputStream(imageBytes));
        }catch (Exception e){
            e.printStackTrace();
        }
        //android
        //image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        //image = Bitmap.createScaledBitmap(image, image.getWidth(), image.getHeight(),false);

    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
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
