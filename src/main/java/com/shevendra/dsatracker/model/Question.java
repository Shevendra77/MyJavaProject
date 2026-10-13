package com.shevendra.dsatracker.model;

import jakarta.persistence.*;

@Entity
@Table(name = "dsa_questions", uniqueConstraints = @UniqueConstraint(columnNames = {"topic", "title"}))
public class Question {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 60)
    private String topic;
    @Column(nullable = false, length = 180)
    private String title;
    @Column(nullable = false)
    private boolean solved = false;

    protected Question() {}
    public Question(String topic, String title) { this.topic = topic; this.title = title; }
    public Long getId() { return id; }
    public String getTopic() { return topic; }
    public String getTitle() { return title; }
    public boolean isSolved() { return solved; }
    public void setSolved(boolean solved) { this.solved = solved; }
}
