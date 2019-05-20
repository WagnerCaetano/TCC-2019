/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;
import Classes.*;
import java.sql.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author 00
 */
public class CRUDTodos {
    /**
     * CRUD CLIENTE
     */
    public static void InserirCliente(Cliente c, Connection con) throws SQLException, Exception
    {
        if(c.notVazio()){

            String str = "Insert into ClienteFicha values(?,?,?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(str);
        
            ps.setString(1, c.getNome());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getCep());
            ps.setString(4, c.getEndereco());
            ps.setString(5, c.getCidade());
            ps.setString(6, c.getSenha());
            ps.setString(7, c.getCpf());
            ps.execute();

           }
        else
        throw new Exception("Erro");
    }
    public static void AlterarCliente(Cliente c, Connection con) throws SQLException, Exception
    {
         if(c.notVazio()){
        String str = "Update SGClienteFicha set cliNome = ?, cliTel = ?, cliEmail = ?, cliCEP = ?, cliAddress = ?, cliCidade = ?, cliCPF = ?, CliPsw = ? where cliId = ?";

        PreparedStatement ps = con.prepareStatement(str);

            ps.setString(1, c.getNome());
            ps.setInt(2, c.getTelefone());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getCep());
            ps.setString(5, c.getEndereco());
            ps.setString(6, c.getCidade());
            ps.setString(7, c.getSenha());
            ps.setString(8, c.getCpf());
            ps.execute();
        }
        else
        throw new Exception("Erro");
    }
    public static void DeletarCliente(int cpf, Connection con) throws SQLException, Exception
    {
        if(Metodos.isCPF(cpf+"")){
        String str = "Delete from SGClienteFicha where cliCPF = ?";

        PreparedStatement ps = con.prepareStatement(str);

        ps.setInt(1, cpf);
        ps.execute();
        }
    }
    
    public static ResultSet PesquisarCliente(Connection con) throws SQLException
    {
        String sql = "select * from SGClienteFicha";
        
        PreparedStatement ps = con.prepareStatement(sql);
        return ps.executeQuery();   
    }
    public static ResultSet PesquisarClientePorNome(String nome,Connection con) throws SQLException
    {
        String sql = "select * from SGClienteFicha where cliNome=?";
        
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, nome);
        return ps.executeQuery(); 
    }
    public static ResultSet PesquisarClientePorCPF(int cpf, Connection con) throws SQLException
    {
        String sql = "select * from SGClienteFicha where cliCPF=?";
        
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, cpf);
        return ps.executeQuery(); 
        
    }
    public static ResultSet PesquisarClientePorId(int id, Connection con) throws SQLException
    {
        String sql = "select * from SGClienteFicha where cliID=?";
        
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        return ps.executeQuery(); 
        
    }
    /**
     * CRUD NOTA FISCAL
     */
    
    public static void insertPDF(int id,String filename,Connection conn) {
        String query;
        PreparedStatement pstmt;
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd-HH_mm_ss");
	Date date = new Date();
	String data = (dateFormat.format(date)); //2016/11/16 12:08:43
        
        try {
            File file = new File(filename);
            query = "insert into SGArquivos (Id,Nome,Arquivo) values (?,(select BulkColumn FROM Openrowset( Bulk '"+file.getAbsolutePath()+"' , Single_Blob) as Arquivo))";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1,id+" "+data+".pdf");
            pstmt.executeUpdate();            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getPDFData(int id,Connection conn) {
        
        byte[] fileBytes;
        String query;
        String path=null;
        try {
            query = "select * from SGArquivos where id = (select ficArqId from SGFichaDeReclamacao where ficId=?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                fileBytes = rs.getBytes("arquivo");
                File file = new File("C://Temp//"+rs.getString("nome"));
                OutputStream targetFile= new FileOutputStream(file);
                targetFile.write(fileBytes);
                targetFile.close();
                path = file.getAbsolutePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }
    
    public static int getIdAvailable(Connection con) throws SQLException{
        String sql = "select max(Id) as 'UltimoId' from SGArquivos";
        
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery(); 
        return rs.getInt("UltimoId");
    } 
    
    /**
     * CRUD CAUSAS
     */
    public static ResultSet getCausas(String causa,Connection con) throws SQLException
    {
        String sql = "select cauId from SGCausas where cauCausa=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, causa);
        return ps.executeQuery();
    }
    public static ResultSet getCausas(Connection con) throws SQLException
    {
        String sql = "select count(*) 'cauFrequencia',cauCausa from SGCausas group by cauCausa";
        
        PreparedStatement ps = con.prepareStatement(sql);
        return ps.executeQuery();
    }
    public static void setCausas(String causa,Connection con) throws SQLException
    {
        String str = "Insert into SGCausas values(?)";
        PreparedStatement ps = con.prepareStatement(str);

        ps.setString(1, causa);
        ps.execute();
    }
    /**
     * CRUD FICHA RECLAMACAO
     */
    public static void InserirProduto(Produto prod , Connection con) throws SQLException, Exception
    {
        if(prod.notVazio()){

            String str = "Insert into SGProduto values(?,?)";
            PreparedStatement ps = con.prepareStatement(str);
            ps.setString(1, prod.getNome());
            ps.setString(2, prod.getGarantia());
            ps.execute();
           }
        else
        throw new Exception("Erro");
    }
    public static ResultSet PesquisarProduto(Connection con) throws SQLException,Exception
    {
        String sql = "select * from SGProduto";
        PreparedStatement ps = con.prepareStatement(sql);
        return ps.executeQuery(); 
    }
    public static ResultSet PesquisarProduto(String nome,Connection con) throws SQLException,Exception
    {
        String sql = "select * from SGProduto where prodNome=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, nome);
        return ps.executeQuery(); 
    }
    public static void DeletarProduto(int id,Connection con) throws SQLException
    {
        String str = "Delete from SGProduto where prodId = ?";
        PreparedStatement ps = con.prepareStatement(str);
        ps.setInt(1, id);
        ps.execute();
    }
    /**
     * CRUD FICHA RECLAMACAO
     */
    public static void InserirReclamacao(FichaReclamacao ficha , Connection con) throws SQLException
    {
        // public FichaReclamacao(int FichaCodigo, int ClienteCodigo, String Tipo, String Descricao, Date dataEmicao) {
        if(ficha.notVazio()){
            String str = "Insert into SGFichaDeReclamacao values(?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(str);
            
            ps.setInt(1, ficha.getClienteCodigo());
            ps.setString(2, ficha.getTipo());
            ps.setString(3, ficha.getDescricao());
            ps.setDate(4, ficha.getDataEmicao());
            ps.setInt(5, ficha.getIdArquivo());
            
            ps.execute();
           }
    }
    public static void AlterarReclamacaoPermitida(FichaReclamacao ficha,Connection con) throws SQLException
    {
        // public FichaReclamacao(int FichaCodigo, Date dataPrevista, String funcPrevista, int causaId, String correcao) {
        
            String str = "update set SGFichaDeReclamacao set ficDataPrevista = ? , ficFuncPrevista = ? , ficCausaId = ? , ficCorrecao = ? where ficId=?";
            PreparedStatement ps = con.prepareStatement(str);
            
            ps.setDate(1, ficha.getDataPrevista());
            ps.setString(2, ficha.getFuncPrevista());
            ps.setInt(3, ficha.getCausaId());
            ps.setString(4, ficha.getCorrecao());
            ps.setInt(5, ficha.getFichaCodigo());
            
            ps.execute();
    }
    public static void AlterarReclamacaoConcluida(FichaReclamacao ficha,Connection con) throws SQLException
    {
        // public FichaReclamacao(int FichaCodigo, java.sql.Date dataConcluido, String funcConcluido) {
        
        if(ficha.notVazio()){
            String str = "update set SGFichaDeReclamacao set ficDataConcluido=?,ficFuncConcluido=? where ficId=? ";
            PreparedStatement ps = con.prepareStatement(str);
            
            ps.setDate(1, ficha.getDataConcluido());
            ps.setString(2, ficha.getFuncConcluido());
            ps.setInt(3, ficha.getFichaCodigo());
            
            ps.execute();
           }
    }
    public static void AlterarReclamacaoNAceito(FichaReclamacao ficha,Connection con) throws SQLException
    {
        // public FichaReclamacao(int FichaCodigo, String motivoNaceita, String FunNAceito) {
        
        if(ficha.notVazio()){
            String str = "update set SGFichaDeReclamacao set ficMotivoNAceito=? , ficFunNAceito where ficId=? ";
            PreparedStatement ps = con.prepareStatement(str);
            
            ps.setString(1, ficha.getMotivoNaceita());
            ps.setString(2, ficha.getFunNAceito());
            ps.setInt(3, ficha.getFichaCodigo());
            
            ps.execute();
           }
    }
    public static void DeletarReclamacao(int id , Connection con) throws SQLException
    {
        if(id>0){
        String str = "Delete from SGFichaDeReclamacao where ficId = ?";

        PreparedStatement ps = con.prepareStatement(str);
        ps.setInt(1, id);
        ps.execute();
        }
    }
    public static ResultSet PesquisarReclamacao(Connection con) throws SQLException
    {
        String sql = "select * from SGFichaDeReclamacao";
        
        PreparedStatement ps = con.prepareStatement(sql);
        return ps.executeQuery();   
    }
    public static ResultSet PesquisarReclamacaoPorCliente(int idCliente , Connection con) throws SQLException
    {
        String sql = "select * from SGFichaDeReclamacao where ficCliId=?";
        
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idCliente);
        return ps.executeQuery();
    }
    public static ResultSet PesquisarReclamacaoPorID(int id , Connection con) throws SQLException
    {
        String sql = "select * from SGFichaDeReclamacao where ficId=?";
        
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        return ps.executeQuery();
    }
    public static ResultSet PesquisarReclamacaoPorCausa(String CausaId , Connection con) throws SQLException
    {
        String sql = "select * from SGFichaDeReclamacao where ficCausaId=?";
        
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1,CausaId);
        return ps.executeQuery();
    }
    public static ResultSet PesquisarReclamacaoPorTipo(String Tipo , Connection con) throws SQLException
    {
        String sql = "select * from SGFichaDeReclamacao where ficTipo=?";
        
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1,Tipo);
        
        return ps.executeQuery();

    }
    
}
