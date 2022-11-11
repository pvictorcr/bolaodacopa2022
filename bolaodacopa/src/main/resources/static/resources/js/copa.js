//TABELA COPA DO MUNDO 2022

//const pontos = document.getElementsByClassName("pontosList");
//const classif = document.getElementById("classif");
//const visaoGeral = document.getElementById("visaoGeral");

var aux = [];
var auxQualif = [];
var qualificados = [];

function closePenalti() {
    document.getElementById("verificacaopenaltis").style.display = "none";
}

function salvarJogo(grupo, jogo, numerodojogo, terminado){
	
	document.getElementById('tabelaCommand').setAttribute('action','/bolaodacopa/atualizarjogogrupo/');
	
	if(!isInt(document.getElementById('tabelagrupo_' + grupo).rows[jogo].cells[2].getElementsByTagName("input")[0].value) ||
		!isInt(document.getElementById('tabelagrupo_' + grupo).rows[jogo].cells[4].getElementsByTagName("input")[0].value))
		return;
		
	if(!confirm("Tem certeza que quer atualizar o jogo " + document.getElementById('tabelagrupo_' + grupo).rows[jogo].cells[1].innerText +
	'x' + document.getElementById('tabelagrupo_' + grupo).rows[jogo].cells[5].innerText +
	(terminado ? '' : ' com o placar ' + document.getElementById('tabelagrupo_' + grupo).rows[jogo].cells[2].getElementsByTagName("input")[0].value +
	'x' + document.getElementById('tabelagrupo_' + grupo).rows[jogo].cells[4].getElementsByTagName("input")[0].value) + '?'))
		return;
	
	document.getElementById('actionGrupos').value = grupo + '_' + jogo + '_' + numerodojogo + '_' + terminado;
	document.getElementById('tabelaCommand').submit();
}

function verificarPenaltis(fase, jogo, numerodojogo, terminado){
	
	document.getElementById('tabelaCommand').setAttribute('action','/bolaodacopa/atualizarjogoeliminatoria/');
	
	if(!isInt(document.getElementById('tabelaeliminatorias_' + fase).rows[jogo].cells[2].getElementsByTagName("input")[0].value) ||
		!isInt(document.getElementById('tabelaeliminatorias_' + fase).rows[jogo].cells[4].getElementsByTagName("input")[0].value))
		return;
		
	if(!confirm("Tem certeza que quer atualizar o jogo " + document.getElementById('tabelaeliminatorias_' + fase).rows[jogo].cells[1].innerText +
	'x' + document.getElementById('tabelaeliminatorias_' + fase).rows[jogo].cells[5].innerText +
	(terminado ? '' : ' com o placar ' + document.getElementById('tabelaeliminatorias_' + fase).rows[jogo].cells[2].getElementsByTagName("input")[0].value +
	'x' + document.getElementById('tabelaeliminatorias_' + fase).rows[jogo].cells[4].getElementsByTagName("input")[0].value) + '?'))
		return;
	
	if(!terminado && 
		document.getElementById('tabelaeliminatorias_' + fase).rows[jogo].cells[2].getElementsByTagName("input")[0].value == document.getElementById('tabelaeliminatorias_' + fase).rows[jogo].cells[4].getElementsByTagName("input")[0].value){
		
		document.getElementById('pais_1').setAttribute('onclick','javascript:salvarJogoPenaltis(' + fase + ',' + jogo + ',' + numerodojogo + ',' + terminado + ',' + 0 + ')'); 
		document.getElementById('pais_2').setAttribute('onclick','javascript:salvarJogoPenaltis(' + fase + ',' + jogo + ',' + numerodojogo + ',' + terminado + ',' + 1 + ')'); 
		document.getElementById('pais_1').src = document.getElementById('tabelaeliminatorias_' + fase).rows[jogo].cells[0].getElementsByTagName("img")[0].src;
		document.getElementById('pais_2').src = document.getElementById('tabelaeliminatorias_' + fase).rows[jogo].cells[6].getElementsByTagName("img")[0].src;
		document.getElementById('verificacaopenaltis').style.display = "block";
	}
	else{
		document.getElementById('actionEliminatorias').value = fase + '_' + jogo + '_' + numerodojogo + '_' + terminado;
		document.getElementById('tabelaCommand').submit();
	}
}

function salvarApostaGrupos(grupo, jogo, numerodojogo, terminado){
	
	document.getElementById('apostaCommand').setAttribute('action','/bolaodacopa/atualizarapostagrupo/');
	
	if(!isInt(document.getElementById('apostagrupo_' + grupo).rows[jogo].cells[2].getElementsByTagName("input")[0].value) ||
		!isInt(document.getElementById('apostagrupo_' + grupo).rows[jogo].cells[4].getElementsByTagName("input")[0].value))
		return;
		
	if(!confirm("Tem certeza que quer atualizar o jogo " + document.getElementById('apostagrupo_' + grupo).rows[jogo].cells[1].innerText +
	'x' + document.getElementById('apostagrupo_' + grupo).rows[jogo].cells[5].innerText +
	(terminado ? '' : ' com o placar ' + document.getElementById('apostagrupo_' + grupo).rows[jogo].cells[2].getElementsByTagName("input")[0].value +
	'x' + document.getElementById('apostagrupo_' + grupo).rows[jogo].cells[4].getElementsByTagName("input")[0].value) + '?'))
		return;
	
	document.getElementById('actionGrupos').value = grupo + '_' + jogo + '_' + numerodojogo + '_' + terminado;
	document.getElementById('apostaCommand').submit();
}

