package com.example.iobackend.mappers;

import com.example.iobackend.database.entities.ItemResultModel;
import com.example.iobackend.dto.ItemResultDto;
import com.example.iobackend.dto.ItemScrapingResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ItemMapper {
    ItemResultDto modelToDto(ItemResultModel model);
    @Mapping(target = "url", source = "directShopUrl")
    ItemResultModel dtoToModel(ItemScrapingResult dto);
}
