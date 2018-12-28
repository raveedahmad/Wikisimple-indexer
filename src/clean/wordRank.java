package clean;

public class wordRank {
    private float pos;
    private String word;
    wordRank(float pos, String word){
        this.pos=pos;
        this.word=word;
    }
    float getPos(){
        return this.pos;
    }
    String getWord(){
        return this.word;
    }
}
