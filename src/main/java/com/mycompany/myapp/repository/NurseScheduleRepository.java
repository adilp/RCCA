package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.NurseSchedule;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the NurseSchedule entity.
 */
public interface NurseScheduleRepository extends JpaRepository<NurseSchedule,Long> {

}
