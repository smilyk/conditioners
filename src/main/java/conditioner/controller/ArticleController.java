package conditioner.controller;

import conditioner.dto.ArticleDto;
import conditioner.dto.ImageDto;
import conditioner.service.ArticleServiceimpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/article")
@Api(value = "Articles", description = "methods that collect data about the article ")
public class ArticleController {

    @Autowired
    ArticleServiceimpl articleService;

    @ApiOperation(value="Adding a new article to DataBase")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added cupboard"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping
    public ArticleDto createArticle(@Valid @RequestBody ArticleDto articleDto){
        return articleService.createArticle(articleDto);
    }
    @ApiOperation(value = "Getting a article from DB")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get article"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping("/{articleUuid}")
    public ArticleDto getArticle(@PathVariable String articleUuid){
        return articleService.getArticleByUuid(articleUuid);
    }
    @ApiOperation(value = "Getting all articles from DB")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get all articles"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )

    @GetMapping()
    public List<ArticleDto> getAllArticles(){
        return articleService.getAllArticles();
    }

    @ApiOperation(value = "Delete  article from DB")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted article"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @DeleteMapping("/{articleUuid}")
    public ArticleDto deleteArticle(@PathVariable String articleUuid){
        return articleService.deleteArticleByUuid(articleUuid);
    }

    @PostMapping(path ="photo/{photoName}")
    public ImageDto getPhotoByName(@PathVariable String photoName) {
        return articleService.getPhoto(photoName);
    }

}
