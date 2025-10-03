package org.molsh.repository;

import jakarta.transaction.Transactional;
import org.molsh.common.ProcessingTaskStatus;
import org.molsh.entity.ProcessingTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessingTaskRepository extends JpaRepository<ProcessingTask, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE ProcessingTask " +
            "SET status = :status " +
            "WHERE id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") ProcessingTaskStatus status);
}
