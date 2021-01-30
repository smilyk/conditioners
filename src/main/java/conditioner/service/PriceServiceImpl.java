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

@Service
public class PriceServiceImpl implements PriceService {

    @Autowired
    public PriceRepository priceRepository;
@Autowired
Utils utils;

    @SneakyThrows
    @Override
    public void store(MultipartFile file) {
        try {
            List<PriceEntity> listPrice = ExcelUtils.parseExcelFile(file.getInputStream());
            // Save Customers to DataBase
            for(PriceEntity p : listPrice){
                p.setUuidPosition(utils.createRandomUuid());
            }
            priceRepository.saveAll(listPrice);
        } catch (IOException e) {
            throw new RuntimeException("FAIL! -> message = " + e.getMessage());
        }
    }

}
//TODO logger