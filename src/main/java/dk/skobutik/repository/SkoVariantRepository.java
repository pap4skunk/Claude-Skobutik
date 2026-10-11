package dk.skobutik.repository;

import dk.skobutik.model.SkoVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkoVariantRepository extends JpaRepository<SkoVariant, Long> {
}
