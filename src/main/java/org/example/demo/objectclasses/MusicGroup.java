package org.example.demo.objectclasses;

import java.util.List;

public class MusicGroup {
    private int id;
    private String name;
    private int formationYear;

    public MusicGroup(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public MusicGroup() {
    }

    // Геттеры и Сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFormationYear() {
        return formationYear;
    }

    public void setFormationYear(int formationYear) {
        this.formationYear = formationYear;
    }

    @Override
    public String toString() {
        return name;
    }

    public String toNote() {
        return "MusicGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", formationYear=" + formationYear +
                '}';
    }
}
