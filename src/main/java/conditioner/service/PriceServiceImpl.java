package conditioner.service;

import conditioner.model.PriceEntity;
import conditioner.repository.PriceRepository;
import conditioner.utils.ExcelUtils;
import conditioner.utils.Utils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PriceServiceImpl implements PriceService {

    @Autowired
    public PriceRepository priceRepository;
@Autowired
Utils utils;

    @SneakyThrows
    @Override
    public void store(MultipartFile file) {
        List<PriceEntity> restoredPriceList = priceRepository.findAll();
        Map<String, String> mapModelUuid =  restoredPriceList.stream()
        .collect(Collectors.toMap(PriceEntity::getModelPosition, PriceEntity::getUuidPosition));
        try {
            List<PriceEntity> listPrice = ExcelUtils.parseExcelFile(file.getInputStream());
            // Save Customers to DataBase
            for(PriceEntity p : listPrice){
                if(mapModelUuid.containsKey(p.getModelPosition())){
                    p.setUuidPosition(mapModelUuid.get(p.getModelPosition()));
                }else {
                    p.setUuidPosition(utils.createRandomUuid());
                }
            }

            priceRepository.saveAll(listPrice);
        } catch (IOException e) {
            throw new RuntimeException("FAIL! -> message = " + e.getMessage());
        }
    }

}
//TODO logger