package com.akapitan.demo.springjooqddd.domain.minion;

import static org.assertj.core.api.Assertions.assertThat;

import com.akapitan.demo.springjooqddd.PostgreSqlContainerConfiguration;
import com.akapitan.demo.springjooqddd.domain.tables.records.MinionRecord;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.jooq.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

@JdbcTest
@Import({PostgreSqlContainerConfiguration.class, MinionRepository.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(config = @SqlConfig(separator = ScriptUtils.EOF_STATEMENT_SEPARATOR))
class MinionRepositoryTest {

  public static final String MINION_NAME = "aco";
  public static final List<Object> MINION_NAMES = List.of("Aco", "Maco", "Kaco");
  @Autowired
  private MinionRepository repository;

  @Test
  void findAll_noInput_shouldReturnAllMinions() {
    List<MinionRecord> all = repository.findAll();
    assertThat(all).size().isEqualByComparingTo(3).returnToIterable().map(MinionRecord::getName)
        .allMatch(MINION_NAMES::contains);
  }

  @Test
  void findAllByName_givenStringName_shouldReturnAllMinionsWithName() {
    List<MinionRecord> all = repository.findAllByNameIgnoreCase(MINION_NAME);
    System.out.println(all);
    assertThat(all).size().isEqualTo(1).returnToIterable().allMatch(x -> x.getName().toLowerCase(
        Locale.ROOT).equals(MINION_NAME));
  }

  @Test
  void findAllByName_givenStringName_shouldReturnAllMinionsContainingName() {
    List<MinionRecord> all = repository.findAllByContainingNameIgnoreCase(MINION_NAME);
    System.out.println(all);
    assertThat(all).size().isEqualByComparingTo(3).returnToIterable()
        .allMatch(x -> (x.getName().toLowerCase(Locale.ROOT).contains(MINION_NAME)));
  }

  @Test
  void save_givenMinion_returnSavedMinion() {

    MinionRecord ivan = new MinionRecord(null, null, "Ivan", "2", UUID.fromString("00000001-0000-0000-0000-a00000000000")
        , JSON.json(prepareDescription()));
    MinionRecord minionSaved = repository.save(ivan);

    assertThat(minionSaved).isNotNull().extracting(MinionRecord::getId).isNotNull();
    assertThat(minionSaved).extracting(MinionRecord::getName).isEqualTo("Ivan");
    assertThat(minionSaved).extracting(MinionRecord::getNumberOfEyes).isEqualTo("2");

    minionSaved.setName("Ivan2");
    minionSaved.setNumberOfEyes("3");
    MinionRecord saveReloaded = repository.save(minionSaved);

    assertThat(saveReloaded).isNotNull().extracting(MinionRecord::getId).isEqualTo(minionSaved.getId());
    assertThat(saveReloaded).extracting(MinionRecord::getName).isEqualTo("Ivan2");
    assertThat(saveReloaded).extracting(MinionRecord::getNumberOfEyes).isEqualTo("3");
    System.out.println(saveReloaded);
  }

  private String prepareDescription() {
    Description description = new Description();
    description.appearance.put("hair", "thin");
    description.appearance.put("eyes", "2");
    description.appearance.put("glasses", "none");

    description.personality.put("character", "childish");
    description.personality.put("loves", "Poochy");
    description.personality.put("enjoys", "bedtime stories");
    description.personality.put("favorite-stuffed-animal", "Tim");

    return description.toJson().toString();
  }

}