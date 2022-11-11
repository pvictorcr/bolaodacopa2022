function salvarApostaGruposAdm(grupo, jogo, numerodojogo, terminado){
	
	document.getElementById('apostaCommand').setAttribute('action','/bolaodacopa/atualizargrupousuario/');
	
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

function salvarApostaEliminatoriasAdm(fase, jogo, numerodojogo, terminado){
	
	document.getElementById('apostaCommand').setAttribute('action','/bolaodacopa/atualizareliminatoriausuario/');
	
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

function salvarFinalAdm(){
	
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
	document.getElementById('apostaCommand').setAttribute('action','/bolaodacopa/atualizarFinalUsuario/');
	document.getElementById('apostaCommand').submit();
}

function isInt(value) {
  return !isNaN(value) && 
         parseInt(Number(value)) == value && 
         !isNaN(parseInt(value, 10));
}

function escolherUsuarioDetalhesAdm(email){
	document.getElementById('email').value = email;
	document.getElementById('listausuarios').submit();
}