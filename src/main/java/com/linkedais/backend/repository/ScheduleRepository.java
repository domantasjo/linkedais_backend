package com.linkedais.backend.repository;

import com.linkedais.backend.model.ScheduleEntry;
import com.linkedais.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<ScheduleEntry, Long> {
    List<ScheduleEntry> findByUserOrderByDayOfWeekAscStartTimeAsc(User user);
    List<ScheduleEntry> findByUserId(Long userId);
}
