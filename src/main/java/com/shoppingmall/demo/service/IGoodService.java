package com.shoppingmall.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shoppingmall.demo.model.DO.GoodDO;
import com.shoppingmall.demo.model.DTO.GoodDeleteBatchDTO;
import com.shoppingmall.demo.model.DTO.GoodSaveDTO;
import com.shoppingmall.demo.model.DTO.GoodUpdateDTO;
import com.shoppingmall.demo.model.DTO.GoodUpdateStockNumDTO;
import com.shoppingmall.demo.model.Query.GoodQuery;
import com.shoppingmall.demo.utils.Result;

public interface IGoodService extends IService<GoodDO> {

    Result saveGood(GoodSaveDTO goodSaveDTO);

    Result updateGoodStockNum(GoodUpdateStockNumDTO goodUpdateStockNumDTO);

    Result updateGood(GoodUpdateDTO goodUpdateDTO);

    Result deleteGoodById(Long id);

    Result deleteGoodBatch(GoodDeleteBatchDTO deleteBatchDTO);

    Result getGoodInfoById(Long goodId);

    Result getMyGoodListPage(GoodQuery goodQuery);

    Result pageGoodListByCondition(GoodQuery goodQuery);

    Result getGoodRankList(GoodQuery goodQuery);

}
