package conditioner.controller;

import conditioner.dto.UploadFileDto;
import conditioner.exceptions.ConditionerException;
import conditioner.service.PriceService;
import conditioner.utils.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.stream.Collectors;
@CrossOrigin(value = "*")
@RestController
public class PriceController {
    @Autowired
    PriceService priceService;

    @PostMapping("/api/uploadfiles")
    public UploadFileDto uploadFileMulti(
            @RequestParam("file") MultipartFile[] file) {

        // Get file name
        String uploadedFileName = Arrays.stream(file).map(MultipartFile::getOriginalFilename)
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

        if (StringUtils.isEmpty(uploadedFileName)) {
            return UploadFileDto.builder().rez(
            "please select a file!").build();
        }

        String notExcelFiles = Arrays.stream(file).filter(x -> !ExcelUtils.isExcelFile(x))
                .map(MultipartFile::getOriginalFilename)
                .collect(Collectors.joining(" , "));

        if(!StringUtils.isEmpty(notExcelFiles)) {
            return UploadFileDto.builder().rez(
                    "Not Excel Files!").build();
        }
        try {
            for(MultipartFile fileForParse: file) {
                priceService.store(fileForParse);
            }
            return UploadFileDto.builder().rez(
                    "Upload Successfully \" + uploadedFileName").build();

        } catch (Exception e) {
            throw new ConditionerException(e.getMessage() + uploadedFileName);
        }
    }
}
//TODO logger

