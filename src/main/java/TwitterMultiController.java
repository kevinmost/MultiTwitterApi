import org.apache.log4j.Logger;
import twitter4j.Twitter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kevin
 * @date 8/14/14
 */
public class TwitterMultiController {

    private static Logger LOGGER = Logger.getLogger(TwitterMultiController.class);

    private List<SingleTwitterClient> twitterClients = new ArrayList<SingleTwitterClient>();

    /**
     * Creates a TwitterMultiController using the tokens.xml file passed in
     * @param xmlFile a tokens.xml file that contains all of your tokens, properly formatted
     */
    public TwitterMultiController(File xmlFile) {
        // For each Twitter client that we parse out of the XML, we create a new SingleTwitterClient object
        for (Twitter twitterClient : TokenXmlTwitterBuilder.buildTwitterClientsFromXml(xmlFile)) {
            twitterClients.add(new SingleTwitterClient(twitterClient));
        }
    }

    /**
     * @return the first Twitter object within this TwitterMultiController that isn't rate-limited. If all are rate-limited, returns null
     */
    public Twitter getValidTwitterClient() {
        int timeUntilNextTokenIsValid = Integer.MAX_VALUE;
        for (SingleTwitterClient twitterClient : twitterClients) {
            // Calculate how many seconds until this token is valid
            int timeUntilThisTokenIsValid = (twitterClient.getTokenValidTimestamp() - (int)(System.currentTimeMillis()/1000));

            // If this is the smallest time-until-valid that we've encountered thus far, record how long it will be until this token is valid
            if (timeUntilThisTokenIsValid < timeUntilNextTokenIsValid) timeUntilNextTokenIsValid = timeUntilThisTokenIsValid;

            // If we come across a token that is already valid, return it to the user
            if (timeUntilThisTokenIsValid <= 0) return twitterClient.getTwitter();
        }

        // If none of our tokens were valid, tell the user how long until one will be valid again and then return null
        LOGGER.warn("All of your Twitter clients are rate-limited. Another client will be available and non-rate-limited in " + timeUntilNextTokenIsValid + " seconds.");
        return null;
    }
}