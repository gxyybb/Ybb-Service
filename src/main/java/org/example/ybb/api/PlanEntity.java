package org.example.ybb.api;
public class PlanEntity {
    private String token;
    private Integer subject;
    private int countByDay;
    public PlanEntity(){

    }
    public PlanEntity(String token, Integer subject, int countByDay) {
        this.token = token;
        this.subject = subject;
        this.countByDay = countByDay;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getSubject() {
        return subject;
    }

    public void setSubject(Integer subject) {
        this.subject = subject;
    }

    public int getCountByDay() {
        return countByDay;
    }

    public void setCountByDay(int countByDay) {
        this.countByDay = countByDay;
    }

    @Override
    public String toString() {
        return "PlanEntity{" +
                "token='" + token + '\'' +
                ", subject='" + subject + '\'' +
                ", countByDay=" + countByDay +
                '}';
    }
}
