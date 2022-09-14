
//Menu lateral
function openNav() {
    document.getElementById("mySidenav").style.width = "250px";
}
function closeNav() {
    document.getElementById("mySidenav").style.width = "0";
}

async function carrega_apostas(){
	
	const contentDiv = document.getElementById("mainDiv");
    contentDiv.innerHTML = await fetchHtmlAsText("/apostas");
}

async function fetchHtmlAsText(url) {
    return await (await fetch(url)).text();
}