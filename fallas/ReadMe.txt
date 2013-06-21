Lista de los servidores:
	Para saber donde migrar, el servidor corriente utiliza el fichero 
	listAddr.txt que contiene las caracteristicas de los distintos
	servidores.
	El primer servidor migra en el segundo, el segundo en el tercero, ...,
	y el ultimo en el primero.
	
	Los 4 elementos de cada linea del fichero son:
	-El nombre del servidor
	-El IP del servidor
	-Dos numeros que  corresponden
	a los port utilizados por los servidores para discutir entre
	ellos y con los clientes durante las migraciones.
	Si el port i es utilizado para un servidor todos los otros port
	no deben ser entre i y i+10.

Crear un servidor:
	->Para crear el servidor principal:
	java Server n name

	Donde n es el numero de jugador y name el nombre del servidor.

	->Para crear un servidor secundario que va a espaerar una migracion:
	java Server wait name

	Donde name es el nombre del servidor.


Crear un cliente:
	java Client IPserver
