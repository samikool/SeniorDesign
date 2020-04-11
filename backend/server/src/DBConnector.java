

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

public class DBConnector {
    private Connection connection;
    private Statement stmt;
    private ResultSet rs;
    public DBConnector(){
        Connection c = null;
        try{
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/seniordesign",
                    "postgres",
                    "password");
            stmt = connection.createStatement();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    private Item getItem(int id, String table){
        try{
            rs = stmt.executeQuery("SELECT * FROM " + table + " WHERE id = " + id);

            rs.next();
            int iid = rs.getInt("id");
            String name = rs.getString("name");
            double price = rs.getDouble("price");
            if(table.equals("bbqs")){
                String encodedImage = new String(rs.getBytes("picture"));
                byte[] imageBytes = Base64.getDecoder().decode(encodedImage);
                rs.close();
                return new Item(iid, name, price, encodedImage);
            }

            rs.close();
            return new Item(iid, name, price);

        }catch (SQLException e){
            System.err.println(e);
        }

        return null;
    }

    private ArrayList<Item> getItems(int[] ids, String table){
        ArrayList<Item> items = new ArrayList<Item>();
        for (int iid : ids) {
            items.add(getItem(iid, table));
        }

        return items;
    }

    private ArrayList<Item> getAllItems(String table){
        try{
            rs = stmt.executeQuery("SELECT * FROM " + table);
            ArrayList<Item> items = new ArrayList<>();
            while(rs.next()){
                int iid = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String encodedImage = new String(rs.getBytes("picture"));
                items.add(new Item(iid, name, price, encodedImage));
            }
            return items;
        }catch (SQLException e){
            System.err.println(e);
        }
        return null;
    }

    public void writeReceipt(Receipt receipt){
        try{
            ArrayList<Item> items = receipt.getItems();
            String itemString = "ARRAY[";
            for (Item item : items) {
                itemString += "ROW('" + item.getName() + "', " + item.getPrice() + ", " +receipt.getItemCount(item) + ")::item,";
            }
            itemString = itemString.substring(0, itemString.length()-1);
            itemString += "]";
            stmt.execute("INSERT INTO receipts (TABLEID, TOTAL, ITEMS) VALUES " +
                    "(" + receipt.getTid() + ", " + receipt.getTotal() + ", " + itemString + ")");
        }catch (SQLException e){
            System.err.println(e);
        }

    }

    public Item getBBQ(int id){
        return getItem(id, "bbqs");
    }

    public ArrayList<Item> getBBQs(int[] ids){
        return getItems(ids, "bbqs");
    }

    public ArrayList<Item> getAllBBQs(){
        return getAllItems("bbqs");
    }

    public Item getDrink(int id){
        return getItem(id, "drinks");
    }

    public ArrayList<Item> getDrinks(int[] ids){
        return getItems(ids, "drinks");
    }

    public ArrayList<Item> getAllDrinks(){
        return getAllItems("drinks");
    }

    public Item getSide(int id){
        return getItem(id, "sides");
    }

    public ArrayList<Item> getAllSides(){
        return getAllItems("sides");
    }

    public void insertImages(){
        //BBQ CAT = 0
        insertImage("samgyupsal", 0);
        insertImage("wanggalbi", 0);
        insertImage("bulgogi", 0);
        insertImage("galbi", 0);
        insertImage("buldak", 0);
        insertImage("chadolbaki", 0);
        //DRINK CAT = 1
        insertImage("water", 1);
        insertImage("pepsi", 1);
        insertImage("diet_pepsi", 1);
        insertImage("root_beer", 1);
        insertImage("mt._dew", 1);
        insertImage("lemonade", 1);
        insertImage("seirra_mist", 1);
        insertImage("rammune", 1);
        insertImage("white_milk", 1);
        insertImage("chocolate_milk", 1);
        insertImage("strawberry_milk", 1);
        //SIDES CAT = 2
        insertImage("kimchi", 2);
        insertImage("potato_salad", 2);
        insertImage("black_beans", 2);
        insertImage("bean_sprouts", 2);
        insertImage("pink_radish", 2);
        insertImage("white_radish", 2);
        insertImage("yellow_radish", 2);
        insertImage("spinach", 2);
    }
    private void insertImage(String itemName, int category){
        String table = "";
        if(category == 0)
            table = "bbqs";
        else if(category == 1)
            table = "drinks";
        else if(category == 2)
            table = "sides";
        try{
            //String dir = "E:\\git\\seniordesign\\backend\\";
            String dir = "C:\\Users\\Sam\\git\\seniordesign\\backend\\";
            BufferedImage image = ImageIO.read(new File(dir,"samgyupsal" + ".jpg"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            String base64String = Base64.getEncoder().encodeToString(baos.toByteArray());
            baos.close();

            String queryStr = "UPDATE " + table + " SET picture = bytea('" + base64String + "') WHERE name = '" + itemName + "'";
            stmt.execute(queryStr);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
