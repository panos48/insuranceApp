//date format
public class Insurance {

    private int insuranceID;
    private String startDateInsurance;
    private String expirationDateInsurance;

    public int getInsuranceID() {
        return insuranceID;
    }

    public void setInsuranceID(int insuranceID) {
        this.insuranceID = insuranceID;
    }

    public String getStartDateInsurance() {
        return startDateInsurance;
    }

    public void setStartDateInsurance(String startDateInsurance) {
        this.startDateInsurance = startDateInsurance;
    }

    public String getExpiredDateInsurance() {
        return expirationDateInsurance;
    }

    public void setExpiredDateInsurance(String expiredDateInsurance) {
        this.expirationDateInsurance = expiredDateInsurance;
    }
}
