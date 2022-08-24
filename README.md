# ARSW_Lab3 - David Ricardo Otálora Bernal

##### Parte I – Antes de terminar la clase.

Control de hilos con wait/notify. Productor/consumidor.

1. Revise el funcionamiento del programa y ejecútelo. Mientras esto ocurren, ejecute jVisualVM y revise el consumo de CPU del proceso correspondiente. A qué se debe este consumo?, cual es la clase responsable?
	
	Tenemos la clase **StartProduction**, esta clase contiene el hilo "coordinador" encargado de las clasess **"Consumer"** y **"Producer"**, primero se lanza el hilo de Producer agregando numeros aleatorios
	a una cola, después de 5 segundos, arranca el segundo hilo de "Consumer".
	El problema es que una vez arranca a consumir, estaria producion y consumiendo al mismo tiempo (es decir los dos hilos se estan ejecutando)
	y al estar en paralelo, esto provoca que el consumo de CPU aumente considerablemente.
	
	![image](https://user-images.githubusercontent.com/46855679/186445479-3d0c93c7-d980-4bd5-8cf6-374877867897.png)
	> Aqui podemos observar un consumo elevado despues de la ejecución de los dos hilos en paralelo	


2. Haga los ajustes necesarios para que la solución use más eficientemente la CPU, teniendo en cuenta que -por ahora- la producción es lenta y el consumo es rápido. Verifique con JVisualVM que el consumo de CPU se reduzca.
	Si ponemos a **dormir** el hilo del consumo durante unos segundos, despues de producir N cantidad de elementos, 
	podemos observar una buena reducción del consumo de la CPU; debido a que solo el hilo de produccion estaria activo todo el tiempo
	mientras que el hilo de consumidor unicamente durente un corto tiempo.
	
	![image](https://user-images.githubusercontent.com/46855679/186433982-d5a706f4-b2ae-419a-86a2-ee8571e64295.png)

3. Haga que ahora el productor produzca muy rápido, y el consumidor consuma lento. Teniendo en cuenta que el productor conoce un límite de Stock (cuantos elementos debería tener, a lo sumo en la cola), haga que dicho límite se respete. Revise el API de la colección usada como cola para ver cómo garantizar que dicho límite no se supere. Verifique que, al poner un límite pequeño para el 'stock', no haya consumo alto de CPU ni errores.

	Para garantizar una produccion alta y que el consumidor consuma lento, debemos cambiar el **sleep** del primer hilo, por un menor tiempo, 
	y para el hilo de consumidor agregaremos un tiempo de espera despues de consumir un elemento, asi garantizaremos que el consumo de la CPU
	sea bajo (mientras un hilo produce el otro consume y luego se detiene por un tiempo)	
	
	![image](https://user-images.githubusercontent.com/46855679/186445230-f3ee6561-4f0a-4fb9-900f-6f5acc7ffb5e.png)
	
	El limite de Stock lo agregaremos como condicion en el while de la clase Producer y Consumer.
	
	![image](https://user-images.githubusercontent.com/46855679/186445901-fd4cb0ec-50ac-48d1-bc43-4a0609a1df53.png)


##### Parte II. – Antes de terminar la clase.

Teniendo en cuenta los conceptos vistos de condición de carrera y sincronización, haga una nueva versión -más eficiente- del ejercicio anterior (el buscador de listas negras). En la versión actual, cada hilo se encarga de revisar el host en la totalidad del subconjunto de servidores que le corresponde, de manera que en conjunto se están explorando la totalidad de servidores. Teniendo esto en cuenta, haga que:

- La búsqueda distribuida se detenga (deje de buscar en las listas negras restantes) y retorne la respuesta apenas, en su conjunto, los hilos hayan detectado el número de ocurrencias requerido que determina si un host es confiable o no (_BLACK_LIST_ALARM_COUNT_).
- Lo anterior, garantizando que no se den condiciones de carrera.

