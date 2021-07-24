google.charts.load('current', {'packages':['line']});
google.charts.setOnLoadCallback(drawChart1);

function drawChart1() {
    var data = new google.visualization.DataTable();
    data.addColumn('number', 'Orden de venta');
    data.addColumn('number', 'Euros (€)');

    data.addRows([
        [1, 1678.90],
        [2, 1340],
        [3, 2150.30],
        [4, 1924.59],
        [5, 1678.90],
        [6, 1340],
        [7, 2150.30],
        [8, 1924.59],
        [9, 1678.90],
        [10, 1340],
        [11, 2150.30],
        [12, 1924.59],
        [13, 1678.90],
        [14, 1340],
        [15, 2150.30],
        [16, 1924.59]
    ]);

    var options = {
        height: 500,
        chart: {
            title: 'Rendimiento De Ventas',
            subtitle: 'Facturas ordenadas por fechas'
        },
        legend: { position: 'none' }
    };

    var chart = new google.charts.Line(document.getElementById('linechart_material'));
    chart.draw(data, google.charts.Line.convertOptions(options));
}

google.charts.load('current', {'packages':['bar']});
google.charts.setOnLoadCallback(drawChart2);

function drawChart2() {
    var data = new google.visualization.arrayToDataTable([
        ['Trimestre', 'Euros (€)'],
        ["Trimestre 1", 1678.90],
        ["Trimestre 2", 1340],
        ["Trimestre 3", 2150.30],
        ["Trimestre 4", 1924.59]
    ]);

    var options = {
        height: 500,
        chart: {
            title: 'Total Euros (€) Por Trimestre',
            subtitle: 'Muestra los trimestres del año actual'
        },
        legend: { position: 'none' }
    }

    var chart = new google.charts.Bar(document.getElementById('top_x_div'));
    chart.draw(data, google.charts.Bar.convertOptions(options));
}

google.charts.load('current', {'packages':['line']});
google.charts.setOnLoadCallback(drawChart3);

function drawChart3() {
    var data = new google.visualization.DataTable();
    data.addColumn('date', 'Fecha De La Factura');
    data.addColumn('number', 'Euros (€)');

    data.addRows([
        [/*[[${date}]]*/, 5.5]
    ]);

    var options = {
        height: 500,
        chart: {
            title: 'Repartición De Facturas A Lo Largo Del Año Actual',
            subtitle: 'Facturas ordenadas por fechas'
        },
        legend: { position: 'none' },
        hAxis: {
            format: 'd/M/yyyy',
            gridlines: { count : 5 }
        },
        vAxis: {
            minValue: 0,
            gridlines: { color : 'none' }
        }
    };

    var chart = new google.charts.Line(document.getElementById('linechart_material_date'));
    chart.draw(data, google.charts.Line.convertOptions(options));
}

