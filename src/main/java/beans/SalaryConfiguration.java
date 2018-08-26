package beans;

public class SalaryConfiguration {

    private Integer grade;
    private Double compRatio;
    private Double sti;
    private Double mti;

    public SalaryConfiguration(Integer grade, Double compRatio,
               Double sti, Double mti) {
        this.grade = grade;
        this.compRatio = compRatio;
        this.sti = sti;
        this.mti = mti;
    }

    public SalaryConfiguration() {}

    public Integer getGrade() {
        return grade;
    }

    public Double getCompRatio() {
        return compRatio;
    }

    public Double getSti() {
        return sti;
    }

    public Double getMti() {
        return mti;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public void setCompRatio(Double compRatio) {
        this.compRatio = compRatio;
    }

    public void setSti(Double sti) {
        this.sti = sti;
    }

    public void setMti(Double mti) {
        this.mti = mti;
    }
}
