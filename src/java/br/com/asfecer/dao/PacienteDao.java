package br.com.asfecer.dao;

import br.com.asfecer.model.Paciente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacienteDao {
    
    private final PreparedStatement opListar;
    private final PreparedStatement opSalvar;
    private final PreparedStatement opPesquisa;
    private final PreparedStatement opAtualiza;
    private final PreparedStatement opExcluir;
    

    public PacienteDao() throws Exception {
        Connection conexao = ConnectionFactory.createConnection();
        opListar = conexao.prepareStatement("SELECT * FROM paciente");
        opSalvar = conexao.prepareStatement("INSERT INTO paciente(nomePaciente, dataNascimento, nomeMae, "
                + "NaturalidadeCidade, cpf, cartaoConvenio, Convenio, tipoSanguineo, fatorRH, sexo, "
                + "Endereco, email, telefone, celular, obs ) Values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        opPesquisa = conexao.prepareStatement("SELECT * FROM paciente WHERE nomePaciente LIKE ? OR  cpf LIKE ?"); // usar %LIKE%
        opAtualiza = conexao.prepareStatement("UPDATE paciente SET nomePaciente = ?, dataNascimento = ?, nomeMae = ?, "
                + "NaturalidadeCidade = ?, cpf = ?, cartaoConvenio = ?, Convenio = ?, tipoSanguineo = ?, fatorRH = ?, sexo = ?, "
                + "Endereco = ?, email = ?, telefone = ?, celular = ?, obs = ? WHERE idPaciente = ?");
        opExcluir = conexao.prepareStatement("DELETE FROM paciente WHERE idPaciente = ?");
    }

    public List<Paciente> listarTodos() throws Exception {
        List<Paciente> pacientes = new ArrayList<>();

        ResultSet resultado = opListar.executeQuery();
        while (resultado.next()) {
            Paciente pacienteAtual = new Paciente();
            pacienteAtual.setIdPaciente(resultado.getInt("idPaciente"));
            pacienteAtual.setNomePaciente(resultado.getString("nomePaciente"));
            //pacienteAtual.setDataNascimento(resultado.getString("dataNascimento"));
            pacienteAtual.setNomeMae(resultado.getString("nomeMae"));
            //pacienteAtual.setNaturalidadeCidade(resultado.getString("NaturalidadeCidade"));
            pacienteAtual.setCpf(resultado.getString("cpf"));
            pacienteAtual.setCartaoConvenio(resultado.getString("cartaoConvenio"));
            //pacienteAtual.setConvenio(resultado.getConvenio("Convenio"));
            pacienteAtual.setTipoSanguineo(resultado.getString("tipoSanguineo"));
            //pacienteAtual.setFatorRH(resultado.getString("fatorRH"));
            //pacienteAtual.setSexo(resultado.getString("sexo"));
            //pacienteAtual.setEndereco(resultado.getString("Endereco"));
            pacienteAtual.setEmail(resultado.getString("email"));
            pacienteAtual.setTelefone(resultado.getString("telefone"));
            pacienteAtual.setCelular(resultado.getString("celular"));
            pacienteAtual.setObs(resultado.getString("obs"));
            pacientes.add(pacienteAtual);
        }
        return pacientes;
    }
    
    
    PAREI AQUI;
    

    public boolean criarPaciente(Paciente novoPaciente) throws Exception {
        try {
            opSalvar.clearParameters();
            opSalvar.setString(1, novoPaciente.getNomePaciente());
            opSalvar.setString(2, novoPaciente.getProntuario());
            opSalvar.setString(3, novoPaciente.getEndereco());
            opSalvar.setString(4, novoPaciente.getCidade());
            opSalvar.setString(5, novoPaciente.getTelFixo());
            opSalvar.setString(6, novoPaciente.getCelular());
            opSalvar.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    public List<Paciente> pesquiarPaciente(String pesquisa) throws Exception {
        try {
            List<Paciente> listPacientes = new ArrayList<>();
            pesquisa = "%" + pesquisa + "%";
            opPesquisa.clearParameters();
            opPesquisa.setString(1, pesquisa);
            opPesquisa.setString(2, pesquisa);
            ResultSet resultado = opPesquisa.executeQuery();
            while(resultado.next()){
                Paciente pacienteAtual = new Paciente();
                pacienteAtual.setIdPaciente(resultado.getInt("idPaciente"));
                pacienteAtual.setNomePaciente(resultado.getString("nomePaciente"));
                pacienteAtual.setProntuario(resultado.getString("prontuario"));
                pacienteAtual.setEndereco(resultado.getString("endereco"));
                pacienteAtual.setCidade(resultado.getString("cidade"));
                pacienteAtual.setTelFixo(resultado.getString("telFixo"));
                pacienteAtual.setCelular(resultado.getString("celular"));
                listPacientes.add(pacienteAtual);
            }

            return listPacientes;
        } catch (SQLException ex) {
            //alterar para JOptionPane
            throw new Exception("Não foi possível pesquisar por este nome.", ex);
        }
    }

    
    public boolean atualizarPaciente(Paciente paciente) throws Exception {
        try{
            
            opAtualiza.clearParameters();
            opAtualiza.setString(1, paciente.getNomePaciente());
            opAtualiza.setString(2, paciente.getProntuario());
            opAtualiza.setString(3, paciente.getEndereco());
            opAtualiza.setString(4, paciente.getCidade());
            opAtualiza.setString(5, paciente.getTelFixo());
            opAtualiza.setString(6, paciente.getCelular());
            opAtualiza.setInt(7, paciente.getIdPaciente());
            opAtualiza.executeUpdate();
            return true;

        } catch(SQLException ex){
            return false;
        }
    }
    
    public void excluirPaciente(String id) throws Exception {
        try{
            opExcluir.setString(1, id);
            opExcluir.executeUpdate();
        } catch(SQLException ex){
            // alterar para JOptionPane
            throw new Exception("Parou aqui."); 
        }
    }
    
}
