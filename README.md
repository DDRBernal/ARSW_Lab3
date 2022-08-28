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

##### Parte III. – Avance para el martes, antes de clase.

1) Revise el programa “highlander-simulator”, dispuesto en el paquete edu.eci.arsw.highlandersim. Este es un juego en el que:

Se tienen N jugadores inmortales.
- Cada jugador conoce a los N-1 jugador restantes.
- Cada jugador, permanentemente, ataca a algún otro inmortal. El que primero ataca le resta M puntos de vida a su contrincante, y aumenta en esta misma cantidad sus propios puntos de vida.
- El juego podría nunca tener un único ganador. Lo más probable es que al final sólo queden dos, peleando indefinidamente quitando y sumando puntos de vida.
2. Revise el código e identifique cómo se implemento la funcionalidad antes indicada. Dada la intención del juego, un invariante debería ser que la sumatoria de los puntos de vida de todos los jugadores siempre sea el mismo(claro está, en un instante de tiempo en el que no esté en proceso una operación de incremento/reducción de tiempo). Para este caso, para N jugadores, cual debería ser este valor?.

El valor deberia ser N*100, si por ejemplo existen 3 jugadores, la vida deberia ser igual a 300.

3. Ejecute la aplicación y verifique cómo funcionan las opción ‘pause and check’. Se cumple el invariante?.

Como podemos observar en la imagen no se cumple, pues en este caso la vida debe ser de 400 porque hay 4 inmortales. 

![image](https://user-images.githubusercontent.com/46855679/187048172-853b60eb-756e-4185-b510-336f768fc786.png)

4. Una primera hipótesis para que se presente la condición de carrera para dicha función (pause and check), es que el programa consulta la lista cuyos valores va a imprimir, a la vez que otros hilos modifican sus valores. Para corregir esto, haga lo que sea necesario para que efectivamente, antes de imprimir los resultados actuales, se pausen todos los demás hilos. Adicionalmente, implemente la opción ‘resume’.

![image](https://user-images.githubusercontent.com/46855679/187053238-065dded3-3db4-40de-91b8-97564fd531d7.png)

5. Verifique nuevamente el funcionamiento (haga clic muchas veces en el botón). Se cumple o no el invariante?.

Como podemos observar funciona correctamente.

![image](https://user-images.githubusercontent.com/46855679/187054120-e4eaf9e1-4c7c-480e-86a9-f2287b3922a0.png)

6. Identifique posibles regiones críticas en lo que respecta a la pelea de los inmortales. Implemente una estrategia de bloqueo que evite las condiciones de carrera. Recuerde que si usted requiere usar dos o más ‘locks’ simultáneamente, puede usar bloques sincronizados anidados:

	```java
	synchronized(locka){
		synchronized(lockb){
			…
		}
	}
	```
 
 ![image](https://user-images.githubusercontent.com/46855679/187054175-1a4e0573-3e20-45f5-988c-3a07b476f302.png)
 
 Esta es una posible región critica que podria ocasionar resultados innesperados, para usaremos la palabra reservada *synchonized*.
 
 ![image](https://user-images.githubusercontent.com/46855679/187055740-3edae4eb-a47d-47a8-8119-3f158e392750.png)

. Tras implementar su estrategia, ponga a correr su programa, y ponga atención a si éste se llega a detener. Si es así, use los programas jps y jstack para identificar por qué el programa se detuvo.

8. Plantee una estrategia para corregir el problema antes identificado (puede revisar de nuevo las páginas 206 y 207 de _Java Concurrency in Practice_).

9. Una vez corregido el problema, rectifique que el programa siga funcionando de manera consistente cuando se ejecutan 100, 1000 o 10000 inmortales. Si en estos casos grandes se empieza a incumplir de nuevo el invariante, debe analizar lo realizado en el paso 4.

10. Un elemento molesto para la simulación es que en cierto punto de la misma hay pocos 'inmortales' vivos realizando peleas fallidas con 'inmortales' ya muertos. Es necesario ir suprimiendo los inmortales muertos de la simulación a medida que van muriendo. Para esto:
	* Analizando el esquema de funcionamiento de la simulación, esto podría crear una condición de carrera? Implemente la funcionalidad, ejecute la simulación y observe qué problema se presenta cuando hay muchos 'inmortales' en la misma. Escriba sus conclusiones al respecto en el archivo RESPUESTAS.txt.
	* Corrija el problema anterior __SIN hacer uso de sincronización__, pues volver secuencial el acceso a la lista compartida de inmortales haría extremadamente lenta la simulación.

11. Para finalizar, implemente la opción STOP.