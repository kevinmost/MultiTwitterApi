import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;
import twitter4j.Twitter;

/**
 * @author kevin
 * @date 8/14/14
 */
public class SingleTwitterClient {

    // The Twitter client bound to this object
    private Twitter twitter;

    // The amount of seconds that this token needs to sleep. A value <=0 means that this token is currently usable.
    private int tokenValidTimestamp;

    protected SingleTwitterClient(Twitter twitter) {
        this.twitter = twitter;
        this.tokenValidTimestamp = 0;

        addRateLimitListener();
    }

    /**
     * Adds a listener to this client's Twitter object. Every time an API call is made, it checks to see if it is rate-limited, and if it is, about to be, it sets the token to sleep until it is going to be reset
     */
    private void addRateLimitListener() {
        twitter.addRateLimitStatusListener(new RateLimitStatusListener() {
            @Override
            public void onRateLimitStatus(RateLimitStatusEvent rateLimitStatusEvent) {
                if (rateLimitStatusEvent.getRateLimitStatus().getRemaining() <= 2) {
                    tokenValidTimestamp = rateLimitStatusEvent.getRateLimitStatus().getResetTimeInSeconds();
                }
            }
            @Override public void onRateLimitReached(RateLimitStatusEvent rateLimitStatusEvent) {}
        });
    }

    protected Twitter getTwitter() {
        return twitter;
    }


    protected int getTokenValidTimestamp() {
        return tokenValidTimestamp;
    }
}
