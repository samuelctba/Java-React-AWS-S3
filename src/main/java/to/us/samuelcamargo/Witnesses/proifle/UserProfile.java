package to.us.samuelcamargo.Witnesses.proifle;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class UserProfile {

    private final UUID userProfileID;
    private final String userName;
    private String userProfileImgLink; //S3 Key

    public UserProfile(UUID userProfileID, String userName, String userProfileImgLink) {
        this.userProfileID = userProfileID;
        this.userName = userName;
        this.userProfileImgLink = userProfileImgLink;
    }

    public UUID getUserProfileID() {
        return userProfileID;
    }

    public String getUserName() {
        return userName;
    }

    public Optional<String> getUserProfileImgLink() {
        return Optional.ofNullable(userProfileImgLink);
    }

    public void setUserProfileImgLink(String userProfileImgLink) {
        this.userProfileImgLink = userProfileImgLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(userProfileID, that.userProfileID) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(userProfileImgLink, that.userProfileImgLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userProfileID, userName, userProfileImgLink);
    }
}
