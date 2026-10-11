package dk.skobutik.repository;

import dk.skobutik.model.Kategori;
import dk.skobutik.model.Sko;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SkoRepository extends JpaRepository<Sko, Long> {

    @Query("""
            select s from Sko s
            where (:kategori is null or s.kategori = :kategori)
              and (:soeg is null
                   or lower(s.navn) like lower(concat('%', :soeg, '%'))
                   or lower(s.maerke) like lower(concat('%', :soeg, '%')))
            order by s.maerke, s.navn
            """)
    List<Sko> soeg(@Param("kategori") Kategori kategori, @Param("soeg") String soeg);
}
