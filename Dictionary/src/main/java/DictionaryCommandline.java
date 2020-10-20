
import java.util.Scanner;

public class DictionaryCommandline {
    DictionaryManagement pilot = new DictionaryManagement();

    public void showAllWords() {
        System.out.println("\t\t\t\tNo\t|" + "English" + "\t\t\t  | VietNamese ");
        int i = 0;
        for (Word word : DictionaryManagement.wordArray) {
            if (word != null) {
                System.out.print("\t\t\t\t"+(i+1)+"\t| ");
                System.out.printf("%-20s| ",  word.getWord_target());
                System.out.print( word.getWord_explain()+"\n");
                ++i;
            }
        }

    }

    public void dictionarySearcher() {
        Scanner input = new Scanner(System.in);
        String find = input.nextLine();
        for (Word word : DictionaryManagement.wordArray) {
            if (word != null) {
                if (word.getWord_target().contains(find)) {
                    System.out.println("\t\t\t\t\t"+word.getWord_target());
                }
            } else break;
        }

    }


    public void dictionaryBasic() {
        pilot.insertFromCommandline();
        showAllWords();
    }

    public void dictionaryAdvanced() {
        Scanner input = new Scanner(System.in);
        pilot.insertFromFile();
        System.out.println("\t\t\t\t===========================================================");
        System.out.println("\n\t\t\t\t\t\t\tYour English-Vietnamese Dictionary\n");
        System.out.println("\t\t\t\t===========================================================");
        System.out.println(" \t\t\t\tOptions: \n "
                + "\t\t\t\t1:Show all Word\n"
                + "\t\t\t\t2:Lookup\n"
                + "\t\t\t\t3:Quick Lookup\n"
                + "\t\t\t\t4:Delete Word\n"
                + "\t\t\t\t5:Export to file\n"
                + "\t\t\t\t6:Add word\n"
                + "\t\t\t\t7:Rewrite meaning\n"
                + "\t\t\t\t8:Exit");
        System.out.print("\t\t\t\tChoose your option >");
        switch (input.nextInt()) {
            case 1:
                System.out.println("\n\t\t\t\t\t\tAll word in your Dictionary\n");
                showAllWords();
                dictionaryAdvanced();
                break;
            case 2:
                pilot.dictionaryLookup();
                dictionaryAdvanced();
                break;
            case 3:
                System.out.println("\t\t\t\tQuick lookup");
                dictionarySearcher();
                dictionaryAdvanced();
                break;
            case 4:
                pilot.deleteWord();
                dictionaryAdvanced();
                break;
            case 5:
                pilot.dictionaryExportToFile();
                dictionaryAdvanced();
                break;
            case 6:
                pilot.addWord();
                dictionaryAdvanced();

            case 8:
                System.out.println("\t\t\t\tExited");
                break;

            default:
                System.out.println("Wrong option! =￣ω￣=\n Choose again pls!");
                dictionaryAdvanced();
        }


    }

    public static void main(String[] args) {

        DictionaryCommandline pilot = new DictionaryCommandline();
        pilot.dictionaryAdvanced();


    }
}
