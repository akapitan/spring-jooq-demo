package com.akapitan.demo.springjooqddd.domain.minion;

import com.akapitan.demo.springjooqddd.domain.tables.Minion;
import com.akapitan.demo.springjooqddd.domain.tables.records.MinionRecord;
import java.util.List;
import java.util.UUID;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class MinionRepository {

  public static final Minion MINION = Minion.MINION;
  private final DSLContext dsl;

  public MinionRepository(DSLContext dsl) {
    this.dsl = dsl;
  }

  public List<MinionRecord> findAll() {
    return dsl.selectFrom(MINION).fetchInto(MinionRecord.class);
  }

  public List<MinionRecord> findAllByNameIgnoreCase(String name) {
    return dsl.selectFrom(MINION)
        .where(MINION.NAME.likeIgnoreCase(name))
        .fetchInto(MinionRecord.class);
  }

  public List<MinionRecord> findAllByContainingNameIgnoreCase(String name) {
    return dsl.selectFrom(MINION)
        .where(MINION.NAME.containsIgnoreCase(name))
        .fetchInto(MinionRecord.class);
  }

  public MinionRecord save(MinionRecord minion) {
    if (minion.getId() == null) {
      minion.setId(UUID.randomUUID());
      minion.setVersion(0);
      return dsl.insertInto(MINION, MINION.ID, MINION.VERSION, MINION.NAME, MINION.NUMBER_OF_EYES)
          .values(minion.getId(), minion.getVersion(), minion.getName(), minion.getNumberOfEyes())
          .returning()
          .fetchOne();
    }
    minion.setVersion(minion.getVersion() + 1);
    dsl.update(MINION).set(minion).execute();

    return dsl.fetchOne(MINION, MINION.ID.eq(minion.getId()));

  }
}
