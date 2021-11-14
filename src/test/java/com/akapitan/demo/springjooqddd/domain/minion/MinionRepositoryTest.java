package com.akapitan.demo.springjooqddd.domain.minion;

import static org.assertj.core.api.Assertions.assertThat;

import com.akapitan.demo.springjooqddd.PostgreSqlContainerConfiguration;
import com.akapitan.demo.springjooqddd.domain.tables.records.MinionRecord;
import java.util.List;
import java.util.Locale;
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
  @Autowired
  private MinionRepository repository;

  @Test
  void findAll_noInput_shouldReturnAllMinions() {
    List<MinionRecord> all = repository.findAll();

    assertThat(all).size().isEqualByComparingTo(3);
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
    MinionRecord ivan = new MinionRecord(null, null, "Ivan", "2", null, null);
    MinionRecord save = repository.save(ivan);

    save.setName("ivan2");
    save.setNumberOfEyes("3");
    MinionRecord saveReloaded = repository.save(save);

    System.out.println(saveReloaded);
  }

}