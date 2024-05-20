package com.codeqna.controller;

import com.codeqna.dto.FileConfigDTO;
import com.codeqna.dto.UploadFileDto;
import com.codeqna.entity.Fileconfig;
import com.codeqna.repository.FileconfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/fileAPI")
public class FileConfigController {

    @Autowired
    private FileconfigRepository fileconfigRepository;

    @PostMapping("/saveFileConfig")
    public String saveFileConfig(@RequestBody FileConfigDTO fileConfigDTO) {
        // 항상 단일 레코드만 존재하게 처리
        Fileconfig fileconfig = fileconfigRepository.findById(1).orElse(new Fileconfig());
        fileconfig.setId(1); // ID는 항상 1로 고정
        fileconfig.setMax_File_Num(fileConfigDTO.getMaxFileNum());
        fileconfig.setMax_file_Size(fileConfigDTO.getMaxFileSize());
        fileconfig.setFile_ext(fileConfigDTO.getFileExt());

        fileconfigRepository.save(fileconfig);
        return "Success";
    }

    @GetMapping("/fileconfig")
    public Fileconfig getFileConfig(){
        Optional<Fileconfig> fileconfig = fileconfigRepository.findById(1);
        return fileconfig.orElseThrow(() -> new RuntimeException("FIle configuration not found"));
    }

    // 파일이 저장될 경로 설정
    @Value("${upload.path}")
    private String uploadPath;

//    @Autowired
//    private UploadfileRepository uploadfileRepository;

    @PostMapping("/upload")
    public ResponseEntity<UploadFileDto> uploadFile(@RequestParam("file") MultipartFile file){

        try {
            // 저장할 파일 경로 생성
            String originalFileName = file.getOriginalFilename();
            String savedFileName = UUID.randomUUID().toString() + "_" + originalFileName;
            Path savedFilePath = Paths.get(uploadPath, savedFileName);

            // 파일 저장
            Files.createDirectories(savedFilePath.getParent());
            file.transferTo(savedFilePath.toFile());

            // 파일 정보 반환
            UploadFileDto fileDto = new UploadFileDto();
            fileDto.setOriginalFileName(originalFileName);
            fileDto.setSavedFileName(savedFileName);
            return ResponseEntity.status(HttpStatus.OK).body(fileDto);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
