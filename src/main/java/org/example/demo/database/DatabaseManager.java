package org.example.demo.database;

import org.example.demo.objectclasses.Artist;
import org.example.demo.objectclasses.Concert;
import org.example.demo.objectclasses.MusicGroup;
import org.example.demo.objectclasses.Repertoire;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/tourmanagement";
    private static final String USER = "java_user";
    private static final String PASSWORD = "123";

    // Метод для получения соединения
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Добавление музыкальной группы
    public static void addMusicGroup(MusicGroup group) throws SQLException {
        String sql = "INSERT INTO musicgroups (musicgroup_name, musicgroup_formationyear) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, group.getName());
            stmt.setInt(2, group.getFormationYear());
            stmt.executeUpdate();
        }
    }

    public static void addConcert(Concert concert) throws SQLException {
        String query = "INSERT INTO concerts (concert_date, concert_mgid, concert_city, concert_venue, " +
                "concert_orgname, concert_orgphone) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setDate(1, Date.valueOf(concert.getDate()));
            preparedStatement.setInt(2, concert.getMusicGroup().getId());
            preparedStatement.setString(3, concert.getCity());
            preparedStatement.setString(4, concert.getVenue());
            preparedStatement.setString(5, concert.getOrgName());
            preparedStatement.setString(6, concert.getOrgPhone());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Не удалось добавить концерт, запись не была добавлена.");
            }
        }
    }

    public static void addRepertoire(Repertoire repertoire) throws SQLException {
        String query = "INSERT INTO repertoire (repertoire_mgid, repertoire_name, repertoire_chart)" +
                "VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, repertoire.getMusicGroup().getId());
            preparedStatement.setString(2, repertoire.getName());

            // Если chartPos = null, передаём null в запрос
            if (repertoire.getChartPos() != null) {
                preparedStatement.setInt(3, repertoire.getChartPos());
            } else {
                preparedStatement.setNull(3, Types.INTEGER);
            }

            preparedStatement.executeUpdate();
        }
    }

    public static void addArtist(Artist artist) throws SQLException {
        String query = "INSERT INTO artists (artist_mgid, artist_name, artist_phone)" +
                "VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, artist.getMusicGroup().getId());
            preparedStatement.setString(2, artist.getName());
            preparedStatement.setString(3, artist.getPhone());

            preparedStatement.executeUpdate();
        }
    }

    // Обновление музыкальной группы
    public static void updateMusicGroup(MusicGroup group) throws SQLException {
        String sql = "UPDATE musicgroups SET musicgroup_name = ?, musicgroup_formationyear = ? WHERE musicgroup_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, group.getName());
            stmt.setInt(2, group.getFormationYear());
            stmt.setInt(3, group.getId());
            stmt.executeUpdate();
        }
    }

    public static void updateConcert(Concert concert) throws SQLException {
        String query = "UPDATE concerts SET concert_mgid = ?, concert_date = ?, concert_city = ?, " +
                "concert_venue = ?, concert_orgname = ?, concert_orgphone = ? WHERE concert_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, concert.getMusicGroup().getId());
            statement.setDate(2, java.sql.Date.valueOf(concert.getDate()));
            statement.setString(3, concert.getCity());
            statement.setString(4, concert.getVenue());
            statement.setString(5, concert.getOrgName());
            statement.setString(6, concert.getOrgPhone());
            statement.setInt(7, concert.getId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Концерт успешно обновлен в базе данных.");
            } else {
                throw new SQLException("Запись не найдена или не обновлена.");
            }
        }
    }

    public static void updateRepertoire(Repertoire repertoire) throws SQLException {
        String query = "UPDATE repertoire SET repertoire_mgid = ?, repertoire_name = ?, repertoire_chart = ? " +
                "WHERE repertoire_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, repertoire.getMusicGroup().getId());
            statement.setString(2, repertoire.getName());
            if (repertoire.getChartPos() != null) {
                statement.setInt(3, repertoire.getChartPos());
            } else {
                statement.setNull(3, Types.INTEGER);
            }
            statement.setInt(4, repertoire.getId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Репертуар успешно обновлен в базе данных.");
            } else {
                throw new SQLException("Запись не найдена или не обновлена.");
            }
        }
    }

    public static void updateArtist(Artist artist) throws SQLException {
        String query = "UPDATE artists SET artist_mgid = ?, artist_name = ?, artist_phone = ? " +
                "WHERE artist_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, artist.getMusicGroup().getId());
            statement.setString(2, artist.getName());
            statement.setString(3, artist.getPhone());
            statement.setInt(4, artist.getId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Артист успешно обновлен в базе данных.");
            } else {
                throw new SQLException("Запись не найдена или не обновлена.");
            }
        }
    }

    // Загрузка музыкальных групп
    public static List<MusicGroup> getMusicGroups() throws SQLException {
        String sql = "SELECT * FROM musicgroups";
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            List<MusicGroup> groups = new ArrayList<>();
            while (rs.next()) {
                MusicGroup group = new MusicGroup();
                group.setId(rs.getInt("musicgroup_id"));
                group.setName(rs.getString("musicgroup_name"));
                group.setFormationYear(rs.getInt("musicgroup_formationyear"));
                groups.add(group);
            }
            return groups;
        }
    }

    public static List<MusicGroup> getAllMusicGroups() throws SQLException {
        String query = "SELECT * FROM musicgroups";
        List<MusicGroup> musicGroups = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                MusicGroup group = new MusicGroup(
                        resultSet.getInt("musicgroup_id"),
                        resultSet.getString("musicgroup_name")
                );
                musicGroups.add(group);
            }
        }
        return musicGroups;
    }

    public static List<Concert> getConcerts() throws SQLException {
        String sql = """
                    SELECT c.concert_id, c.concert_date, c.concert_city, c.concert_venue, 
                           c.concert_orgname, c.concert_orgphone, mg.musicgroup_id, mg.musicgroup_name 
                    FROM concerts c
                    JOIN musicgroups mg ON c.concert_mgid = mg.musicgroup_id
                """;

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            List<Concert> concerts = new ArrayList<>();
            while (rs.next()) {
                MusicGroup group = new MusicGroup(
                        rs.getInt("musicgroup_id"),
                        rs.getString("musicgroup_name")
                );
                Concert concert = new Concert(
                        rs.getDate("concert_date").toLocalDate(),
                        group,
                        rs.getString("concert_city"),
                        rs.getString("concert_venue"),
                        rs.getString("concert_orgname"),
                        rs.getString("concert_orgphone")
                );
                concert.setId(rs.getInt("concert_id"));
                concerts.add(concert);
            }
            return concerts;
        }
    }

    public static List<Repertoire> getRepertoire() throws SQLException {
        String sql = """
                    SELECT c.repertoire_id, c.repertoire_name, c.repertoire_chart, 
                    mg.musicgroup_id, mg.musicgroup_name 
                    FROM repertoire c
                    JOIN musicgroups mg ON c.repertoire_mgid = mg.musicgroup_id
                """;

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            List<Repertoire> repertoire = new ArrayList<>();
            while (rs.next()) {
                MusicGroup group = new MusicGroup(
                        rs.getInt("musicgroup_id"),
                        rs.getString("musicgroup_name")
                );

                // Проверяем, равен ли chart 0, и заменяем его на null
                Integer chart = rs.getInt("repertoire_chart");
                if (chart == 0 && rs.wasNull()) {
                    chart = null;
                }

                Repertoire track = new Repertoire(
                        group,
                        rs.getString("repertoire_name"),
                        chart // передаем null вместо 0
                );
                track.setId(rs.getInt("repertoire_id"));
                repertoire.add(track);
            }
            return repertoire;
        }
    }

    public static List<Artist> getArtist() throws SQLException {
        String sql = """
                    SELECT c.artist_id, c.artist_name, c.artist_phone, 
                    mg.musicgroup_id, mg.musicgroup_name 
                    FROM artists c 
                    JOIN musicgroups mg ON c.artist_mgid = mg.musicgroup_id
                """;

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            List<Artist> artists = new ArrayList<>();
            while (rs.next()) {
                MusicGroup group = new MusicGroup(
                        rs.getInt("musicgroup_id"),
                        rs.getString("musicgroup_name")
                );

                Artist artist = new Artist(
                        group,
                        rs.getString("artist_name"),
                        rs.getString("artist_phone")
                );
                artist.setId(rs.getInt("artist_id"));
                artists.add(artist);
            }
            return artists;
        }
    }

    // Удаление музыкальной группы
    public static void deleteMusicGroup(MusicGroup group) throws SQLException {
        String query = "DELETE FROM musicgroups WHERE musicgroup_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, group.getId());
            statement.executeUpdate();
        }
    }

    public static void deleteConcert(Concert concert) throws SQLException {
        String query = "DELETE FROM concerts WHERE concert_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, concert.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Концерт успешно удален из базы данных.");
            } else {
                throw new SQLException("Запись не найдена или не удалена.");
            }
        }
    }

    public static void deleteRepertoire(Repertoire repertoire) throws SQLException {
        String query = "DELETE FROM repertoire WHERE repertoire_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, repertoire.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Репертуар успешно удален из базы данных.");
            } else {
                throw new SQLException("Запись не найдена или не удалена.");
            }
        }
    }

    public static void deleteArtist(Artist artist) throws SQLException {
        String query = "DELETE FROM artists WHERE artist_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, artist.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Артист успешно удален из базы данных.");
            } else {
                throw new SQLException("Запись не найдена или не удалена.");
            }
        }
    }
}
