public class Word {
    private String word_target, word_explain;

    public Word() {
        word_explain = "";
        word_target = "";
    }
    public Word(String word_target,String word_explain){
        this.setWord_explain(word_explain);
        this.setWord_target(word_target);
    }
    public String getWord_explain() {
        return word_explain;
    }

    public String getWord_target() {
        return word_target;
    }

    public void setWord_explain(String word_explain) {
        this.word_explain = word_explain;
    }

    public void setWord_target(String word_target) {
        this.word_target = word_target;
    }
}
