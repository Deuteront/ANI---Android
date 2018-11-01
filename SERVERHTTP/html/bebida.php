<?php

header("Content-Type: text/html; charset=UTF-8");
//die(var_dump("aa"));
//$con = new PDO("mysql:host=localhost;dbname=pi", "root", ""); 
$a = getallheaders();
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
$listaIDs=str_replace("bebida","",$listaIDs);
$listaIDs=str_replace(":","",$listaIDs);
$numeroVirgula=substr_count($listaIDs, ",");
$numeroVirgula=$numeroVirgula+1;

for($i=0;$i<$numeroVirgula;$i++) {
  $res=preg_split("/,/",$listaIDs);
  echo $res[$i];
  fwrite($arq, "\nVALORES DO ARRAY: ".$res[$i]);
}
$id=$res[0];
$descricao=$res[1];
$nome=$res[2];
$preco=$res[3];


fclose($arq);


try{

			$conn = new PDO('mysql:host=localhost;dbname=trabalho_bd', "root", "");


		


			$sql = "INSERT INTO Bebida VALUES (".$id.", '".$descricao."', '".$nome."', ".$preco.")";

			$st = $conn->prepare($sql);

			$st->bindValue(":idBebida", $id, PDO::PARAM_STR);
			$st->bindValue(":descricao", $descricao, PDO::PARAM_STR);
			$st->bindValue(":nome", $nome, PDO::PARAM_STR);
			$st->bindValue(":preco", $preco, PDO::PARAM_INT);
			

			$st->execute();
		
			echo "ELE FAZ ESSA isso";
		}
		catch(PDOException $e){
			echo "Não conectou.";
			$e->getMensage() . "</br>";
		}




?>
