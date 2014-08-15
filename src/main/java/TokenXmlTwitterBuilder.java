import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.ConfigurationNode;
import org.apache.log4j.Logger;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Takes an XML input of Twitter tokens as input and outputs a List<Twitter>
 * @author kevin
 * @date 8/14/14
 */
public class TokenXmlTwitterBuilder {

    private static Logger LOGGER = Logger.getLogger(TokenXmlTwitterBuilder.class);


    /**
     * Given an XML file containing the Twitter tokens and their 4 parameters, returns a List of Twitter objects
     * @param xmlFile The XML input
     */
    public static List<Twitter> buildTwitterClientsFromXml(File xmlFile) {
        List<Twitter> twitterClients = new ArrayList<Twitter>();

        XMLConfiguration twitterTokens = null;
        // Open XML token file
        try {
            twitterTokens = new XMLConfiguration(xmlFile);
        } catch (ConfigurationException e) {
            LOGGER.fatal("Could not open token .xml file");
        }


        // Adds each token from the XML to twitterClients
        for (ConfigurationNode token : twitterTokens.getRoot().getChildren("token")) { // For each token object in the XML...
            ConfigurationBuilder thisTwitterBuilder = new ConfigurationBuilder();

            LOGGER.debug("Adding new token with following parameters: ");
            for (ConfigurationNode tokenElement : token.getChildren()) {
                LOGGER.debug(tokenElement.getName() + ": " + tokenElement.getValue().toString());
            }
            thisTwitterBuilder.setOAuthConsumerKey(token.getChild(0).getValue().toString());
            thisTwitterBuilder.setOAuthConsumerSecret(token.getChild(1).getValue().toString());
            thisTwitterBuilder.setOAuthAccessToken(token.getChild(2).getValue().toString());
            thisTwitterBuilder.setOAuthAccessTokenSecret(token.getChild(3).getValue().toString());

            // Creates the Twitter client for this token and tests it. If it is valid, adds it to the list of clients we have
            Twitter thisTwitterClient = new TwitterFactory(thisTwitterBuilder.build()).getInstance();
            try {
                thisTwitterClient.verifyCredentials();
                twitterClients.add(thisTwitterClient);
            } catch (TwitterException e) {
                switch (e.getErrorCode()) {
                    case 88: // Rate-limit error
                        LOGGER.info("Token " + token.getChild(2).getValue().toString() + " is rate-limited and will not be used");
                        break;
                    default:
                        LOGGER.warn("Token " + token.getChild(2).getValue().toString() + " is invalid. Please remove it from your XML. Reason: " + e.getErrorMessage());
                        break;
                }
            }
        }

        // Log the number of tokens that were added. Logs as INFO if everything worked, otherwise logs as WARN
        int numBadTokens;
        if ((numBadTokens = twitterTokens.getRoot().getChildren("token").size() - twitterClients.size()) == 0) {
            LOGGER.info("Successfully added all " + twitterClients.size() + " tokens!");
        } else {
            LOGGER.warn("Could not add " + numBadTokens + " to your config. Please check this log for the token(s) that were invalid and the error code given.");
        }

        return twitterClients;
    }
}
