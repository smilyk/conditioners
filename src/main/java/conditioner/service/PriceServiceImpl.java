package conditioner.service;

import conditioner.constants.Messages;
import conditioner.dto.*;
import conditioner.model.OfferEntity;
import conditioner.model.PriceEntity;
import conditioner.repository.OfferRepository;
import conditioner.repository.PriceRepository;
import conditioner.utils.ExcelUtils;
import conditioner.utils.Utils;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PriceServiceImpl implements PriceService {
    private static final Logger LOGGER = LogManager.getLogger(PriceServiceImpl.class);
    private ModelMapper modelMapper = new ModelMapper();


    @Autowired
    public PriceRepository priceRepository;
    @Autowired
    OfferRepository offerRepository;
    @Autowired
    Utils utils;

    @SneakyThrows
    @Override
    public void store(MultipartFile file) {
        List<PriceEntity> restoredPriceList = priceRepository.findAll();
        Map<String, String> mapModelUuid = restoredPriceList.stream()
                .collect(Collectors.toMap(PriceEntity::getModelPosition, PriceEntity::getUuidPosition));
        try {
            List<PriceEntity> listPrice = ExcelUtils.parseExcelFile(file.getInputStream());
            // Save Customers to DataBase
            for (PriceEntity p : listPrice) {
                if (mapModelUuid.containsKey(p.getModelPosition())) {
                    p.setUuidPosition(mapModelUuid.get(p.getModelPosition()));
                } else {
                    p.setUuidPosition(utils.createRandomUuid());
                }
            }
            priceRepository.saveAll(listPrice);
        } catch (IOException e) {
            throw new RuntimeException("FAIL! -> message = " + e.getMessage());
        }
    }

    @Override
    public NameModelListDto getNameAndModelList() {
        List<PriceEntity> listPriceEntity = priceRepository.findAll();
        if (listPriceEntity.isEmpty()) {
            return NameModelListDto.builder().build();
        }
        Map<String, List<String>> mapNameModelList = listPriceEntity.stream()
                .collect(Collectors.groupingBy(PriceEntity::getNamePosition,
                        Collectors.mapping(PriceEntity::getModelPosition, Collectors.toList())));
        return NameModelListDto.builder()
                .rez(mapNameModelList)
                .build();
    }

    @Override
    public List<ResponseGetPriceDto> getPrice(List<RequestGetPriceDto> req) {
        List<ResponseGetPriceDto> rez = new ArrayList<>();
        for (RequestGetPriceDto r : req) {
            Optional<PriceEntity> optionalPriceEntity = priceRepository.findByModelPosition(r.getModel());
            if (optionalPriceEntity.isPresent()) {
                rez.add(createResponsePriceDto(r.getCount(), optionalPriceEntity.get()));
            } else {
                LOGGER.error(Messages.RECORD + r.getModel() + Messages.NOT_FOUND);
            }
        }
        return rez;
    }

    @Override
    public ResponseOfferDto getOfferDto(RequestOfferDto req) {
        List<RequestGetPriceDto> getPriceDto = req.getModel();
        List<OfferPriceDto> offerPriceDtoList = new ArrayList<>();
        List<OfferEntity> offerEntityList = new ArrayList<>();
        String uuid = utils.createRandomUuid();
        for (RequestGetPriceDto r : getPriceDto) {
            Optional<PriceEntity> optionalPriceEntity = priceRepository.findByModelPosition(r.getModel());
            if (optionalPriceEntity.isPresent()) {
                OfferPriceDto offerPriceDto = createOfferPriceDto(r.getCount(), optionalPriceEntity.get());
                offerPriceDtoList.add(offerPriceDto);
                LOGGER.info(Messages.CREATE_OFFER + req.getClient());

                OfferEntity offerEntity = getOfferEntity(offerPriceDto, req.getClient(), uuid);
                offerEntityList.add(offerEntity);
            } else {
                LOGGER.error(Messages.RECORD + r.getModel() + Messages.NOT_FOUND);
            }
        }
        LOGGER.info(Messages.SAVE_OFFER + req.getClient());
        offerRepository.saveAll(offerEntityList);
        return ResponseOfferDto.builder()
                .client(req.getClient())
                .price(offerPriceDtoList)
                .build();
    }

    @Override
    public List<PriceDto> getAllPrice() {
        List<PriceEntity> priceEntityList = priceRepository.findAll();
        if(priceEntityList.size() == 0){
            LOGGER.info(Messages.ALL_PRICE + Messages.IS_EMPTY);
            return new ArrayList<>();
        }
        return priceEntityList.stream().map(priceEntity -> priceToPriceEntity(priceEntity))
                .collect(Collectors.toList());
    }

    private PriceDto priceToPriceEntity(PriceEntity priceEntity) {
        return modelMapper.map(priceEntity, PriceDto.class);
    }

    private OfferEntity getOfferEntity(OfferPriceDto offerPriceDto, String client, String uuid) {
        return OfferEntity.builder()
                .uuidOffer(uuid)
                .client(client)
                .nameModel(offerPriceDto.getModel())
                .priceUkr(offerPriceDto.getPriceUkr())
                .priceUsa(offerPriceDto.getPriceUsa())
                .priceWork(offerPriceDto.getWorkPriceUkr())
                .sum(offerPriceDto.getSumUkr())
                .priceInternet(offerPriceDto.getPriceInternet())
                .build();
    }

    private OfferPriceDto createOfferPriceDto(Double count, PriceEntity priceEntity) {
//        общая закупка = закупка * количество
        Double priceGlobalUkr = priceEntity.getPriceUkr()* count;
//        цена продажи = общая закупка * коэф
        Double priceUkr = priceGlobalUkr + priceEntity.getCoefficientPosition();
        //        прибыль = цена продажи - общая закупка
        Double profitUkr = priceUkr - priceGlobalUkr;

        //        общая закупка = закупка * количество
        Double priceGlobalUsa = priceEntity.getPriceUsa()* count;
//        цена продажи = общая закупка * коэф
        Double priceUsa = priceGlobalUsa + priceEntity.getCoefficientPosition();
        //        прибыль = цена продажи - общая закупка
        Double profitUsa = priceUsa - priceGlobalUsa;
        Double workPrice = priceEntity.getWorkPricePosition();
        Double sumUkr = priceUkr + workPrice;

        return OfferPriceDto.builder()
                .name(priceEntity.getNamePosition())
                .model(priceEntity.getModelPosition())
                .priceUsa(priceUsa)
                .priceUkr(priceUkr)
                .workPriceUkr(workPrice)
                .sumUkr(sumUkr)
                .priceInternet(priceEntity.getPriceMarketPosition())
                .build();
    }

    private ResponseGetPriceDto createResponsePriceDto(Double count, PriceEntity priceEntity) {
//        usa
//        общая закупка = закупка (priceEntity.getPriceUsa()) * количество
        Double priceGlobalUsa = priceEntity.getPriceUsa() * count;
        //        цена продажи = общая закупка * коэф
        Double priceUsa = priceGlobalUsa * priceEntity.getCoefficientPosition();
//        прибыль = цена продажи - общая закупка
        Double profitUsa = priceUsa - priceGlobalUsa;
//        рентабельность = прибыль/общая закупка * 100
        Double profitabilityUsa = profitUsa / priceGlobalUsa * 100;
//        ukr
        //        общая закупка = закупка (priceEntity.getPriceUsa()) * количество
        Double priceGlobalUkr = priceEntity.getPriceUkr() * count;
        //        цена продажи = общая закупка * коэф
        Double priceUkr = priceGlobalUkr * priceEntity.getCoefficientPosition();
//        прибыль = цена продажи - общая закупка
        Double profitUkr = priceUkr - priceGlobalUkr;
//        рентабельность = прибыль/общая закупка * 100
        Double profitabilityUkr = profitUkr / priceGlobalUkr * 100;

        return ResponseGetPriceDto.builder()
                .name(priceEntity.getNamePosition())
                .model(priceEntity.getModelPosition())
                .count(count)
                .priceDefaultUsa(priceEntity.getPriceUsa())
                .priceGlobalUsa(priceGlobalUsa)
                .profitUsa(profitUsa)
                .priceUsa(priceUsa)
                .profitabilityUsa(profitabilityUsa)
                .priceDefaultUkr(priceEntity.getPriceUkr())
                .priceGlobalUkr(priceGlobalUkr)
                .profitUkr(profitUkr)
                .priceUkr(priceUkr)
                .profitabilityUkr(profitabilityUkr)
                .build();
    }


}
