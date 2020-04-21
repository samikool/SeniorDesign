import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Receipt {

    private int tid;    //tableid
    private int wid;    //waiterid
    private double total;
    ArrayList<Item> items;
    ArrayList<Integer> quantities;
    public Receipt(int tid, int wid){
        this.tid = tid;
        this.wid = wid;
        total = 0;
        items = new ArrayList<>();
        quantities = new ArrayList<>();
    }

    public Receipt(int tid){
        this(tid, -1);
    }

    public void addItem(Item item, int quant) {
        if(items.contains(item)){
            int i = items.indexOf(item);
            quantities.set(i, quantities.get(i)+quant);
        }else{
            items.add(item);
            quantities.add(quant);
        }
        total += item.getPrice() * quant;
    }

    public void removeItem(Item item, int quant) {
        if(items.contains(item)){
            int i = items.indexOf(item);
            if(quantities.get(i) >= quant){
                quantities.set(i, quantities.get(i) - quant);
                total -= item.getPrice() * quant;
            }
        }
    }

    public void removeAllItems(){
        total = 0;
        items = new ArrayList<>();
        quantities = new ArrayList<>();
    }

    public int getItemCount(Item item){
        if(items.contains(item)){
            int i =items.indexOf(item);
            return quantities.get(i);
        }else{
            return -1;
        }
    }

    public int getNumItems(){
        int count = 0;
        for (int quant: quantities){
            count+=quant;
        }
        return count;
    }

    public ArrayList<Item> getItems(){return items;}

    public double getTotal(){return Math.round(100 * total) / 100.;}

    public int getTid() {return tid;}

    public void setTid(int tid) {this.tid = tid;}

    public int getWid() {return wid;}

    public void setWid(int wid) {this.wid = wid;}

    @Override
    public String toString(){
        String str = "Table: " + tid + "    Waiter: " + wid + "\n";
        for (int i=0; i<items.size(); i++) {
            str += quantities.get(i) + "   " + items.get(i).getName() + "   " + items.get(i).getPrice() * quantities.get(i) + "\n";
        }
        str += "        Total: " + getTotal() + "\n";
        return str;
    }
}
