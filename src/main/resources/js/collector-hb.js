function makeTextFile(textToWrite) {
  var downloadLink = document.createElement("a");
  // downloadLink.innerHTML = "Download File";

  var fileNameToSaveAs =
    "collector-hb-" + new Date().getTime() + ".json";
  downloadLink.download = fileNameToSaveAs;

  var textFileAsBlob = new Blob([textToWrite], { type: "text/plain" });
  downloadLink.href = (window.webkitURL || window.URL).createObjectURL(
    textFileAsBlob
  );

  downloadLink.click();
  window.webkitURL.revokeObjectURL(textFileAsBlob);
}

function onlyNumber(x) {
  if (!x) return x;
  x = x
    .replace(/[^0-9\.\,-]+/g, "")
    .replace(",", ".")
    .replace("+", "")
    .replace("%", "");
  return x.includes(".") ? parseFloat(x).toFixed(2) : parseInt(x);
}

function transformNumber(n) {
  var map = [
    {
      multiplicador: 1000,
      simbolo: "K",
    },
    {
      multiplicador: 1000000,
      simbolo: "M",
    },
    {
      multiplicador: 1000000000,
      simbolo: "B",
    },
  ];

  var multiplicador = 1;
  for (i = 0; i < map.length; i++) {
    var simbolo = map[i].simbolo;

    if (n.includes(simbolo)) {
      n = n.replace(simbolo, "");
      multiplicador = map[i].multiplicador;
      break;
    }
  }

  return parseInt(onlyNumber(n) * multiplicador);
}

function runCollector(interval) {
  var dLinhas = document.querySelectorAll(".body_grid table tr");

  var linhas = Array.prototype.slice
    .call(dLinhas)
    .filter(function (linha, ilinha) {
      return !(ilinha == 0 || ilinha == dLinhas.length - 1);
    });

  var dt = new Date();
  dt =
    dt.toLocaleDateString() +
    " " +
    dt.getHours() +
    ":" +
    dt.getMinutes() +
    ":" +
    dt.getSeconds() +
    "." +
    dt.getMilliseconds();
  var acoes = [];

  linhas.forEach((linha, i) => {
    var cols = linha.querySelectorAll("td");

    //
    var nome = cols[1].querySelector("input").value;

    var ultimo = onlyNumber(cols[2].querySelector("span").innerHTML);

    if( ultimo == 0 || ultimo == '0' || ultimo == null || ultimo == '' )
      return;

    var variacao = onlyNumber(cols[3].querySelector("span").innerHTML);

    var compra = onlyNumber(cols[4].querySelector("span").innerHTML);

    var venda = onlyNumber(cols[5].querySelector("span").innerHTML);

    var min = onlyNumber(cols[6].querySelector("span").innerHTML);

    var max = onlyNumber(cols[8].querySelector("span").innerHTML);

    var abertura = onlyNumber(cols[9].querySelector("span").innerHTML);

    var fech = onlyNumber(cols[10].querySelector("span").innerHTML);

    var volume = transformNumber(cols[11].querySelector("span").innerHTML);

    var nneg = transformNumber(cols[12].querySelector("span").innerHTML);

    var acao = {
      stockDescription: nome,
      currentPrice: ultimo,
      variation: variacao,
      buyPrice: compra,
      sellPrice: venda,
      minPriceDay: min,
      maxPriceDay: max,
      openPrice: abertura,
      closePrice: fech,
      buyVolume: volume,
      negotiationVolume: nneg,
      dtCollectStr: dt,
    };
    acoes.push(acao);
  });

  if (interval)
    setTimeout(function () {
      runCollector(interval);
    }, interval);

  makeTextFile(JSON.stringify(acoes));
  return acoes;
}

var startAt = 0;

//start 10:00
var now = new Date();
var startAt = (600 - (now.getHours() * 60 + now.getMinutes())) * 60 * 1000;
startAt = startAt < 0 ? 0 : startAt;

var interval = 15000;
setTimeout(function () {
  runCollector(interval);
}, startAt);

// var acoes = runCollector(2000);
// console.log(acoes[0]);
