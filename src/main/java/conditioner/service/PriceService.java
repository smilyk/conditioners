package conditioner.service;

import conditioner.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface PriceService {
    void store(MultipartFile file);

    NameModelListDto getNameAndModelList();

    List<ResponseGetPriceDto> getPrice(List<RequestGetPriceDto> req);

    ResponseOfferDto getOfferDto(RequestOfferDto req);

    List<PriceDto> getAllPrice();
}
