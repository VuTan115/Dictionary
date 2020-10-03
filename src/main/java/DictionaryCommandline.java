import javax.sound.midi.Soundbank;
import java.util.Scanner;

public class DictionaryCommandline {
    public void showAllWords() {
        System.out.println("No\t|" + "English" + "\t\t|VietNamese ");
        int i = 0;
        for (Word word : Dictionary.wordArray) {
            if (word != null) {
                System.out.println((i + 1) + "\t|" + "" + word.getWord_target() + "\t\t|=>" + word.getWord_explain());
                ++i;
            }
        }

    }

    public void dictionarySearcher() {
        Scanner input = new Scanner(System.in);
        String find = input.nextLine();
        for (Word word : Dictionary.wordArray) {
            if (word != null) {
                if (word.getWord_target().contains(find)) {
                    //System.out.println(word.getWord_target() + " " + word.getWord_explain());
                    System.out.println(word.getWord_target());
                }
            } else break;

        }

    }


    public void dictionaryBasic() {

        DictionaryManagement pilot = new DictionaryManagement();
        pilot.insertFromCommandline();
        showAllWords();
    }

    public void dictionaryAdvanced() {
        DictionaryManagement pilot = new DictionaryManagement();
        Scanner input = new Scanner(System.in);
        pilot.insertFromFile();
        System.out.println("\t\t\t\t-----------------------------------------------------------");
        System.out.println("\n\t\t\t\toYour English-Vietnamese Dictionaryo\n");
        System.out.println("\t\t\t\t-----------------------------------------------------------");
        System.out.println(" \t\t\t\tOptions: \n \t\t\t\t1:Show all Word\n \t\t\t\t2:Lookup\n " +
                "\t\t\t\t3:Quick Lookup\n \t\t\t\t4:Exit");
        System.out.println("\t\t\t\tChoose your option >");
        switch (input.nextInt()) {
            case 1:
                System.out.println("\t\t\t\tAll word in your Dictionary\n");
                showAllWords();
                dictionaryAdvanced();
                break;
            case 2:
                System.out.println("\t\tSearch function\n");
                pilot.dictionaryLookup();
                dictionaryAdvanced();
                break;
            case 3:
                System.out.println("\t\t\t\tQuick lookup");
                dictionarySearcher();
                dictionaryAdvanced();
                break;
            case 4:
                System.out.println("\t\t\t\tExited");
                break;
            default:
                System.out.println("Wrong option! =￣ω￣=\n Choose again pls!");
                dictionaryAdvanced();
        }


    }

    public static void main(String[] args) {

        DictionaryCommandline pilot = new DictionaryCommandline();
        //pilot.dictionaryBasic();

        pilot.dictionaryAdvanced();
        DictionaryManagement test = new DictionaryManagement();
        //test.insertFromCommandline();

    }
}
