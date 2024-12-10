package org.example.demo.objectclasses;

public class Repertoire {
    private int id;
    private String name;
    private Integer chartPos;
    private MusicGroup musicGroup;

    public Repertoire(MusicGroup group, String name, Integer chartPos) {
        this.musicGroup = group;
        this.name = name;
        this.chartPos = chartPos;
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

    public Integer getChartPos() {
        return chartPos;
    }

    public void setChartPos(Integer chartPos) {
        this.chartPos = chartPos;
    }

    public MusicGroup getMusicGroup() {
        return musicGroup;
    }

    public void setMusicGroup(MusicGroup musicGroup) {
        this.musicGroup = musicGroup;
    }

    @Override
    public String toString() {
        return musicGroup.getName() + " - " + name;
    }
}
