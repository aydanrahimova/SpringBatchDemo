package com.example.springbatchdemo.dao.entities;

import static jakarta.persistence.GenerationType.IDENTITY;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "person")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;
  private String firstName;
  private String lastName;
  private LocalDate birthDate;
  private String email;

}
