function carregaChart(){

	google.charts.load('current', {'packages':['line']});
	google.charts.setOnLoadCallback(drawChart);
}

function drawChart() {
	
	var data = new google.visualization.DataTable();
    data.addColumn('number', 'Jogos Finalizados');
    
    //nomes.foreach(data.addColumn('number', currentValue));
    for (const element of nomes) {
  		data.addColumn('number', element)
	}

    data.addRows(chartData);

      var options = {
        chart: {
          title: titulo,
          subtitle: subtitulo
        },
        width: 900,
        height: 500
        };

      var chart = new google.charts.Line(document.getElementById('grafico'));

      chart.draw(data, google.charts.Line.convertOptions(options));
}	