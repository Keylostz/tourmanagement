package org.example.demo.objectclasses;

public class Artist {
    private int id;
    private String name;
    private String phone;
    private MusicGroup musicGroup;

    public Artist(MusicGroup group, String artistName, String phone) {
        musicGroup = group;
        this.name = artistName;
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public MusicGroup getMusicGroup() {
        return musicGroup;
    }

    public void setMusicGroup(MusicGroup musicGroup) {
        this.musicGroup = musicGroup;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phone + '\'' +
                ", musicGroup=" + musicGroup.getName() +
                '}';
    }
}
