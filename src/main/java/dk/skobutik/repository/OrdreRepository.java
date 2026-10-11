package dk.skobutik.repository;

import dk.skobutik.model.Ordre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdreRepository extends JpaRepository<Ordre, Long> {

    List<Ordre> findAllByOrderByOprettetDesc();
}
