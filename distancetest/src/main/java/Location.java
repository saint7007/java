import java.util.Map;

public class   Location {


    private String id;
    private int name;
    private Map<String, Double> location; //  Map
    private String countryName;
    private String countryId;
    private String regId;
    private String contId;
    private Integer con;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public Map<String, Double> getLocation() {
        return location;
    }

    public void setLocation(Map<String, Double> location) {
        this.location = location;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getContId() {
        return contId;
    }

    public void setContId(String contId) {
        this.contId = contId;
    }

    public Integer getCon() {
        return con;
    }

    public void setCon(Integer con) {
        this.con = con;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id='" + id + '\'' +
                ", name=" + name +
                ", location=" + location +
                ", countryName='" + countryName + '\'' +
                ", countryId='" + countryId + '\'' +
                ", regId='" + regId + '\'' +
                ", contId='" + contId + '\'' +
                ", con=" + con +
                '}';
    }
}
