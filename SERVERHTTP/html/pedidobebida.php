<?php

$arq = fopen("ARQUIVOTESTEDOPOST.TXT", "w");
fwrite($arq, "\n\nEsta gfhfdgj4352436345 \n");
$listaIDs =json_encode($_POST);

fwrite($arq, "\n\nEsta aqui \n");
fwrite($arq, "\n\nPOST: ".json_encode($_POST)."\n ");
echo $listaIDs;

$listaIDs=str_replace("\\","",$listaIDs);
$listaIDs=str_replace("/","",$listaIDs);
$listaIDs=str_replace('"',"",$listaIDs);
$listaIDs=str_replace("{","",$listaIDs);
$listaIDs=str_replace('["',"",$listaIDs);
$listaIDs=str_replace(']"',"",$listaIDs);
$listaIDs=str_replace("}","",$listaIDs);
$listaIDs=str_replace("pedidoBebida","",$listaIDs);
$listaIDs=str_replace(":","",$listaIDs);
$numeroVirgula=substr_count($listaIDs, ",");
$numeroVirgula=$numeroVirgula+1;

for($i=0;$i<$numeroVirgula;$i++) {
  $res=preg_split("/,/",$listaIDs);
  echo $res[$i];
  fwrite($arq, "\nVALORES DO ARRAY: ".$res[$i]);
}
$qtBebidaPedido=$res[0];
$subTotal=$res[1];
$idBebida=$res[2];
$obs=$res[4];





fclose($arq);


try{

			$conn = new PDO('mysql:host=localhost;dbname=trabalho_bd', "root", "");


		


			$sql = "INSERT INTO Pedido (qtBebidaPedido,subTotal,idBebida,obs) VALUES (".$qtBebidaPedido.", ".$subTotal.", ".$idBebida.",'".$obs."')";

			$st = $conn->prepare($sql);

			

			$st->execute();
		
			echo "ELE FAZ ESSA isso";
		}
		catch(PDOException $e){
			echo "Não conectou.";
			$e->getMensage() . "</br>";
		}



?>
