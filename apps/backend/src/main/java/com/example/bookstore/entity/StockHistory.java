package com.example.bookstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "stock_history")
public class StockHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Plain book id (no FK): history is append-only and survives book deletion.
    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @Enumerated(EnumType.STRING)
    @Column(name = "change_type", nullable = false)
    private StockChangeType changeType;

    @Column(name = "previous_level", nullable = false)
    private int previousLevel;

    @Column(name = "new_level", nullable = false)
    private int newLevel;

    // ISO-8601 UTC instant string; lexicographic order == chronological order.
    @Column(name = "changed_at", nullable = false)
    private String changedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public StockChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(StockChangeType changeType) {
        this.changeType = changeType;
    }

    public int getPreviousLevel() {
        return previousLevel;
    }

    public void setPreviousLevel(int previousLevel) {
        this.previousLevel = previousLevel;
    }

    public int getNewLevel() {
        return newLevel;
    }

    public void setNewLevel(int newLevel) {
        this.newLevel = newLevel;
    }

    public String getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(String changedAt) {
        this.changedAt = changedAt;
    }
}
