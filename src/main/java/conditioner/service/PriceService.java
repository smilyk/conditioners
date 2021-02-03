package conditioner.service;

import conditioner.dto.NameModelListDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface PriceService {
    void store(MultipartFile file);

    NameModelListDto getNameAndModelList();
}
