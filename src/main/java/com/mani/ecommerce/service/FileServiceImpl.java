package com.mani.ecommerce.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {

        //File names of current / original file
        String originalFileName = file.getOriginalFilename();

        // Generate a unique file name
        String randomId = UUID.randomUUID().toString();
        //mat.jpg -fileName
        //1234 - randomId
        //fileName = 1234.jpg

        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        // Path+"/" + fileName
        // pathSeperator ;, seperator /
        String filePath = path+ File.separator + fileName;

        // check if path exist and created
        File folder = new File(path);
        if(!folder.exists())
            folder.mkdir();

        //upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));

        return fileName;
    }
}
