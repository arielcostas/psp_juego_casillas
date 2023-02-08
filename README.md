# Juego tablero de premios

Ejercicio de clase para la asignatura de Programación de Servicios y Procesos del CFGS de DAM.

## Descripción

El juego consiste en un tablero de 3x4 casilllas, en que algunas de ellas tienen premios y otras no. El jugador debe
elegir una casilla y si tiene premio, se lo lleva. Si no tiene premio, se le indica que no tiene premio
y se le da la opción de elegir otra casilla.

El juego funciona con arquitectura cliente-servidor, en que el cliente es un programa que se ejecuta en la
consola de comandos y el servidor es un programa que se ejecuta en un servidor remoto.

Se ha escrito un protocolo de mensajes de red para que el cliente y el servidor se comuniquen entre sí; funciona
por encima de TCP/IP en el puerto 9696 (por defecto).

## Requisitos

* Java 17 LTS o superior
* IntelliJ IDEA (puede que funcione con otras IDEs, pero no se ha probado)

## Posibles mejoras

- [ ] Uso de Maven para la gestión de dependencias
- [ ] Configuración de puerto y host del servidor desde el cliente
- [ ] Configuración de puerto y host en los que escucha el servidor
- [ ] Configuración de número de casillas y número de premios
- [ ] Configuración de número de intentos
- [ ] Configuración de número de jugadores máximos (después no se aceptan más conexiones)
- [ ] Configuración de número de jugadores simultáneos (después no se aceptan más conexiones)
- [ ] Uso de sistema de logs mejor
- [ ] Mejorar cómo se muestra al cliente los premios obtenidos
- [ ] Mejorar la salida al finalizar el juego (cliente)
- [ ] Mejorar la salida al finalizar el juego (servidor)
- [ ] Uso de sockets SSL para cifrar la comunicación
