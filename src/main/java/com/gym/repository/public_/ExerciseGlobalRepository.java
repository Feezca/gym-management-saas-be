package com.gym.repository.public_;

import com.gym.domain.public_.entity.ExerciseGlobal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseGlobalRepository extends JpaRepository<ExerciseGlobal, Long> {
}
