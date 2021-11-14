package com.akapitan.demo.springjooqddd.domain.minion;

import com.akapitan.demo.springjooqddd.domain.tables.Minion;
import com.akapitan.demo.springjooqddd.domain.tables.records.MinionRecord;
import java.util.List;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class MinionRepository {

  private final DSLContext dsl;

  public MinionRepository(DSLContext dsl) {
    this.dsl = dsl;
  }

  public List<MinionRecord> findAll() {
    return dsl.selectFrom(Minion.MINION).fetchInto(MinionRecord.class);
  }

  public List<MinionRecord> findAllByNameIgnoreCase(String name) {
    return dsl.selectFrom(Minion.MINION)
        .where(Minion.MINION.NAME.likeIgnoreCase(name))
        .fetchInto(MinionRecord.class);
  }

  public List<MinionRecord> findAllByContainingNameIgnoreCase(String name) {
    return dsl.selectFrom(Minion.MINION)
        .where(Minion.MINION.NAME.containsIgnoreCase(name))
        .fetchInto(MinionRecord.class);
  }
}
