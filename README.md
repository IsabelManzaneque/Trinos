# Trinos
Implementación de un sistema básico de microblogging

## Tabla de contenidos
* [Información general](#información-general)
* [Estructura del sistema](#estructura-del-sistema)
* [Tecnologías](#tecnologías)
* [Setup](#setup)

## Información general

Este proyecto implementa la simulación de un sistema básico de microblogging. Este software permitirá a una serie de usuarios registrarse en el
sistema de mensajería instantánea y realizar distintas interacciones con otros usuarios como seguir o dejar de seguir, enviar trinos, búsqueda 
de usuarios en el sistema etc.

Para la implementación de este sistema se ha utilizado la tecnología Java RMI, un mecanismo que permite realizar llamadas a métodos de objetos 
remotos situados en distintas (o la misma) máquinas virtuales Java, compartiendo así recursos y carga de procesamiento a través de varios
sistemas. RMI permite exportar objetos como objetos remotos para que otro proceso remoto pueda acceder directamente como un objeto Java. 
Como RMI forma parte del API de Java, la integración de objetos remotos en aplicaciones distribuidas se realiza sin necesidad de usar recursos 
adicionales y permite utilizar la misma sintaxis para llamadas a objetos remotos y locales.


## Estructura del sistema

El sistema se compone de 3 entidades, cada una con una serie de responsabilidades concretas, que se comunican para ofrecer la funcionalidad completa del
sistema. Las entidades son las siguientes:

• Servidor: Se encarga de controlar el proceso de autenticación de los usuarios del sistema y gestión de sus trinos, para ello hace uso de dos servicios:

* Servicio Autenticación: Se encarga de registrar y de autenticar a los usuarios del
sistema.
* Servicio Gestor: Se encarga de gestionar todas las operaciones de los usuarios en
relación con enviar trinos, bloquear y hacerse seguidor de otros usuarios

• Base de datos: La encargada de almacenar todos los datos del sistema: Usuarios, Seguidores, Trinos etc. Hace uso del Servicio de datos, que posibilita las 
relaciones entre trinos y usuarios

• Clientes: Actores principales del sistema, que pueden interactuar entre ellos enviándose trinos y haciéndose seguidores unos de otros. 
Hacen uso de un callback para recibir los trinos de los usuarios a los que siguen

El servidor establece un flujo de comunicación con la base de datos y es el único que puede consumir el servicio que esta suministra. El servidor establece 
a su vez un flujo de comunicación con los clientes y estos pueden así utilizar los servicios del sistema de microblogging. Podemos de esta manera ver a la
entidad servidor como el intermediario entre la base de datos y los clientes.

<img width="473" alt="image" src="https://user-images.githubusercontent.com/86284395/214815251-c88f1f77-6e47-424c-99ba-8c6e7655fd30.png">


## Tecnologías

* IDE: Eclipse 2021 – 03
* Java version "18" 2022-03-22
* Java(TM) SE Runtime Environment (build 18+36-2087)
* Java HotSpot(TM) 64-Bit Server VM (build 18+36-2087, mixed mode, sharing)


## Setup
Para arrancar el proyecto:

```
Descargar y extraer .zip del proyecto
Ejecutar archivos .bat en el siguiente orden:
 1 - basededatos.bat
 2 - servidor.bat
 3 - cliente.bat
Se pueden crear múltiples procesos cliente pero solo 
un proceso basededatos y un proceso servidor

```

