import com.google.gson.Gson;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;


/**
 * Manager the dictionary.
 */
public class DictionaryManagement {
    //ArrayList<Word> dictionary = new ArrayList<Word>();
    public static int arraySize;
    //public Dictionary word = new Dictionary(arraySize);

    /**
     * insert function .
     */
    public void insertFromCommandline() {
        Scanner input = new Scanner(System.in);
        System.out.println("How many Word you wanna translate? ");
        System.out.print(">");

        arraySize = input.nextInt();
        Dictionary array = new Dictionary(arraySize);
        input.nextLine();
        for (int i = 0; i < DictionaryManagement.arraySize; i++) {
            Word creat = new Word();
            System.out.println("The " + (i + 1) + " English " + "word ");
            creat.setWord_target(input.nextLine());
            System.out.println("Explain meaning of the " + (i + 1) + " word ");
            creat.setWord_explain(input.nextLine());
            Dictionary.wordArray[i] = new Word(creat.getWord_target(), creat.getWord_explain());
        }


    }

    public void insertFromFile() {
        try {

            File file = new File("src\\main\\java\\data.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            Scanner read = new Scanner(file);
            Dictionary arr = new Dictionary(100);
            int i = 0;
            while (read.hasNextLine()) {
                String[] line = read.nextLine().split("\t");
                if (Arrays.toString(line).compareTo("[]") != 0) {
                    Dictionary.wordArray[i] = new Word(line[0], line[1]);
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
        System.out.println("Search for >");
        Scanner input = new Scanner(System.in);
        String search = input.nextLine();
        for (Word ans : Dictionary.wordArray) {
            if (ans.getWord_target().equals(search)) {
                System.out.printf("Result: " + ans.getWord_explain());
                break;
            } else {
                System.out.println("There is no word like that! ");
                break;
            }

        }

    }

    public void dictionaryExportToFile() {

        try {

            FileOutputStream outFile = new FileOutputStream("data1.txt");
            ObjectOutputStream writer = new ObjectOutputStream(outFile);

            for (Word word : Dictionary.wordArray) {
                writer.writeObject(word);
            }
            outFile.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

