package sample;

import java.util.HashMap;
import java.util.Map;

public class RefactorDict {
    Map<String, String> dictionary = new HashMap<String, String>();
    String newWord = new String();

    public RefactorDict () {}

    public RefactorDict (Map<String, String> map) {
        dictionary = map;
    }

}
