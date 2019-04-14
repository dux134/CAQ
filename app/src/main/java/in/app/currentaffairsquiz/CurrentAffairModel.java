package in.app.currentaffairsquiz;

public class CurrentAffairModel {
    private String title,date,questions;
    int time;

    public CurrentAffairModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "CurrentAffairModel{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", questions='" + questions + '\'' +
                '}';
    }
}
