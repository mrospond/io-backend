package com.example.iobackend.mappers;

import com.example.iobackend.database.entities.ItemResultModel;
import com.example.iobackend.dto.ItemResultDto;
import com.example.iobackend.dto.ItemScrapingResult;
import org.mapstruct.Mapper;

@Mapper
public interface ItemMapper {
    ItemResultDto modelToDto(ItemResultModel model);
    ItemResultModel dtoToModel(ItemScrapingResult dto);
}
