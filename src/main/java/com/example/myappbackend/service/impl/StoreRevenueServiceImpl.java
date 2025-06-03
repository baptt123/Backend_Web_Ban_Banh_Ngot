package com.example.myappbackend.service.impl;

import com.example.myappbackend.repository.OrdersRepository;
import com.example.myappbackend.service.interfaceservice.StoreRevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreRevenueServiceImpl implements StoreRevenueService {

    private final OrdersRepository ordersRepository;

    private final Integer STORE_ID = 1;

    @Override
    public List<RevenueResponse> getDailyRevenue(RevenueFilterRequest request) {
        List<Object[]> raw = ordersRepository.getDailyRevenue(STORE_ID, request.getStartDate(), request.getEndDate());
        return raw.stream()
                .map(obj -> new RevenueResponse(
                        ((java.sql.Date) obj[0]).toLocalDate(),
                        (BigDecimal) obj[1]
                )).collect(Collectors.toList());
    }

    @Override
    public List<RevenueResponse> getWeeklyRevenue(Integer year) {
        List<Object[]> raw = ordersRepository.getWeeklyRevenue(STORE_ID, year);
        return raw.stream()
                .map(obj -> new RevenueResponse(
                        LocalDate.ofYearDay(year, ((Integer) obj[0] - 1) * 7 + 1),
                        (BigDecimal) obj[1]
                )).collect(Collectors.toList());
    }

    @Override
    public List<RevenueResponse> getMonthlyRevenue(Integer year) {
        List<Object[]> raw = ordersRepository.getMonthlyRevenue(STORE_ID, year);
        return raw.stream()
                .map(obj -> new RevenueResponse(
                        LocalDate.of(year, (Integer) obj[0], 1),
                        (BigDecimal) obj[1]
                )).collect(Collectors.toList());
    }
}
