package conditioner.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import conditioner.constants.Messages;
import conditioner.dto.ArticleDto;
import conditioner.exceptions.ConditionerException;
import conditioner.model.ArticleEntity;
import conditioner.repository.ArticleRepository;
import conditioner.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import java.io.IOException;

import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleServiceimpl {
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    Utils utils;
    private static final Logger LOGGER = LogManager.getLogger(ArticleServiceimpl.class);

    private ObjectMapper mapper = new ObjectMapper();
    private ModelMapper modelMapper = new ModelMapper();

    public ArticleDto createArticle(ArticleDto articleDto) {
        try {
            ArticleEntity article = articleDtoToEntity(articleDto);
            article.setUuidArticle(utils.createRandomUuid());
            String pictureLink = saveArticleOnServer(articleDto.getPictureBody(), articleDto.getPictureName());
//            article.setPictureUrl(pictureLink);
            String articleUuid = articleRepository.save(article).getUuidArticle();
            articleDto.setUuidArticle(articleUuid);

            LOGGER.info("Article created {}", mapper.writeValueAsString(article));
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
            throw new ConditionerException(e.getMessage());
        }
        return articleDto;
    }

    private String saveArticleOnServer(String pictures, String pictureName) {

        byte[] decodedImage = Base64.getMimeDecoder().decode(pictures);
        String link = pictureName + ".jpeg";
        try {
            File imgFile = new File(link);
            System.err.println("created file with name " + link);
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(decodedImage));
            ImageIO.write(img, "png", imgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return link;

    }

    private ArticleEntity articleDtoToEntity(ArticleDto articleDto) {
        return modelMapper.map(articleDto, ArticleEntity.class);
    }

    public ArticleDto getConditionerById(String articleUuid) {
        Optional<ArticleEntity> optionalArticleEntity = articleRepository.findByUuidArticle(articleUuid);
        if (!optionalArticleEntity.isPresent()) {
            LOGGER.error(Messages.ARTICLE + Messages.WITH_ID + articleUuid + Messages.NOT_FOUND);
            throw new ConditionerException(Messages.ARTICLE + Messages.WITH_ID + articleUuid + Messages.NOT_FOUND);
        }
        ArticleEntity article = optionalArticleEntity.get();
        ArticleDto articleDto = articleToArticleDto(article);
        try {
            LOGGER.info("Article with id {} found", articleUuid);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ConditionerException(e.getMessage());
        }
        return articleDto;
    }

    public List<ArticleDto> getAllArticles() {
        List<ArticleEntity> articles = articleRepository.findAll();
        List<ArticleDto> articlesDto = articles.stream().map(this::articleToArticleDto).collect(Collectors.toList());
        LOGGER.info(Messages.LIST + Messages.ARTICLES + Messages.FOUND);
        return articlesDto;
    }

    public ArticleDto deleteConditionerById(String articleUuid) {
        Optional<ArticleEntity> optionalArticleEntity = articleRepository.findByUuidArticle(articleUuid);
        if (!optionalArticleEntity.isPresent()) {
            LOGGER.error(Messages.ARTICLE + Messages.WITH_ID + articleUuid + Messages.NOT_FOUND);
            throw new ConditionerException(Messages.ARTICLE + Messages.WITH_ID + articleUuid + Messages.NOT_FOUND);
        }
        try {
            ArticleEntity article = optionalArticleEntity.get();
            ArticleDto articleDto = articleToArticleDto(article);
            articleRepository.delete(article);
            LOGGER.info("Article with id {} deleted ", articleUuid);
            return articleDto;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ConditionerException(e.getMessage());
        }
    }

    private ArticleDto articleToArticleDto(ArticleEntity article) {
        return modelMapper.map(article, ArticleDto.class);
    }

    public byte[] getPhoto(String photoName) {
        String encodeString = "";
        byte[] file = null;
        try {
            String fileLink = photoName + ".jpeg";
             file = FileUtils.readFileToByteArray(new File(fileLink));
            encodeString = Base64.getEncoder().encodeToString(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}