import java.util.ArrayList;

public class test {
    public static void main(String args[]){
        ArrayList<String> test = new ArrayList<>();
        test.add("a");
        test.add("b");
        test.add("c");
        test.add("a");
        test.add("d");
        test.add("e");

        if(test.lastIndexOf("f") < 0){
            test.add("f");
            //save index
            System.out.println(test.size()-1);
        }
        //test.set(0,null);
        test.set(0, null);

        for(String string : test){
            if(string != null)
                System.out.println(string);
        }


    }
}
