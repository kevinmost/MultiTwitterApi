import twitter4j.TwitterException;

import java.io.File;

/**
 * @author kevin
 * @date 8/14/14
 */
public class TwitterTester {
    public static void main(String[] args) throws TwitterException {
        TwitterMultiController twitter = new TwitterMultiController(new File("res/tokens.xml"));

        for (int i = 0; i < 500; i++) {
            System.out.println(i + ": " + twitter.getValidTwitterClient().getFriendsIDs(1408128844));
        }
    }
}
