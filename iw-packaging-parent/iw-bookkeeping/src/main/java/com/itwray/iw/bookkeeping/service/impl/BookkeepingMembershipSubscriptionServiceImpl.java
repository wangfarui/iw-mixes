package com.itwray.iw.bookkeeping.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.itwray.iw.bookkeeping.dao.BookkeepingMembershipSubscriptionDao;
import com.itwray.iw.bookkeeping.mapper.BookkeepingMembershipSubscriptionMapper;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingMembershipSubscriptionAddDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingMembershipSubscriptionListDto;
import com.itwray.iw.bookkeeping.model.dto.BookkeepingMembershipSubscriptionUpdateDto;
import com.itwray.iw.bookkeeping.model.entity.BookkeepingMembershipSubscriptionEntity;
import com.itwray.iw.bookkeeping.model.enums.MembershipSubscriptionExpiryTypeEnum;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingMembershipSubscriptionDetailVo;
import com.itwray.iw.bookkeeping.model.vo.BookkeepingMembershipSubscriptionListVo;
import com.itwray.iw.bookkeeping.service.BookkeepingMembershipSubscriptionService;
import com.itwray.iw.bookkeeping.utils.MembershipBillingCycleUtils;
import com.itwray.iw.web.model.enums.SortTypeEnum;
import com.itwray.iw.web.model.enums.SortWayEnum;
import com.itwray.iw.web.service.impl.WebServiceImpl;
import com.itwray.iw.web.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * 会员订阅记录表 服务实现类
 *
 * @author wray
 * @since 2025/11/5
 */
@Service
public class BookkeepingMembershipSubscriptionServiceImpl extends WebServiceImpl<BookkeepingMembershipSubscriptionDao, BookkeepingMembershipSubscriptionMapper, BookkeepingMembershipSubscriptionEntity,
        BookkeepingMembershipSubscriptionAddDto, BookkeepingMembershipSubscriptionUpdateDto, BookkeepingMembershipSubscriptionDetailVo, Integer> implements BookkeepingMembershipSubscriptionService {

    @Autowired
    public BookkeepingMembershipSubscriptionServiceImpl(BookkeepingMembershipSubscriptionDao baseDao) {
        super(baseDao);
    }

    @Override
    public Integer add(BookkeepingMembershipSubscriptionAddDto dto) {
        this.fillEndDateIfAbsent(dto);
        return super.add(dto);
    }

    @Override
    public void update(BookkeepingMembershipSubscriptionUpdateDto dto) {
        this.fillEndDateIfAbsent(dto);
        super.update(dto);
    }

    @Override
    public List<BookkeepingMembershipSubscriptionListVo> list(BookkeepingMembershipSubscriptionListDto dto) {
        List<BookkeepingMembershipSubscriptionEntity> list = getBaseDao().lambdaQuery()
                .eq(BookkeepingMembershipSubscriptionEntity::getUserId, UserUtils.getUserId())
                .eq(dto.getMembershipType() != null, BookkeepingMembershipSubscriptionEntity::getMembershipType, dto.getMembershipType())
                .and(MembershipSubscriptionExpiryTypeEnum.VALID.equals(dto.getExpiryType()), wrapper -> wrapper
                        .isNull(BookkeepingMembershipSubscriptionEntity::getEndDate)
                        .or()
                        .ge(BookkeepingMembershipSubscriptionEntity::getEndDate, LocalDate.now())
                )
                .and(MembershipSubscriptionExpiryTypeEnum.ABOUT_EXPIRE.equals(dto.getExpiryType()), wrapper -> wrapper
                        .isNotNull(BookkeepingMembershipSubscriptionEntity::getEndDate)
                        .le(BookkeepingMembershipSubscriptionEntity::getEndDate, LocalDate.now().plusWeeks(1L))
                        .ge(BookkeepingMembershipSubscriptionEntity::getEndDate, LocalDate.now())
                )
                .lt(MembershipSubscriptionExpiryTypeEnum.EXPIRED.equals(dto.getExpiryType()), BookkeepingMembershipSubscriptionEntity::getEndDate, LocalDate.now())
                .orderByAsc(SortWayEnum.isAsc(dto.getSortWay()), SortTypeEnum.getDefaultSortField(dto.getSortType()))
                .orderByDesc(!SortWayEnum.isAsc(dto.getSortWay()), SortTypeEnum.getDefaultSortField(dto.getSortType()))
                .list();
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream()
                .map(t -> BeanUtil.copyProperties(t, BookkeepingMembershipSubscriptionListVo.class))
                .toList();
    }

    @Override
    public int autoRenew() {
        int count = 0;
        // 查询所有开通了自动续费的会员服务
        List<BookkeepingMembershipSubscriptionEntity> subscriptionEntityList = getBaseDao().lambdaQuery()
                .eq(BookkeepingMembershipSubscriptionEntity::getAutoRenew, Boolean.TRUE)
                .eq(BookkeepingMembershipSubscriptionEntity::getEndDate, LocalDate.now())
                .list();
        if (CollectionUtils.isEmpty(subscriptionEntityList)) {
            return count;
        }
        for (BookkeepingMembershipSubscriptionEntity subscriptionEntity : subscriptionEntityList) {
            // 计算计费周期下的下一个续费时长
            subscriptionEntity.setStartDate(subscriptionEntity.getEndDate());
            LocalDate endDate = MembershipBillingCycleUtils.computeBillingCycleDate(subscriptionEntity);
            if (endDate == null) {
                continue;
            }

            BookkeepingMembershipSubscriptionEntity updateEntity = new BookkeepingMembershipSubscriptionEntity();
            updateEntity.setId(subscriptionEntity.getId());
            updateEntity.setEndDate(endDate);
            boolean updateCount = getBaseDao().updateById(updateEntity);
            count += updateCount ? 1 : 0;
        }
        return count;
    }

    /**
     * 如果结束日期为空，则根据开始日期和计费周期计算结束日期
     */
    private void fillEndDateIfAbsent(BookkeepingMembershipSubscriptionAddDto dto) {
        if (dto.getEndDate() == null) {
            dto.setEndDate(MembershipBillingCycleUtils.computeBillingCycleDate(
                    dto.getStartDate(), dto.getBillingCycle(), dto.getCycleNum(), dto.getCycleUnit()
            ));
        }
    }
}
