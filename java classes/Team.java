import java.util.Collection;

public class Team 
{
    private String abv;
    private String name;
    private String country;
    private String region;
    private Collection<Player> players;

    public Team(String abv, String name, String country, String region, Collection<Player> players) {
        this.abv = abv;
        this.name = name;
        this.country = country;
        this.region = region;
        this.players = players;
    }
    
    public String getAbv() {
        return abv;
    }

    public void setAbv(String abv) {
        this.abv = abv;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Collection<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Collection<Player> players) {
        this.players = players;
    }
        
}
