public class Player 
{
    private String name;
    private String sumName;
    private String lane;
    private String country;

    public Player(String name, String sumName, String lane, String country) {
        this.name = name;
        this.sumName = sumName;
        this.lane = lane;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSumName() {
        return sumName;
    }

    public void setSumName(String sumName) {
        this.sumName = sumName;
    }

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
