package com.example.myappbackend.service.statisticservice;
import com.example.myappbackend.dto.statisticdto.BarChartResponseDTO;
import com.example.myappbackend.dto.statisticdto.PieChartResponseDTO;
import com.example.myappbackend.exception.ResourceNotFoundException;
import com.example.myappbackend.model.User;
import com.example.myappbackend.repository.OrdersRepository;
import com.example.myappbackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@AllArgsConstructor
@Service
public class StatisticsService {

    @Autowired
    private OrdersRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    public List<BarChartResponseDTO> getRevenueByTimeRange(String username, LocalDate startDate, LocalDate endDate) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        if (user.getStore() == null || user.getStore().getStoreId() == null) {
            throw new ResourceNotFoundException("Store not found for user: " + username);
        }

        Integer storeId = user.getStore().getStoreId();

        // ✅ Chuyển LocalDate sang LocalDateTime cho đúng kiểu với cột createdAt
        LocalDateTime startDateTime = startDate.atStartOfDay();             // 00:00:00
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);         // 23:59:59.999999999

        // ✅ Gọi repository đúng kiểu tham số
        List<Object[]> results = orderRepository.findRevenueByStoreAndDateRange(storeId, startDateTime, endDateTime);

        return results.stream()
                .map(result -> {
                    // Tùy DB trả về java.sql.Date, Timestamp hoặc LocalDateTime
                    LocalDate date;
                    if (result[0] instanceof java.sql.Date) {
                        date = ((java.sql.Date) result[0]).toLocalDate();
                    } else if (result[0] instanceof java.sql.Timestamp) {
                        date = ((Timestamp) result[0]).toLocalDateTime().toLocalDate();
                    } else if (result[0] instanceof LocalDateTime) {
                        date = ((LocalDateTime) result[0]).toLocalDate();
                    } else {
                        throw new IllegalArgumentException("Unsupported date type: " + result[0].getClass());
                    }

                    BigDecimal revenue = result[1] instanceof BigDecimal
                            ? (BigDecimal) result[1]
                            : new BigDecimal(result[1].toString());
                    System.out.println("StoreID: " + storeId);
                    System.out.println("Start: " + startDateTime);
                    System.out.println("End: " + endDateTime);
                    return new BarChartResponseDTO(date, revenue);
                })
                .collect(Collectors.toList());
    }


    public List<PieChartResponseDTO> getRevenueByCategoryForMonth(String username, int year, int month) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        if (user.getStore() == null || user.getStore().getStoreId() == null) {
            throw new ResourceNotFoundException("User is not associated with any store");
        }

        Integer storeId = user.getStore().getStoreId();

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<Object[]> results = orderRepository.findRevenueByCategoryForMonth(storeId, startDate, endDate);

        return results.stream()
                .map(result -> {
                    String category = (String) result[0];
                    BigDecimal revenue = result[1] instanceof BigDecimal
                            ? (BigDecimal) result[1]
                            : new BigDecimal(result[1].toString());
                    return new PieChartResponseDTO(category, revenue);
                })
                .collect(Collectors.toList());
    }

}