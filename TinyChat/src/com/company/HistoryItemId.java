package com.company;

import java.util.UUID;

public class HistoryItemId implements Comparable<HistoryItemId> {

    public HistoryItemId() {
        id = UUID.randomUUID();
    }

    public HistoryItemId(String id) throws IllegalArgumentException {
        this.id = UUID.fromString(id);
    }

    @Override
    public int compareTo(HistoryItemId other) {
        return this.id.compareTo(other.id);
    }

    @Override
    public String toString() {
        return id.toString();
    }

    private UUID id;

}
