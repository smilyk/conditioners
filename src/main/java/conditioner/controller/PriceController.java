package conditioner.controller;

import conditioner.dto.*;
import conditioner.exceptions.ConditionerException;
import conditioner.service.ArticleServiceimpl;
import conditioner.service.PriceService;
import conditioner.utils.ExcelUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(value = "*")
@RestController
public class PriceController {
    private static final Logger LOGGER = LogManager.getLogger(PriceController.class);

    @Autowired
    PriceService priceService;

    @PostMapping("/api/uploadfiles")
    public UploadFileDto uploadFileMulti(
            @RequestParam("file") MultipartFile[] file) {

        // Get file name
        String uploadedFileName = Arrays.stream(file).map(MultipartFile::getOriginalFilename)
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

        if (StringUtils.isEmpty(uploadedFileName)) {
            LOGGER.error("file not selected");
            return UploadFileDto.builder().rez(
                    "please select a file!").build();
        }

        String notExcelFiles = Arrays.stream(file).filter(x -> !ExcelUtils.isExcelFile(x))
                .map(MultipartFile::getOriginalFilename)
                .collect(Collectors.joining(" , "));

        if (!StringUtils.isEmpty(notExcelFiles)) {
            LOGGER.error("format of file - not excel");
            return UploadFileDto.builder().rez(
                    "Not Excel Files!").build();
        }
        try {
            for (MultipartFile fileForParse : file) {
                priceService.store(fileForParse);
                LOGGER.info("file with name " + uploadedFileName + "was upload successfully");
            }
            return UploadFileDto.builder().rez(
                    "Upload Successfully file with name: " + uploadedFileName).build();

        } catch (Exception e) {
            throw new ConditionerException(e.getMessage() + uploadedFileName);
        }
    }

    @GetMapping()
    public NameModelListDto getNameAndModelsList() {
        return priceService.getNameAndModelList();
    }

    //подробная таблица
    @PostMapping("/price")
    public List<ResponseGetPriceDto> getPriceForChosenPosition(@RequestBody List<RequestGetPriceDto> req) {
        return priceService.getPrice(req);
    }

    //сводная таблица + предлжение клиенту
//    TODO созранять в базу предложение только если клиент обозначен
    @PostMapping("/price/proposition")
    public ResponseOfferDto getProposition(@RequestBody RequestOfferDto req) {
        return priceService.getOfferDto(req);
    }

//    получить весь прайс
    @GetMapping("/price")
    public List<PriceDto> getPrice() {
        return priceService.getAllPrice();
    }

//    удалить позицию из прайса
    @DeleteMapping("{/uuidPosition}")
    public PriceDto deletePositionInPrice( @PathVariable String uuidPosition){
        return priceService.deletePositionFromPrice(uuidPosition);
    }
//    обновить позицию в прайсе
    @PutMapping()
    public PriceDto updatePricePosition(@RequestBody PriceDto priceDto){
        return priceService.updatePricePosition(priceDto);
    }
}
//TODO Swager

