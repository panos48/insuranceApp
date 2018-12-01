import java.util.Objects;

public class Owner {

    private String firstName;
    private String surName;
    private int driverLicense;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Owner owner = (Owner) o;
        return driverLicense == owner.driverLicense;
    }

    @Override
    public int hashCode() {
        return Objects.hash(driverLicense);
    }

    public int getDriverLicense() {
        return driverLicense;
    }

    public void setDriverLicense(int driverLicense) {
        this.driverLicense = driverLicense;
    }



    @Override
    public String toString() {
        return "Owner{" +
                "firstName='" + firstName + '\'' +
                ", surName='" + surName + '\'' +
                ", driverLicense=" + driverLicense +
                '}';
    }
}
