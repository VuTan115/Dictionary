public class Dictionary {
    public static Word[] wordArray;
    public static int size;
    public Dictionary(int size) {

        wordArray = new Word[size];
        //initialWordArray();
    }
    public void initialWordArray(){
        for (int i = 0; i < wordArray.length; i++) {
            wordArray[i]=new Word();
        }
    }

}
