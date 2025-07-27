package com.example.springbatchdemo.batch;

import static com.example.springbatchdemo.constants.ApplicationConstants.CHUNK_SIZE;
import static com.example.springbatchdemo.constants.ApplicationConstants.DELIMITER;
import static com.example.springbatchdemo.constants.ApplicationConstants.FIELD_NAMES;
import static com.example.springbatchdemo.constants.ApplicationConstants.FILE_PATH;
import static com.example.springbatchdemo.constants.ApplicationConstants.PEOPLE_FILE_READER;
import static com.example.springbatchdemo.constants.ApplicationConstants.PEOPLE_INSERT_JOB;
import static com.example.springbatchdemo.constants.ApplicationConstants.PEOPLE_INSERT_STEP;

import java.time.LocalDate;
import com.example.springbatchdemo.dao.entities.Person;
import com.example.springbatchdemo.dao.repositories.PersonRepository;
import com.example.springbatchdemo.logger.ChunkSizeLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class PeopleInsertingJob {

  private final JobRepository jobRepository;
  private final PersonRepository personRepository;
  private final PlatformTransactionManager transactionManager;

  @Bean
  public Job peopleInsertJob() {
    return new JobBuilder(PEOPLE_INSERT_JOB, jobRepository)
            .start(peopleInsertStep())
            .build();
  }

  @Bean
  public Step peopleInsertStep() {
    return new StepBuilder(PEOPLE_INSERT_STEP, jobRepository)
            .<Person, Person>chunk(CHUNK_SIZE, transactionManager)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .listener(chunkSizeLogger())
            .build();
  }

  @Bean
  public FlatFileItemReader<Person> reader() {
    return new FlatFileItemReaderBuilder<Person>()
            .name(PEOPLE_FILE_READER)
            .resource(new ClassPathResource(FILE_PATH))
            .linesToSkip(1)
            .lineMapper(lineMapper())
            .targetType(Person.class)
            .build();
  }

  private LineMapper<Person> lineMapper() {
    DefaultLineMapper<Person> lineMapper = new DefaultLineMapper<>();

    DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
    lineTokenizer.setDelimiter(DELIMITER);
    lineTokenizer.setStrict(false);
    lineTokenizer.setNames(FIELD_NAMES);

    lineMapper.setLineTokenizer(lineTokenizer);
    lineMapper.setFieldSetMapper( (fieldSet) -> {
        Person person = Person.builder()
                .firstName(fieldSet.readString("firstName"))
                .lastName(fieldSet.readString("lastName"))
                .email(fieldSet.readString("email"))
                .build();

        String dateStr = fieldSet.readString("birthDate");
        person.setBirthDate(LocalDate.parse(dateStr));
        return person;
    });
    return lineMapper;
  }

  @Bean
  public ItemProcessor<Person, Person> processor() { //will do processing before storing data in DB
    return entity -> {
      entity.setFirstName(entity.getFirstName().toLowerCase());
      entity.setLastName(entity.getLastName().toLowerCase());
      return entity;
    };
  }

  @Bean
  public ItemWriter<Person> writer() {
    return personRepository::saveAll;
  }

  @Bean
  public ItemWriteListener<Person> chunkSizeLogger() {
    return new ChunkSizeLogger();
  }
}
