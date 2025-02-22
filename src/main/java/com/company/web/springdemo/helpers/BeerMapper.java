package com.company.web.springdemo.helpers;

import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.BeerDto;
import com.company.web.springdemo.services.BeerService;
import com.company.web.springdemo.services.StyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeerMapper {

    private final BeerService beerService;

    private final StyleService styleService;

    @Autowired
    public BeerMapper(BeerService beerService, StyleService styleService) {
        this.beerService = beerService;
        this.styleService = styleService;
    }

    public Beer fromDto(int id, BeerDto dto) {
        Beer beer = fromDto(dto);
        beer.setId(id);
        Beer repositoryBeer = beerService.get(id);
        beer.setCreatedBy(repositoryBeer.getCreatedBy());
        beer.setPhotoUrl(repositoryBeer.getPhotoUrl());
        return beer;
    }

    public Beer fromDto(BeerDto dto) {
        Beer beer = new Beer();
        beer.setName(dto.getName());
        beer.setAbv(dto.getAbv());
        beer.setStyle(styleService.get(dto.getStyleId()));
        return beer;
    }

    public BeerDto toDto(Beer beer) {
        BeerDto beerDto = new BeerDto();
        beerDto.setName(beer.getName());
        beerDto.setAbv(beer.getAbv());
        beerDto.setStyleId(beer.getStyle().getId());
        return beerDto;
    }

}
