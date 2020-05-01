package to.us.samuelcamargo.Witnesses.proifle;

import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import to.us.samuelcamargo.Witnesses.bucket.BucketName;
import to.us.samuelcamargo.Witnesses.fileStore.FileStore;

import java.io.IOException;
import java.util.*;

@Service
public class UserProfileService {

    private final UserProfileDataAccessService userProfileDataAccessService;
    private final FileStore fileStore;

    @Autowired
    public UserProfileService(UserProfileDataAccessService userProfileDataAccessService,
                              FileStore fileStore) {
        this.userProfileDataAccessService = userProfileDataAccessService;
        this.fileStore = fileStore;
    }

    public List<UserProfile> getUserProfiles() {
        return userProfileDataAccessService.getUserProfiles();
    }

    public byte[] downloadUserProfileImage(UUID userProfileId) {
        UserProfile user = getProfileIDorThrow(userProfileId);
        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getUserProfileID());
        return user.getUserProfileImgLink()
                .map(key -> fileStore.downloadImageFromS3(path, key))
                .orElse( new byte[0]);
    }

    public void uploadUserProfileImage(UUID userProfileId, MultipartFile file) {
        //Check if image is not empty
        isFileEmpty(file);
        //Check if file is an image
        isImage(file);
        // The user exists in our database
        UserProfile user = getProfileIDorThrow(userProfileId);
        // Grab Metadata from file
        Map<String, String> metadata = extractMetadata(file);
        // userProfileImgLink
        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getUserProfileID());
        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
        try {
            fileStore.save(path, filename, file.getInputStream(), Optional.of(metadata));
            user.setUserProfileImgLink(filename);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }

    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    private UserProfile getProfileIDorThrow(UUID userProfileId) {
        return userProfileDataAccessService.getUserProfiles()
                .stream()
                .filter(userProfile -> userProfile.getUserProfileID().equals(userProfileId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("User profile %s not found", userProfileId)));
    }

    private void isImage(MultipartFile file) {
        if (!Arrays.asList(ContentType.IMAGE_JPEG.getMimeType(),
                ContentType.IMAGE_PNG.getMimeType(),
                ContentType.IMAGE_GIF.getMimeType(),
                ContentType.IMAGE_BMP.getMimeType())
                .contains(file.getContentType())) {
            throw new IllegalStateException("File has a invalid image format [ " + file.getContentType() + " ]");
        }
    }

    private void isFileEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("File is Empty!" + "/nSize: " + file.getSize());
        }
    }

}
