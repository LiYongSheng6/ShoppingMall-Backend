package com.shoppingmall.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shoppingmall.demo.model.DO.BandDO;
import com.shoppingmall.demo.model.DTO.BandSaveDTO;
import com.shoppingmall.demo.model.DTO.BandUpdateDTO;
import com.shoppingmall.demo.utils.Result;

public interface IBandService extends IService<BandDO> {

    Result saveBand(BandSaveDTO bandSaveDTO);

    Result updateBand(BandUpdateDTO bandUpdateDTO);

    Result getBandListByType(Integer type);

    Result deleteBandById(Long id);
}
