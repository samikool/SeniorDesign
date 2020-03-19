import java.util.HashMap;

public class Receipt {

    private int tid;    //tableid
    private int wid;    //waiterid
    private double total;
    private HashMap<Integer, Integer> itemsOnReceipt; //<itemID, quantity>
    private HashMap<Integer, Item> itemInfo;

    public Receipt(int tid, int wid){
        this.tid = tid;
        this.wid = wid;
        total = 0;
        itemsOnReceipt = new HashMap<>();
        itemInfo = new HashMap<>();
    }

    public void addItem(Item item, int quant) {
        if (!itemsOnReceipt.containsKey(item.getId())) {
            itemsOnReceipt.put(item.getId(), quant);
        }
        else {
            itemsOnReceipt.put(item.getId(), itemsOnReceipt.get(item.getId()) + quant);
        }
        total += item.getPrice() * quant;

        if(!itemInfo.containsKey(item.getId())){
            itemInfo.put(item.getId(), item);
        }
    }

    public void removeItem(Item item, int quant) {
        if(itemsOnReceipt.containsKey(item.getId())){
            itemsOnReceipt.put(item.getId(), itemsOnReceipt.get(item.getId()) - quant);
            total -= item.getPrice() * quant;
        }

        if(!itemsOnReceipt.containsKey(item.getId())){
            itemInfo.remove(item.getId());
        }
    }

    public double getTotal(){
        return Math.round(100 * total) / 100.;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }
}
