package com.android.melanieh.dignitymemorialandroid;

/**
 * Created by melanieh on 5/9/17.
 */

public class FAQ {

    private String question;
    private String answer;

    public FAQ(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