function salvarApostaEliminatorias(fase, jogo, numerodojogo, terminado){
	
	document.getElementById('apostaCommand').setAttribute('action','/bolaodacopa/atualizarapostaeliminatoria/');
	
	if(!isInt(document.getElementById('apostaeliminatorias_' + fase).rows[jogo].cells[2].getElementsByTagName("input")[0].value) ||
		!isInt(document.getElementById('apostaeliminatorias_' + fase).rows[jogo].cells[4].getElementsByTagName("input")[0].value))
		return;
		
	if(!confirm("Tem certeza que quer atualizar o jogo " + document.getElementById('apostaeliminatorias_' + fase).rows[jogo].cells[1].innerText +
	'x' + document.getElementById('apostaeliminatorias_' + fase).rows[jogo].cells[5].innerText +
	(terminado ? '' : ' com o placar ' + document.getElementById('apostaeliminatorias_' + fase).rows[jogo].cells[2].getElementsByTagName("input")[0].value +
	'x' + document.getElementById('apostaeliminatorias_' + fase).rows[jogo].cells[4].getElementsByTagName("input")[0].value) + '?'))
		return;
	
	document.getElementById('actionEliminatorias').value = fase + '_' + jogo + '_' + numerodojogo + '_' + terminado;
	document.getElementById('apostaCommand').submit();
}

function salvarFinal(){
	
	var vicecampeao = document.getElementById("vicecampeao");
	var campeao = document.getElementById("campeao");
	
	if(vicecampeao.value == 0 || campeao.value == 0){
		document.getElementById('campeao').focus();
		document.getElementById('mensagemdeerrofinal').innerText = 'Vice-Campeão e Campeão devem ser escolhidos.';
		return;
	}
	
	if(vicecampeao.value == campeao.value){
		document.getElementById('campeao').focus();
		document.getElementById('mensagemdeerrofinal').innerText = 'Vice-Campeão e Campeão não podem ser os mesmos.';
		return;
	}
	
	document.getElementById('actionEliminatorias').value = vicecampeao.value + '_' + campeao.value;
	document.getElementById('apostaCommand').setAttribute('action','/bolaodacopa/atualizarFinal/');
	document.getElementById('apostaCommand').submit();
}

function isInt(value) {
  return !isNaN(value) && 
         parseInt(Number(value)) == value && 
         !isNaN(parseInt(value, 10));
}

function salvarJogoPenaltis(fase, jogo, numerodojogo, terminado, vencedorPenaltis){
	
	document.getElementById('actionEliminatorias').value = fase + '_' + jogo + '_' + numerodojogo + '_' + terminado + '_' + vencedorPenaltis;
	document.getElementById('tabelaCommand').submit();
}

function escolherUsuarioDetalhes(email){
	document.getElementById('email').value = email;
	document.getElementById('classificacao').submit();
}

//Função resetar Dados
function reset(ident){

	//Zerando os dados dos Times
	for(var i = 0; i < times.length; i++){
		times[i].pontos = 0;
		times[i].jogos = 0;
		times[i].vitoria = 0;
		times[i].empate = 0;
		times[i].derrota = 0;
		times[i].golsPro = 0;
		times[i].golsContra = 0;
		times[i].saldoGols = 0;
	}

	//Zerando os auxiliares dos inputs
	for(var i = 0; i < 48; i++){
		aux[i] = new criarAux("a", "a");
	}

	//Resetando a tabela
	for(var i = 1; i < 5; i++){
		for(var j = 2; j < 11; j++){
			pontos[0].children[i].children[j].textContent = "";
		}
		pontos[0].children[i].children[0].textContent = "\u268B";
	}

	//Zerando os inputs
	eval(ident + "1a").value = ""
	eval(ident + "1b").value = ""

	eval(ident + "2a").value = ""
	eval(ident + "2b").value = ""

	eval(ident + "3a").value = ""
	eval(ident + "3b").value = ""

	eval(ident + "4a").value = ""
	eval(ident + "4b").value = ""

	eval(ident + "5a").value = ""
	eval(ident + "5b").value = ""

	eval(ident + "6a").value = ""
	eval(ident + "6b").value = ""

	visaoGeral.style.display = "none";

	localStorage.removeItem("times");
	localStorage.removeItem("aux");
	localStorage.removeItem("qualificados");
	localStorage.removeItem("auxAll");	
	localStorage.removeItem("finalista");
}