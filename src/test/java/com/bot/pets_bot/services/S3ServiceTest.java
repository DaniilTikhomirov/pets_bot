package com.bot.pets_bot.services;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class S3ServiceTest {


    private final S3Service s3Service = new S3Service();

    @Test
    public void testGetExtension_Success() {
        String filename = "image.png";
        String extension = s3Service.getExtension(filename);

        Assertions.assertEquals("png", extension);
    }

    @Test
    public void testGetExtension_EmptyFilename() {
        String filename = "image";
        String extension = s3Service.getExtension(filename);

        Assertions.assertEquals("", extension);
    }
}
