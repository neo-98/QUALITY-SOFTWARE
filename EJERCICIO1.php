<?php

	 /*
	  ini_set('display_errors', true);
	  error_reporting(E_ALL); 
	*/
  
	require_once('lib/nusoap.php');
	$error  = '';
	$result = array();
	$result_all = array();
	$response = '';
	$wsdl = "http://localhost/webServicesUSTA/webservice-server.php?wsdl";
	if(isset($_POST['sub'])){

		$isbn = trim($_POST['isbn']);
		//echo 'aca toy ISBN->'.$isbn;exit();
		if(!$isbn){
			$error = 'ISBN no puede estar en blanco.';
		}

		if(!$error){
			//create client object
			$client = new nusoap_client($wsdl, true);
			$err = $client->getError();

			if ($err) {
				echo '<h2>Error en el Constructor</h2>' . $err;
				// At this point, you know the call that follows will fail
			    exit();
			}
			 try {

				$result = $client->call('fetchBookData', array($isbn));
				$result = json_decode($result);
			  }catch (Exception $e) {
			    echo 'Caught exception: ',  $e->getMessage(), "\n";
			 }
		}
	}

	if(isset($_POST['sub_all'])){

		if(!$error){
			//create client object
			$client = new nusoap_client($wsdl, true);
			$err = $client->getError();

			if ($err) {
				echo '<h2>Error en el Constructor</h2>' . $err;
				// At this point, you know the call that follows will fail
			    exit();
			}
			 try {

				$result_all = $client->call('fetchBookDataAll');
				//echo 'aca voy2= <hr>';print_r($result_all);exit();
				$result_all = json_decode($result_all);
			  }catch (Exception $e) {
			    echo 'Caught exception: ',  $e->getMessage(), "\n";
			 }
		}
	}	

	/* Add new book **/
	if(isset($_POST['addbtn'])){
		$title = trim($_POST['title']);
		$isbn = trim($_POST['isbn']);
		$author = trim($_POST['author']);
		$category = trim($_POST['category']);
		$price = trim($_POST['price']);

		//Perform all required validations here
		if(!$isbn || !$title || !$author || !$category || !$price){
			$error = 'All fields are required.';
		}

		if(!$error){
			//create client object
			$client = new nusoap_client($wsdl, true);
			$err = $client->getError();
			if ($err) {
				echo '<h2>Constructor error</h2>' . $err;
				// At this point, you know the call that follows will fail
			    exit();
			}
			 try {
				/** Call insert book method */
				 $response =  $client->call('insertBook', array($title, $author, $price, $isbn, $category));
				 $response = json_decode($response);
			  }catch (Exception $e) {
			    echo 'Caught exception: ',  $e->getMessage(), "\n";
			 }
		}
	}

?>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Book Store Web Service</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>

<div class="container">
  <h2>Libros de la facultad de sistemas ( SOAP Web Service)</h2>
  <hr>
  <div class='row'>
  <form class="form-inline" method = 'post' name='form1'>
        <button type="submit" name='sub_all' class="btn btn-default">Información de todos los libros</button>

    </form>
     <br /> 
   </div>     
  <div class='row'>
  	<form class="form-inline" method = 'post' name='form1'>
  		<?php if($error) { ?> 
	    	<div class="alert alert-danger fade in">
    			<a href="#" class="close" data-dismiss="alert">&times;</a>
    			<strong>Error!</strong>&nbsp;<?php echo $error; ?> 
	        </div>
		<?php } ?>
	    <div class="form-group">
	      <label for="email">ISBN:</label>
	      <input type="text" class="form-control" name="isbn" id="isbn" placeholder="Ingrese el ISBN del libro" required>
	    </div>
	    <button type="submit" name='sub' class="btn btn-default">Obtener información del libro</button>
    </form>

   </div>
   <hr>
   <h2>Información del libro</h2>
  <table class="table">
    <thead>
      <tr>
        <th>Titulo</th>
        <th>Autor</th>
        <th>Precio</th>
        <th>ISBN</th>
        <th>Categoria</th>
      </tr>
    </thead>
    <tbody>
    <?php 
    	if($result){?>
		      <tr>
		        <td><?php echo $result->title; ?></td>
		        <td><?php echo $result->author_name; ?></td>
		        <td><?php echo $result->price; ?></td>
		        <td><?php echo $result->isbn; ?></td>	
		        <td><?php echo $result->category; ?></td>
		      </tr>
      <?php}
  		else{ ?>
  			<tr>
		        <td colspan='5'>Ingrese un ISBN valido y de click en el boton de traer información del libro</td>
		      </tr>
  		<?php } 
  			if($result_all){
  		    	foreach ($result_all as $fila => $data) {
  		    		echo '<tr><td>'.$data->title.'</td>'.
  		    			     '<td>'.$data->author_name.'</td>'.
  		    			     '<td>'.$data->price.'</td>'.
  		    			     '<td>'.$data->isbn.'</td>'.
  		    			     '<td>'.$data->category.'</td></tr>';
  		    	}
  		    }	

  		?>
    </tbody>
  </table>
	<div class='row'>
	<h2>Añadir un nuevo libro</h2>
	 <?php if(isset($response->status)) {

	  if($response->status == 200){ ?>
		<div class="alert alert-success fade in">
    			<a href="#" class="close" data-dismiss="alert">&times;</a>
    			<strong>Success!</strong>&nbsp; libro añadido satisfatoriamente. 
	        </div>
	  <?php }elseif(isset($response) && $response->status != 200) { ?>
			<div class="alert alert-danger fade in">
    			<a href="#" class="close" data-dismiss="alert">&times;</a>
    			<strong>Error!</strong>&nbsp; No fue posible agregar el libro(puede intentar nuevamente o contactar al creador del servicio)
	        </div>
	 <?php } 
	 }
	 ?>
  	<form class="form-inline" method = 'post' name='form1'>
  		<?php if($error) { ?> 
	    	<div class="alert alert-danger fade in">
    			<a href="#" class="close" data-dismiss="alert">&times;</a>
    			<strong>Error!</strong>&nbsp;<?php echo $error; ?> 
	        </div>
		<?php } ?>
	    <div class="form-group">
	      <label for="email"></label>
	      <input type="text" class="form-control" name="title" id="title" placeholder="Enter Title" required>
				<input type="text" class="form-control" name="author" id="author" placeholder="Enter Author" required>
				<input type="text" class="form-control" name="price" id="price" placeholder="Enter Price" required>
				<input type="text" class="form-control" name="isbn" id="isbn" placeholder="Enter ISBN" required>
				<input type="text" class="form-control" name="category" id="category" placeholder="Enter Category" required>
	    </div>
	    <button type="submit" name='addbtn' class="btn btn-default">Añadir nuevo libro</button>
    </form>
   </div>
</div>
<br>
</body>
</html>



