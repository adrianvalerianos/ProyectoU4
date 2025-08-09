package database;

import models.Tarea;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DBManagement {
    public static void crearTabla() {
        String sql = """
                CREATE TABLE IF NOT EXISTS tareas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT NOT NULL,
                descripcion TEXT NOT NULL,
                fecha TEXT NOT NULL,
                color TEXT NOT NULL
                );""";

        DBConnection db = new DBConnection();
        try (Connection connection = db.getConnection();
             Statement stm = connection.createStatement()){
            stm.execute(sql);
            System.out.println("Se inicializó la base de datos");
        } catch (Exception e) {
            System.out.println("Tronó lol "+ e.getMessage());
        } finally {
            db.closeConnection();
        }
    }

    public static void guardarTarea(Tarea tarea) {
        String sql = "INSERT INTO tareas (titulo, descripcion, fecha, color) " +
                "VALUES (?, ?, ?, ?)";

        DBConnection db = new DBConnection();
        try (Connection connection = db.getConnection();
            Statement stm = connection.createStatement()){
            PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstm.setString(1, tarea.getTitulo());
            pstm.setString(2, tarea.getDescripcion());
            pstm.setString(3, tarea.getFecha());
            pstm.setString(4, tarea.getColor());

            pstm.executeUpdate();
            System.out.println("Tarea guardada en la base de datos.");

        } catch (Exception e) {
            System.err.println("Error al guardar la tarea: " + e.getMessage());
        } finally {
            db.closeConnection();
        }
    }

    public static ArrayList<Tarea> buscarTareas(String fecha) {
        ArrayList<Tarea> tareasEncontradas = new ArrayList<>();
        String sql = "SELECT * FROM tareas WHERE fecha = ?";

        DBConnection db = new DBConnection();
        try (Connection connection = db.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setString(1, fecha);

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    Tarea tarea = new Tarea();
                    tarea.setId(rs.getInt("id"));
                    tarea.setTitulo(rs.getString("titulo"));
                    tarea.setDescripcion(rs.getString("descripcion"));
                    tarea.setFecha(rs.getString("fecha"));
                    tarea.setColor(rs.getString("color"));
                    tareasEncontradas.add(tarea);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar tareas: " + e.getMessage());
            return new ArrayList<>();
        } finally {
            db.closeConnection();
        }

        return tareasEncontradas;
    }

    public static void editarTarea(Tarea tarea) {
        String sql = "UPDATE tareas SET " +
                "titulo = ?, descripcion = ?, fecha = ?, color = ? " +
                "WHERE id = ?;";

        DBConnection db = new DBConnection();
        try (Connection connection = db.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setString(1, tarea.getTitulo());
            pstm.setString(2, tarea.getDescripcion());
            pstm.setString(3, tarea.getFecha());
            pstm.setString(4, tarea.getColor());

            pstm.setInt(5, tarea.getId());

            int filasAfectadas = pstm.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Tarea actualizada correctamente.");
            } else {
                System.out.println("No se encontró la tarea con ID: " + tarea.getId());
            }

        } catch (Exception e) {
            System.err.println("Error al actualizar partida: " + e.getMessage());
        } finally {
            db.closeConnection();
        }
    }

    public static void eliminarTarea(int index) {
        String sql = "DELETE FROM tareas WHERE id = " + index + ";";

        DBConnection db = new DBConnection();
        try (Connection connection = db.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.executeUpdate();
            System.out.println("Tarea borrada con éxito");
            System.out.println(sql);
        } catch (SQLException e) {
            System.err.println("Error al borrar la tarea: " + e.getMessage());
        } finally {
            db.closeConnection();
        }
    }
}
