/**
 * @author kevin
 * @date 8/14/14
 */
public enum TwitterUserApiCalls {
    NAME("getName"),
    DESCRIPTION("getDescription"),
    FAVORITES_COUNT("getFavouritesCount"),
    FOLLOWERS_COUNT("getFollowersCount"),
    FRIENDS_COUNT("getFriendsCount"),
    ID("getId"),
    LANGUAGE("getLang"),
    LISTED_COUNT("getListedCount"),
    LOCATION("getLocation"),
    SCREEN_NAME("getScreenName"),
    STATUS("getStatus"),
    STATUSES_COUNT("getStatusesCount"),
    TIME_ZONE("getTimeZone"),
    URL("getURL")
    ;

    private String methodName;

    TwitterUserApiCalls(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }
}
