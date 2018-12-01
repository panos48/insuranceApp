public class Vehicle implements Comparable{

    private String carPlate;
    private String model;

    public String getPlate() {
        return carPlate;
    }

    public void setPlate(String plate) {
        this.carPlate = plate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public int compareTo(Object o) {
        Vehicle other = (Vehicle) o;
        return this.carPlate.compareTo(other.carPlate);
    }
}
