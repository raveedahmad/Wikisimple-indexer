package clean;

//needed libraries
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

//main class
public class Clean {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        //reading the file that is to be indexed
        File file = new File("D:\\Project data\\corpus.txt");
        BufferedReader br = new BufferedReader(new FileReader(file)); 
        
        //all common word that can't distinguish betweem two articles
        String[] uw={"under","will","would","shall","aux","could","with","then","around","through","these","is","the","th","who","into","and","as","its","each","which","can","also","those","for","there","to","have","has","of","than","off","them","in","it","on","at","an","other","all","some","that","from","was","this","were","by","when","or","a","be","such","not","do","but","his","are"};
        //converting common words array to list
        List <String>wordList = new ArrayList<>(Arrays.asList(uw));
        
        uw=null;
        
        
        String[] words,t;//array for spliting words
        ArrayList <String>list;//array list for storing the splited words for futher operations
        LinkedList <wordRank> link = new LinkedList();//linked list for storing the words and their positions in hash tables
        
        //hash table for forward indexing
        Hashtable<String,LinkedList> forward;
        forward=new<String,LinkedList> Hashtable();
        
        String output="";//string for storing the content of an article
        String lineOfText;//string for reading a line of text from the file
        lineOfText=br.readLine();
        int count=0;
        //loop for the forward indexing
        while (lineOfText != null) {
            String title2;
            String title=lineOfText;//reading title
            title2=title;
            output+=title;
            //reading the content of a article and storing in a single string
            lineOfText=br.readLine();
            while ( lineOfText != null && !lineOfText.isEmpty()){
                output+=(lineOfText)+" ";
                lineOfText=br.readLine();
            }
            
            //removing the symbols from the string of content but retaining the decimal points and '-' between words and then converting the whole text to lowercase
            output=output.replaceAll("[^a-zA-Z0-9.\\s]|\\.(?!\\d)|(?<!\\w)-(?!\\w)|(?<!\\w)_(?!\\w)|\\-(?!\\d)", "");
            output=output.toLowerCase();
            output=output.replaceAll("\\s{2,}", " ").trim();
            output=output.replaceAll("_"," ");
            output=output.replaceAll("-"," ");
            
            title2=title2.replaceAll("[^a-zA-Z0-9.\\s]|\\.(?!\\d)|(?<!\\w)-(?!\\w)|(?<!\\w)_(?!\\w)|\\-(?!\\d)", "");
            title2=title2.toLowerCase();
            title2=title2.replaceAll("\\s{2,}", " ").trim();
            title2=title2.replaceAll("_"," ");
            title2=title2.replaceAll("-"," ");
            
            t=title2.split(" ");
            
            //split the whole text and storing a array
            words=output.split(" ");
            
            //storing the array to ArrayList for making the removal and count of duplicate and unwanted words easy
            list = new ArrayList<>(Arrays.asList(words));
            
            //removing all duplicate and common words 
            list.removeAll(wordList);
            list=removeDuplicates(list);
            
            //adding wordRank of every word
            for (String key:list){
                link.add(new wordRank(findPos(key,words,t.length),key));
            }
            //putting the title and the link list of wordRank of all words in the article
            forward.put(title, (LinkedList) link.clone());
            count++;
            //clearing data
            words=null;
            list=null;
            output="";
            link.clear();
            //getting line for next pass
            if(lineOfText != null)
                lineOfText=br.readLine();
        }
        //clearing all the data that is not usefull further
        lineOfText=null;
        br.close();
        //calling garbage collector
        System.gc();
        
        LinkedList <wordRank> link1 = new <wordRank>LinkedList();
        //geting the keys of the forward hash table 
        Set<String> keys = forward.keySet();
        //backward indexing
        //for all articles
        System.out.println("Backward");
        for(String key:keys){
            //get linked list of all the wordRank for an article
            link=forward.get(key);
            if(count%100==0)
                System.out.println(count);
            for(int i=0;i<link.size();i++){
                String savestr = "D:\\Project data\\backward1\\"+link.get(i).getWord()+".txt"; 
                File f = new File(savestr);
                if (!f.exists()){
                    f.createNewFile();
                }
                BufferedWriter out = new BufferedWriter(new FileWriter(new File(savestr), true));
                out.append(key+" "+link.get(i).getPos());
                out.newLine();
                out.close();
            }
            count--;
        }
        keys=null;
        System.gc();//garbage collector
    }
    
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) //removeing the duplicate words
    {
        // Create a new ArrayList 
        ArrayList<T> newList = new ArrayList<T>(); 

        // Traverse through the first list 
        for (T element : list) { 
  
            if (!newList.contains(element)) { 
  
                newList.add(element); 
            } 
        } 
  
        // return the new list 
        return newList; 
    }
    public static float findPos(String word,String[] array,int title){//finding the postions of specific word an returing linkedList
        float pos=0;
        for (int i=0;i<array.length;i++){
            if (word.equals(array[i])){
                if(i<title){
                    pos+=5000;
                }else
                    pos+=2000/(i+1);
                //System.out.println(pos);
            }
        }
        pos = Math.round(pos*100)/100;
        //System.out.println(pos);
        return pos;
    }
    
}

    
