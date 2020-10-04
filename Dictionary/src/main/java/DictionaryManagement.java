import com.google.gson.Gson;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;


/**
 * Manager the dictionary.
 */
public class DictionaryManagement {
    public Scanner input = new Scanner(System.in);
    public static Word[] wordArray = new Word[100000];

    /**
     * insert function .
     */
    public void insertFromCommandline() {
        System.out.println("\t\t\t\tHow many Word you wanna translate? ");
        System.out.print(">");
        for (int i = 0; i < wordArray.length; i++) {
            Word creat = new Word();
            System.out.println("\t\t\t\tThe " + (i + 1) + " English " + "word ");
            creat.setWord_target(input.nextLine());
            System.out.println("\t\t\t\tExplain meaning of the " + (i + 1) + " word ");
            creat.setWord_explain(input.nextLine());
            wordArray[i] = new Word(creat.getWord_target(), creat.getWord_explain());
        }


    }

    public void insertFromFile() {
        try {
            File file = new File("src\\main\\java\\data.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            Scanner read = new Scanner(file);
            int i = 0;
            while (read.hasNextLine()) {
                String[] line = read.nextLine().split("\t");
                if (Arrays.toString(line).compareTo("[]") != 0) {
                    wordArray[i] = new Word(line[0], line[1]);
                    ++i;
                }
            }
        } catch (IOException bug) {
            System.out.println("Loi doc file" + bug);
        }
    }

    public void insertFromFileJson() {
        try {
            Gson json = new Gson();
            File jsonFile = new File("E:\\\\Code\\JavaOOP\\\\RemakeDictionary\\\\src\\\\main" +
                    "\\\\java\\\\data.json");
            FileInputStream inFile = new FileInputStream(jsonFile);
            int c = inFile.read();
            String ans = "";
            while (c != -1) {
                //System.out.print((char) c);
                ans += (char) c;
                c = inFile.read();
            }
            //json.fromJson(ans);
            System.out.println(ans);
            inFile.close();
        } catch (IOException bug) {
            System.out.println("loi doc file " + bug);
        }

    }

    public void dictionaryLookup() {
        System.out.print("\t\t\t\tSearch for >");
        String search = input.nextLine();
        for (Word ans : wordArray) {
            if (ans.getWord_target().equals(search)) {
                System.out.printf("\t\t\t\tResult: " + ans.getWord_explain()+"\n");
                break;
            } else {
                System.out.println("\t\t\t\tThere is no word like that! ");
                break;
            }

        }

    }

    public void deleteWord() {
        System.out.print("\t\t\t\tType word to delete here>");
        String del = input.nextLine();
        if (wordArray[0] == null) {
            System.out.println("\t\t\t\tDictionary is empty");
            return;
        }
        for (int i = 0; i < wordArray.length; i++) {
            if (wordArray[i].getWord_target().equals(del)) {
                wordArray[i] = null;
                wordArray[wordArray.length]=null;
                break;
            }
        }
        try {

            File file = new File("src\\main\\java\\data.txt");
            FileWriter writer = new FileWriter(file);
            if (!file.exists()) {
                file.createNewFile();
            }
            String allwords = "";
            for (Word words : wordArray) {
                if (words != null) {
                    allwords += words.getWord_target() + "\t" + words.getWord_explain() + "\n";
                }

            }
            writer.write(allwords);
            writer.close();
        } catch (Exception ect) {
            ect.printStackTrace();
        }


    }

    public void dictionaryExportToFile() {
        try {
            File file = new File("src\\main\\java\\data1.txt");
            FileWriter writer = new FileWriter(file);
            if (!file.exists()) {
                file.createNewFile();
            }
            String allwords = "";
            for (Word words : wordArray) {
                if (words != null) {
                    allwords += words.getWord_target() + "\t" + words.getWord_explain() + "\n";
                }
            }
            writer.write(allwords);
            writer.close();
            System.out.println("\t\t\t\tDictionary has been export to > " + file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}

