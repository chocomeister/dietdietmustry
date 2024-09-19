package com.example.dietandnutritionapplication;

import java.io.Serializable;

public class FAQ implements Serializable {
    private String title;
    private String question;
    private String answer;
    private String dateCreated;

    public FAQ(){

    }

    public FAQ(String title, String question, String answer, String dateCreated) {
        this.title = title;
        this.question = question;
        this.answer = answer;
        this.dateCreated = dateCreated;
    }

    public String getTitle() { return title;
    }

    public void setTitle(String title) { this.title = title;
    }

    public String getQuestion() { return question;
    }

    public void setQuestion(String question) { this.question = question;
    }

    public String getAnswer() { return answer;
    }

    public void setAnswer(String answer) { this.answer = answer;
    }

    public String getDateCreated() { return dateCreated;
    }

    public void setDateCreated(String dateCreated) { this.dateCreated = dateCreated;
    }
}


