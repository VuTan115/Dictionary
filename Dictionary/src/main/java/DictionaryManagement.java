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

    public static int getSize() {
        int size = 0;
        for (Word words : wordArray) {
            if (words != null) {
                size++;
            }
        }
        return size;
    }

    /**
     * insert function .
     */
    public void insertFromCommandline() {
        System.out.print("\t\t\t\tHow many Word you wanna add? >");
        int num=input.nextInt();
        input.nextLine();
        for (int i = 0; i < num; i++) {
            Word creat = new Word();

            System.out.print("\t\t\t\tThe " + (i + 1) + " English " + "word >");
            creat.setWord_target(input.nextLine());
            System.out.print("\t\t\t\tExplain meaning of the " + (i + 1) + " word >");
            creat.setWord_explain(input.nextLine());
            wordArray[getSize()] = new Word(creat.getWord_target(), creat.getWord_explain());
        }
        rewriteFile("src\\main\\java\\data.txt");
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
        for (int i = 0; i < getSize(); i++) {
            if (wordArray[i].getWord_target().equals(search)) {
                System.out.printf("\t\t\t\tResult: " + wordArray[i].getWord_explain() + "\n");
                return;
            }
        }
        System.out.println("\t\t\t\tThere is no word like that! ");
    }

    public void deleteWord() {
        System.out.print("\t\t\t\tType word to delete here>");
        String del = input.nextLine();
        if (wordArray[0] == null) {
            System.out.println("\t\t\t\tDictionary is empty");
            return;
        }
        for (int i = 0; i < getSize(); i++) {
            if (wordArray[i].getWord_target().equals(del)) {
                wordArray[i] = null;
                break;
            }
        }
        rewriteFile("src\\main\\java\\data.txt");

    }

    public void rewriteFile(String path) {
        try {
            File file = new File(path);
            FileWriter writer = new FileWriter(file);
            if (!file.exists()) {
                file.createNewFile();
            }
            String allwords = "";
            for (int i = 0; i < getSize(); i++) {
                if (wordArray[i] != null) {
                    allwords += wordArray[i].getWord_target() + "\t" + wordArray[i].getWord_explain() + "\n";
                }
            }
            writer.write(allwords);
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    public void dictionaryExportToFile() {
        rewriteFile("src\\main\\java\\data1.txt");
        System.out.println("\t\t\t\tDictionary has been export to > src\\main\\java\\data1.txt");
    }

}

