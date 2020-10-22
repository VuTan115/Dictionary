package main.java.MainPackage;

import com.gtranslate.Audio;
import com.gtranslate.Language;
import com.gtranslate.Translator;
import javazoom.jl.decoder.JavaLayerException;

import java.io.IOException;
import java.io.InputStream;


public class APITranslate {

    public static String Translate(String s) {
        Translator translate = Translator.getInstance();
        String text = translate.translate(s + "&client=tw-ob", Language.ENGLISH, Language.VIETNAMESE);
        return text;
    }

    public static void SpeakAPI(String word) {
        try {
            Audio audio = Audio.getInstance();
            InputStream sound = audio.getAudio(word + "&client=tw-ob", Language.ENGLISH);
            audio.play(sound);
        } catch (IOException | JavaLayerException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void main(String[] args) {
        SpeakAPI("chicken");
        System.out.println(Translate("hello"));
    }
}
