package to.us.samuelcamargo.Witnesses.dataStore;

import org.springframework.stereotype.Repository;
import to.us.samuelcamargo.Witnesses.proifle.UserProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FakeUserProfileDataStore {

    private static final List<UserProfile> USER_PROFILES = new ArrayList<>();

    static {
        USER_PROFILES.add(new UserProfile(UUID.fromString("d39c677a-4c65-4200-8d4c-650345281948"), "samuelcamargo", null));
        USER_PROFILES.add(new UserProfile(UUID.fromString("ac884b68-a3a3-46c8-9391-6a54355a4b19"), "dheboracamargo", null));
    }

    public List<UserProfile> getUserProfiles() {
        return USER_PROFILES;
    }
}
