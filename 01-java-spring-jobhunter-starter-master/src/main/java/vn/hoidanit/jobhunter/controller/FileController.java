package vn.hoidanit.jobhunter.controller;

import vn.hoidanit.jobhunter.service.FileService;
import java.net.URISyntaxException;
import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    @Value("${hoidanit.upload-file.base-uri}")
    private String baseURI;

    private final FileService fileService;
    public FileController( FileService fileService){
        this.fileService = fileService;
    }

    
    @PostMapping("/files")
    public String upload(@RequestParam("file") MultipartFile file,
                        @RequestParam("folder") String folder) throws URISyntaxException,IOException{
        //skip validate

        //create a directory if not exist 
        this.fileService.createDirectoty(baseURI + folder);
        //store file 
        this.fileService.store(file ,folder);
        return file.getOriginalFilename() + folder;
    }
}
