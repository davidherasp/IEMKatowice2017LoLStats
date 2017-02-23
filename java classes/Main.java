import com.robrua.orianna.api.core.RiotAPI;
import java.io.IOException;

public class Main 
{
    public static void main(String[] args) throws IOException 
    {
    	// Set your API key here if you want the recent soloQ data from the players. You can also set the api key as a run parameter
        RiotAPI.setAPIKey(args[0]);
        DAO dao = new DAO();
        
        // export as a CSV the recent games of the players (NOT LCS GAMES)
        dao.getRecentGamesGamesCSV(dao.getTeams());
        
        // export as a CSV the games played at LCS before IEM Katowice 2017
        dao.getLCSGamesCSV(dao.getTeams());
    }
}
