package com.itwray.iw.eat.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;

/**
 * 用餐记录的菜品食材详情对象
 *
 * @author wray
 * @since 2024/5/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealDishesMaterialDetailVo {

    /**
     * 需要采购的食材集合
     */
    private List<NeedPurchaseMaterial> needPurchaseMaterialList;

    /**
     * 常用食材集合
     */
    private HashSet<String> commonMaterialList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NeedPurchaseMaterial {
        /**
         * 食材名称
         */
        private String materialName;

        /**
         * 食材用量集合
         */
        private List<String> materialDosages;
    }

}
