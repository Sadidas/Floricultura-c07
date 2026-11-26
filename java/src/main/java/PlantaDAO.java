import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlantaDAO {

    public boolean inserirPlanta(String nome, String especie, BigDecimal preco, int estoque) {
        String sql = "INSERT INTO planta (nome, especie, preco, estoque) VALUES (?, ?, ?, ?)";
        try (Connection conexao = ConnectionDAO.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, especie);
            stmt.setBigDecimal(3, preco);
            stmt.setInt(4, estoque);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Planta> listarPlantas() {
        String sql = "SELECT id, nome, especie, preco, estoque FROM planta ORDER BY id";
        List<Planta> plantas = new ArrayList<>();

        try (Connection conexao = ConnectionDAO.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                plantas.add(mapearPlanta(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plantas;
    }

    public List<Planta> buscarPorEspecie(String especie) {
        String sql = """
                SELECT id, nome, especie, preco, estoque
                FROM planta
                WHERE especie = ?
                ORDER BY id
                """;
        List<Planta> plantas = new ArrayList<>();

        try (Connection conexao = ConnectionDAO.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, especie);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    plantas.add(mapearPlanta(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plantas;
    }

    public boolean atualizarPreco(int id, BigDecimal novoPreco) {
        String sql = "UPDATE planta SET preco = ? WHERE id = ?";
        try (Connection conexao = ConnectionDAO.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setBigDecimal(1, novoPreco);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removerPlanta(int id) {
        String sql = "DELETE FROM planta WHERE id = ?";
        try (Connection conexao = ConnectionDAO.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public BigDecimal valorTotalEstoque() {
        String sql = "SELECT COALESCE(SUM(preco * estoque), 0) AS total FROM planta";
        try (Connection conexao = ConnectionDAO.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getBigDecimal("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    private Planta mapearPlanta(ResultSet rs) throws SQLException {
        return new Planta(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("especie"),
                rs.getBigDecimal("preco"),
                rs.getInt("estoque")
        );
    }
}
