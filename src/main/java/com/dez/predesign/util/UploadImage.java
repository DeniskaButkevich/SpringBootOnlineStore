package com.dez.predesign.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.dez.predesign.data.catalog.Product;
import com.dez.predesign.data.catalog.Image;
import com.dez.predesign.repository.ImageRepo;
import com.dez.predesign.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class UploadImage {

    public static void uploadImage(String id, MultipartFile file, String uploadPath, ProductRepo productRepo, ImageRepo imageRepo, String flag) throws IOException {

        Product product = productRepo.findById(Long.parseLong(id)).get();
        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String uuidFile = UUID.randomUUID().toString();
        String resultFilename = uuidFile + file.getOriginalFilename();

        file.transferTo(new File(uploadPath + resultFilename));

        if (flag.equals("general"))
            product.setFilename(resultFilename);
        if (flag.equals("hover"))
            product.setHoverFilename(resultFilename);
        if (flag.equals("sec"))
            imageRepo.save(new Image(product, resultFilename));

        productRepo.save(product);
    }

    public static File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public static AmazonS3 getConnectiontAmazonS3(String AWS_ACCESS_KEY_ID, String AWS_SECRET_ACCESS_KEY) {
        AWSCredentials credentials = new BasicAWSCredentials(AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
        final AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new StaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_CENTRAL_1)
                .build();
        return s3;
    }

    public static String putObjectAmazonS3(MultipartFile file, String S3_BUCKET_NAME, String AWS_ACCESS_KEY_ID, String AWS_SECRET_ACCESS_KEY) throws IOException {

        String uuidFile = UUID.randomUUID().toString();
        String resultFilename = uuidFile + file.getOriginalFilename();

        AmazonS3 connection = getConnectiontAmazonS3(AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);

        connection.putObject(
                new PutObjectRequest(S3_BUCKET_NAME, resultFilename, convert(file))
                        .withCannedAcl(CannedAccessControlList.PublicRead));

        return resultFilename;
    }

    public static void deleteObjectAmazonS3(String key, String s3_bucket_name, String aws_access_key_id, String aws_secret_access_key) {
        AmazonS3 connection = getConnectiontAmazonS3(aws_access_key_id, aws_secret_access_key);
        connection.deleteObject(s3_bucket_name, key);
    }
}