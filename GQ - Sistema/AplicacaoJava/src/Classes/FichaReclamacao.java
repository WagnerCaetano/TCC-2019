/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Util.BDConexao;
import Util.CRUDTodos;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 *
 * @author u18300
 */
public class FichaReclamacao {
    private int FichaCodigo;
    private int ClienteCodigo;
    private String Tipo;
    private String Descricao;
    private int idArquivo;
    
    private Date dataEmicao;
    private String funcEmicao;
    
    private String motivoNaceita;
    private String FunNAceito;
    
    private Date dataPrevista;
    private String funcPrevista;
    private int causaId;
    private String correcao;
    
    private Date dataConcluido;
    private String funcConcluido;
    

    public FichaReclamacao(int FichaCodigo, int ClienteCodigo, String Tipo, String Descricao, Date dataEmicao) {
        this.FichaCodigo = FichaCodigo;
        this.ClienteCodigo = ClienteCodigo;
        this.Tipo = Tipo;
        this.Descricao = Descricao;
        this.dataEmicao = dataEmicao;
    }
    public FichaReclamacao(int ClienteCodigo, String Tipo, String Descricao, Date dataEmicao ,int idArquivo) {
        this.ClienteCodigo = ClienteCodigo;
        this.Tipo = Tipo;
        this.Descricao = Descricao;
        this.dataEmicao = dataEmicao;
        this.idArquivo = idArquivo;
    }

    public FichaReclamacao(int FichaCodigo, String motivoNaceita, String FunNAceito) {
        this.FichaCodigo = FichaCodigo;
        this.motivoNaceita = motivoNaceita;
        this.FunNAceito = FunNAceito;
    }

    public FichaReclamacao(int FichaCodigo, Date dataPrevista, String funcPrevista, int causaId, String correcao) {
        this.FichaCodigo = FichaCodigo;
        this.dataPrevista = dataPrevista;
        this.funcPrevista = funcPrevista;
        this.causaId = causaId;
        this.correcao = correcao;
    }

    public FichaReclamacao(int FichaCodigo, Date dataConcluido, String funcConcluido) {
        this.FichaCodigo = FichaCodigo;
        this.dataConcluido = dataConcluido;
        this.funcConcluido = funcConcluido;
    }

    public String getMotivoNaceita() {
        return motivoNaceita;
    }

    public void setMotivoNaceita(String motivoNaceita) {
        this.motivoNaceita = motivoNaceita;
    }

    public String getFunNAceito() {
        return FunNAceito;
    }

    public void setFunNAceito(String FunNAceito) {
        this.FunNAceito = FunNAceito;
    }
    
    
    

    public String getClienteCpf() throws SQLException, ClassNotFoundException
    {
        BDConexao c = new BDConexao();
        String cpfFora = null;
        ResultSet rs = CRUDTodos.PesquisarClientePorId(this.ClienteCodigo, c.criaConexao());
        if(rs.next())
            cpfFora = rs.getString("cliCPF");
        return cpfFora;
    }
    public String getClienteNome() throws SQLException, ClassNotFoundException
    {
        BDConexao c = new BDConexao();
        String nomeFora = null;
        ResultSet rs = CRUDTodos.PesquisarClientePorId(this.ClienteCodigo, c.criaConexao());
        if(rs.next())
            nomeFora = rs.getString("cliNome");
        return nomeFora;
    }
    
    public boolean notVazio()
    {
        return (this.idArquivo > 0 && this.ClienteCodigo > 0 && this.Tipo.length() > 0 && this.Descricao.length() > 0 && this.dataEmicao.toString().length() > 0 );
    }

    public int getIdArquivo() {
        return idArquivo;
    }

    public void setIdArquivo(int idArquivo) {
        this.idArquivo = idArquivo;
    }

    
    
    public int getFichaCodigo() {
        return FichaCodigo;
    }

    public void setFichaCodigo(int FichaCodigo) {
        this.FichaCodigo = FichaCodigo;
    }

    
    
    public int getClienteCodigo() {
        return ClienteCodigo;
    }

    public void setClienteCodigo(int ClienteCodigo) {
        this.ClienteCodigo = ClienteCodigo;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String Tipo) {
        this.Tipo = Tipo;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String Descricao) {
        this.Descricao = Descricao;
    }

    public Date getDataEmicao() {
        return dataEmicao;
    }

    public void setDataEmicao(Date dataEmicao) {
        this.dataEmicao = dataEmicao;
    }

    public String getFuncEmicao() {
        return funcEmicao;
    }

    public void setFuncEmicao(String funcEmicao) {
        this.funcEmicao = funcEmicao;
    }

    public Date getDataPrevista() {
        return dataPrevista;
    }

    public void setDataPrevista(Date dataPrevista) {
        this.dataPrevista = dataPrevista;
    }

    public String getFuncPrevista() {
        return funcPrevista;
    }

    public void setFuncPrevista(String funcPrevista) {
        this.funcPrevista = funcPrevista;
    }

    public Date getDataConcluido() {
        return dataConcluido;
    }

    public void setDataConcluido(Date dataConcluido) {
        this.dataConcluido = dataConcluido;
    }

    public String getFuncConcluido() {
        return funcConcluido;
    }

    public void setFuncConcluido(String funcConcluido) {
        this.funcConcluido = funcConcluido;
    }

    public int getCausaId() {
        return causaId;
    }

    public void setCausaId(int causaId) {
        this.causaId = causaId;
    }

    public String getCorrecao() {
        return correcao;
    }

    public void setCorrecao(String correcao) {
        this.correcao = correcao;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.ClienteCodigo;
        hash = 59 * hash + Objects.hashCode(this.Tipo);
        hash = 59 * hash + Objects.hashCode(this.Descricao);
        hash = 59 * hash + Objects.hashCode(this.dataEmicao);
        hash = 59 * hash + Objects.hashCode(this.funcEmicao);
        hash = 59 * hash + Objects.hashCode(this.dataPrevista);
        hash = 59 * hash + Objects.hashCode(this.funcPrevista);
        hash = 59 * hash + Objects.hashCode(this.dataConcluido);
        hash = 59 * hash + Objects.hashCode(this.funcConcluido);
        hash = 59 * hash + this.causaId;
        hash = 59 * hash + Objects.hashCode(this.correcao);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FichaReclamacao other = (FichaReclamacao) obj;
        if (this.ClienteCodigo != other.ClienteCodigo) {
            return false;
        }
        if (!Objects.equals(this.Tipo, other.Tipo)) {
            return false;
        }
        if (!Objects.equals(this.Descricao, other.Descricao)) {
            return false;
        }
        if (!Objects.equals(this.dataEmicao, other.dataEmicao)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FichaReclamacao{" + "ClienteCodigo= " + ClienteCodigo + ", Tipo= " + Tipo + ", Descricao= " + Descricao + ", dataEmicao= " + dataEmicao + ", funcEmicao= " + funcEmicao + 
                ", dataPrevista= " + dataPrevista + ", funcPrevista= " + funcPrevista + ", dataConcluido= " + dataConcluido + ", funcConcluido= " + funcConcluido + ", causaId= " + causaId + 
                ", correcao= " + correcao + '}';
    }
    
    
    
    
    
    
}
