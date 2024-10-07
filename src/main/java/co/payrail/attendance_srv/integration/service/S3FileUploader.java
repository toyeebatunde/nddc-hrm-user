package co.payrail.attendance_srv.integration.service;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.net.URI;
import java.time.Duration;
import java.util.Base64;
import java.util.UUID;

public class S3FileUploader {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String bucketName;

    public S3FileUploader(S3Client s3Client, S3Presigner s3Presigner, String bucketName) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.bucketName = bucketName;
    }

    public URI generatePresignedUrl(String fileKey) {
        try {
            // Set the expiration time for the pre-signed URL (e.g., 1 hour)
            Duration expiration = Duration.ofHours(1);

            // Generate a pre-signed URL for the client to upload or download the file
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(expiration)
                    .getObjectRequest(getObjectRequest)
                    .build();

            // Return the pre-signed URL as a URI
            return s3Presigner.presignGetObject(presignRequest).url().toURI();
        } catch (Exception e) {
            throw new RuntimeException("Error generating pre-signed URL", e);
        }
    }

    public String uploadFile(String base64File, String existingS3FileKey) {
        try {
            // If an existing file key is provided, delete the existing file before uploading the new one
            if (existingS3FileKey != null) {
                deleteFile(existingS3FileKey);
            }

            // Convert base64 to byte array (assuming the base64 string is not null)
            byte[] fileBytes = Base64.getDecoder().decode(base64File);

            // Generate a unique key for the S3 object (you can use UUID or any other method)
            String s3ObjectKey = UUID.randomUUID().toString();

            // Upload the file to S3
            PutObjectResponse putObjectResponse = s3Client.putObject(PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3ObjectKey)
                    .build(), RequestBody.fromBytes(fileBytes));

            // Return the S3 URL
            return "https://" + bucketName + ".s3.amazonaws.com/" + s3ObjectKey;
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file to S3", e);
        }
    }

    public void deleteFile(String s3FileKey) {
        try {
            // Delete the file from S3
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3FileKey)
                    .build());

            // You can log or handle the delete response if needed
        } catch (Exception e) {
            throw new RuntimeException("Error deleting file from S3", e);
        }
    }
}
