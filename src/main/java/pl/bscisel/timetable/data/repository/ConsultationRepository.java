package pl.bscisel.timetable.data.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.bscisel.timetable.data.entity.Consultation;

import java.util.List;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

}
