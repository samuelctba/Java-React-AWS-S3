package to.us.samuelcamargo.Witnesses.bucket;

public enum BucketName {
    PROFILE_IMAGE("my-first-bucket-sac");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }
    public String getBucketName() {
        return bucketName;
    }

}
