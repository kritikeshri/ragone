package dev.kriti.ragone.DTO;

import java.util.List;

public class RagAnswer {

    private String answer;
    private Boolean foundInContext;
    private List<String> followUpQuestions;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean getFoundInContext() {
        return foundInContext;
    }

    public void setFoundInContext(Boolean foundInContext) {
        this.foundInContext = foundInContext;
    }

    public List<String> getFollowUpQuestions() {
        return followUpQuestions;
    }

    public void setFollowUpQuestions(List<String> followUpQuestions) {
        this.followUpQuestions = followUpQuestions;
    }

}
