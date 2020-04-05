Para ejecutar la aplicacion utilizando Maven:

	mvn spring-boot:run -Drun.jvmArguments="-Dspring.profiles.active=dev"
	
	mvn test
	
	
Probando controllers:

	Obteniendo version de build:
	curl -X GET "http://localhost:8080/api/version/number"
	
	Realizando POST echo test:
	curl -X POST -H 'Content-Type: application/json' -H 'Accept: application/json' 
	-d '{"mensaje":"mensaje de prueba"}' localhost:8080/api/echo
	
	Obtener suma de numeros:
	curl -X GET "http://localhost:8080/api/math/sum?valores=1.1&valores=2.22&valores=3.33"
	
Probando aplicacion de cuentas corrientes.

    * Crear una cuenta corriente en ARS (ó USD ó EUR):
    
    curl -X POST \
      http://localhost:8080/api/account/create \
      -H 'Accept: */*' \
      -H 'Content-Type: application/json' \
      -d '{
    	"account_currency": "ARS",
    	"balance": 20000
    }'
    
    * Obtener una cuenta por numero de cuenta (antes creada):
    
    curl -X GET http://localhost:8080/api/account/{account_number}
    
    
    * Obtener todas las cuentas creadas: 
    
    curl -X GET http://localhost:8080/api/account/all
    
    
    * Obtener todos los movimientos de una cuenta:
    
    curl -X GET http://localhost:8080/api/account/{account_number}/movement/all
    
    * Agregar un movimiento (CREDIT ó DEBIT) a una cuenta ya creada:
    
    curl -X PUT \
      http://localhost:8080/api/account/7296490866458256204/movement/add \
      -H 'Accept: */*' \
      -H 'Content-Type: application/json' \
      -d '{
    	"movement_type": "CREDIT",
    	"description": "Some description",
    	"amount": 1000
    }'
    
    * Borrar una cuenta creada (solo se borrara la que no tiene movimientos)
    
    curl -X DELETE http://localhost:8080/api/account/delete/{account_number}
    
