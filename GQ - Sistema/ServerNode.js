const express = require('express');
const bodyParser = require('body-parser');
const sql = require('mssql');

const app = express();
const port = 54000;
const connStr = "Server=00-PC; Database=BD_SistemaReclamacao; User Id=Admin; Password=20092001;";

  sql.connect(connStr)
   .then(conn => global.conn = conn)
    .catch(err => console.log(err));

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json({ limit: '50MB' }));

function execSQLQuery(sqlQry, res){
    global.conn.request()
    .query(sqlQry)
    .then(result => res.json(result.recordset))
    .catch(err => res.json(err));
    res.setHeader('Access-Control-Allow-Origin', '*');
    }


const router = express.Router();
router.get('/', (request, response) => response.json({message: 'Funcionando!'}));

router.get('/reclamacoes', (req, res) =>{execSQLQuery('SELECT * FROM SGFichaDeReclamacao', res);

router.get('/reclamacoes/:id?', (req, res) =>{
 let filter = '';
 if(req.params.id) 
    filter = ' WHERE ficCliId=' + parseInt(req.params.id);
    execSQLQuery('SELECT * FROM SGFichaDeReclamacao' + filter, res);
 })
})

router.post('/clientes/inserir/',(req,res) =>{
    const nome  = req.body.nome.substring(0,50);
    const email = req.body.email.substring(0,30);
    const cpf = req.body.cpf.substring(0,11);
    const celular = req.body.celular.substring(0,11);
    const senha = req.body.csenha.substring(0, 56);
    const cep = req.body.CEP.substring(0,30);
    const endereco = req.body.endereco;
    const cidade = req.body.cidade.substring(0,30);

    execSQLQuery(`insert into SGClienteFicha (cliNome,cliEmail,cliCPF,cliTel,cliPsw,cliCEP,cliAddress,cliCidade) values('${nome}',' ${email}', '${cpf}', '${celular}', '${senha}', '${cep}', '${endereco}', '${cidade}')`,res);
})

router.post('/reclamacoes/inserirReclamacoes/',(req,res) =>{
    const id = req.body.id;
    const cars = req.body.cars;
    const feedback = req.body.feedback;
    const idArquivo = req.body.iddeArquivo;
    process.stdout.write("id: "+id+" "+cars+" "+feedback+" "+idArquivo);
    // COLOCAR METODO NO JAVASCRIPT PARA PUXAR ID DO CLIENTE
    execSQLQuery(`insert into SGFichaDeReclamacao (ficCliId,ficTipo,ficDesc,ficDataEmicao,ficArqId) values(${id},' ${cars}', '${feedback}', getdate(),${idArquivo})`,res);
})

router.post('/reclamacoes/inserirArquivo/',(req,res) =>{
    const id  = req.body.id;
    const file = req.body.arquivo;
    // COLOCAR METODO NO JAVASCRIPT PARA PUXAR ID DO CLIENTE
    execSQLQuery(`insert into SGArquivos (Id,Nome,Arquivo) values (${id},'${id}'+'.pdf', CONVERT(VARBINARY(max), '${file}'))`,res);
})

router.get('/clientes/',(req,res) =>{
    execSQLQuery('SELECT * from SGClienteFicha',res);
})
router.get('/reclamacoes/id/',(req,res) =>{
    execSQLQuery(`select max(Id) as 'UltimoId' from SGArquivos`,res);
})

app.use('/', router);

app.listen(port);
console.log('API funcionando!');