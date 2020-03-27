import java.sql.*;
import java.util.ArrayList;
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
                items.add(new Item(iid, name, price));
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

}
